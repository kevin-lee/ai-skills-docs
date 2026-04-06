import just.semver.SemVer
import extras.scala.io.syntax.color.*

ThisBuild / scalaVersion := props.ProjectScalaVersion
ThisBuild / organization := "io.kevinlee"
ThisBuild / organizationName := "Kevin's Code"

ThisBuild / developers := List(
  Developer(
    props.GitHubUsername,
    "Kevin Lee",
    "kevin.code@kevinlee.io",
    url(s"https://github.com/${props.GitHubUsername}"),
  )
)
ThisBuild / homepage := url(
  s"https://github.com/${props.GitHubUsername}/${props.RepoName}"
).some
ThisBuild / scmInfo :=
  ScmInfo(
    browseUrl =
      url(s"https://github.com/${props.GitHubUsername}/${props.RepoName}"),
    connection =
      s"scm:git:git@github.com:${props.GitHubUsername}/${props.RepoName}.git",
  ).some

ThisBuild / licenses := props.licenses

lazy val aiSkillsDocs = (project in file("."))
  .settings(
    name := prefixedProjectName(""),
    description := "A tool to manage AI agent skills",
  )
  .settings(noPublish)

lazy val docs = (project in file("docs-gen-tmp/docs"))
  .enablePlugins(MdocPlugin, DocusaurPlugin)
  .settings(
    name := "docs",
    mdocIn := file("docs/latest"),
    mdocOut := file("generated-docs/docs"),
    cleanFiles += ((ThisBuild / baseDirectory).value / "generated-docs" / "docs"),
    scalacOptions ~= (_.filter(opt => opt != "-Xfatal-warnings")),
    mdocVariables := {
      val logger = sLog.value
      val latestVersion = docsTools.getTheLatestTaggedVersion(logger.error(_))
      docsTools.createMdocVariables(latestVersion)
    },
    mdoc := {
      implicit val logger: Logger = sLog.value

      val latestVersion = docsTools.getTheLatestTaggedVersion(logger.error(_))

      val envVarCi = sys.env.get("CI")
      val ciResult = s"""sys.env.get("CI")=${envVarCi}"""
      envVarCi match {
        case Some("true") =>
          logger.info(
            s">> ${ciResult.yellow} so ${"run".green} `${"writeLatestVersion".blue}` and `${"writeVersionsArchived".blue}`."
          )
          val websiteDir = docusaurDir.value
          docsTools.writeLatestVersion(websiteDir, latestVersion)
          docsTools.writeVersionsArchived(websiteDir, latestVersion)
        case Some(_) | None =>
          logger.info(
            s">> ${ciResult.yellow} so it will ${"not run".red} `${"writeLatestVersion".cyan}` and `${"writeVersionsArchived".cyan}`."
          )
      }
      mdoc.evaluated
    },
    docusaurDir := (ThisBuild / baseDirectory).value / "website",
    docusaurBuildDir := docusaurDir.value / "build",
  )
  .settings(noPublish)

lazy val docsTools = new {

  lazy val CmdRun = new {
    import sys.process._

    def runAndCapture(command: Seq[String]): (Int, String, String) = {
      val out = new StringBuilder
      val err = new StringBuilder
      val exitCode =
        Process(command).!(
          ProcessLogger(
            (o: String) => out.append(o).append('\n'),
            (e: String) => err.append(e).append('\n'),
          )
        )
      (exitCode, out.result().trim, err.result().trim)
    }

    def fail(prefix: String,
             step: String,
             command: Seq[String],
             out: String,
             err: String)(log: String => Unit): Nothing = {
      val cmdString = command.mkString(" ")
      val details =
        if (err.nonEmpty) err
        else if (out.nonEmpty) out
        else "(no output)"
      log(s">> [$prefix][$step] Command failed: `$cmdString`\n$details".red)
      throw new MessageOnlyException(s"$step failed: $cmdString\n$details")
    }
  }

  def getTheLatestTaggedVersion(logger: => String => Unit): String = {
    val (ghVersionExit, ghVersionOut, ghVersionErr) =
      CmdRun.runAndCapture(Seq("gh", "--version"))
    if (ghVersionExit != 0)
      CmdRun.fail(
        "getTheLatestTaggedVersion",
        "gh --version",
        Seq("gh", "--version"),
        ghVersionOut,
        ghVersionErr,
      )(logger)

    val (ghAuthExit, ghAuthOut, ghAuthErr) =
      CmdRun.runAndCapture(Seq("gh", "auth", "status", "-h", "github.com"))
    if (ghAuthExit != 0)
      CmdRun.fail(
        "getTheLatestTaggedVersion",
        "gh auth status",
        Seq("gh", "auth", "status", "-h", "github.com"),
        ghAuthOut,
        ghAuthErr,
      )(logger)

    val repo = s"${props.GitHubUsername}/${props.CodeRepoName}"

    val tagNameCmd =
      Seq(
        "gh",
        "release",
        "view",
        "-R",
        repo,
        "--json",
        "tagName",
        "-q",
        ".tagName"
      )

    val (tagExit, tagOut, tagErr) = CmdRun.runAndCapture(tagNameCmd)
    if (tagExit != 0)
      CmdRun.fail(
        "getTheLatestTaggedVersion",
        "gh release view",
        tagNameCmd,
        tagOut,
        tagErr
      )(logger)

    val tagName = tagOut.trim
    if (tagName.isEmpty)
      CmdRun.fail(
        "getTheLatestTaggedVersion",
        "gh release view (empty tagName)",
        tagNameCmd,
        tagOut,
        tagErr,
      )(logger)

    if (!tagName.startsWith("v")) {
      logger(
        s">> [getTheLatestTaggedVersion] Expected tagName to start with 'v' but got: $tagName".red
      )
      throw new MessageOnlyException(
        s"Expected tagName to start with 'v' but got: $tagName"
      )
    }

    val versionWithoutV = tagName.stripPrefix("v")
    SemVer.parse(versionWithoutV) match {
      case Right(v) => v.render
      case Left(parseError) =>
        logger(
          s">> [getTheLatestTaggedVersion] Invalid SemVer from tagName ($tagName): ${parseError.toString}".red
        )
        throw new MessageOnlyException(
          s"Invalid SemVer from tagName ($tagName): ${parseError.toString}"
        )
    }
  }

  def writeLatestVersion(websiteDir: File, latestVersion: String)(
    implicit logger: Logger
  ): Unit = {
    val latestVersionFile = websiteDir / "latestVersion.json"
    val latestVersionJson = raw"""{"version":"$latestVersion"}"""

    val websiteDirRelativePath =
      s"${latestVersionFile.getParentFile.getParentFile.getName.cyan}/${latestVersionFile.getParentFile.getName.yellow}"
    logger.info(
      s""">> Writing ${"the latest version".blue} to $websiteDirRelativePath/${latestVersionFile.getName.green}.
         |>> Content: ${latestVersionJson.blue}
         |""".stripMargin
    )
    IO.write(latestVersionFile, latestVersionJson)
  }

  def writeVersionsArchived(websiteDir: File, latestVersion: String)(
    implicit logger: Logger
  ): Unit = {
    import sys.process._

    val (ghVersionExit, ghVersionOut, ghVersionErr) =
      CmdRun.runAndCapture(Seq("gh", "--version"))
    if (ghVersionExit != 0)
      CmdRun.fail(
        "writeVersionsArchived",
        "gh --version",
        Seq("gh", "--version"),
        ghVersionOut,
        ghVersionErr
      )(logger.error(_))

    val (ghAuthExit, ghAuthOut, ghAuthErr) =
      CmdRun.runAndCapture(Seq("gh", "auth", "status", "-h", "github.com"))
    if (ghAuthExit != 0)
      CmdRun.fail(
        "writeVersionsArchived",
        "gh auth status",
        Seq("gh", "auth", "status", "-h", "github.com"),
        ghAuthOut,
        ghAuthErr,
      )(logger.error(_))

    val repo = s"${props.GitHubUsername}/${props.CodeRepoName}"

    val ghTagsCmd =
      Seq(
        "gh",
        "api",
        "-H",
        "Accept: application/vnd.github+json",
        s"/repos/$repo/tags",
        "--paginate",
        "-q",
        ".[].name",
      )

    val (tagsExit, tagsOut, tagsErr) = CmdRun.runAndCapture(ghTagsCmd)
    if (tagsExit != 0)
      CmdRun.fail(
        "writeVersionsArchived",
        "gh api tags",
        ghTagsCmd,
        tagsOut,
        tagsErr
      )(logger.error(_))

    val tags = tagsOut.trim
    if (tags.isEmpty)
      CmdRun.fail(
        "writeVersionsArchived",
        "gh api tags (empty)",
        ghTagsCmd,
        tagsOut,
        tagsErr
      )(logger.error(_))

    val versions = tags
      .split("\n")
      .map(_.trim)
      .filter(t => t.nonEmpty && t.startsWith("v"))
      .map(_.stripPrefix("v"))
      .map(SemVer.parse)
      .collect { case Right(v) => v }
      .sorted(Ordering[SemVer].reverse)
      .map(_.render)
      .filter(_ != latestVersion)

    val versionsArchivedFile = websiteDir / "src" / "pages" / "versionsArchived.json"

    val versionsInJson = versions
      .map { v =>
        raw"""  {
             |    "name": "$v",
             |    "label": "$v"
             |  }""".stripMargin
      }
      .mkString("[\n", ",\n", "\n]")

    IO.write(versionsArchivedFile, versionsInJson)
  }

  def createMdocVariables(version: String): Map[String, String] = {
    val versionForDoc = version
    Map("VERSION" -> versionForDoc)
  }

}

lazy val props =
  new {

    private val GitHubRepo = findRepoOrgAndName

    val GitHubUsername = GitHubRepo.fold("kevin-lee")(_.orgToString)
    val RepoName = GitHubRepo.fold("ai-skills-docs")(_.nameToString)

    val CodeRepoName = RepoName.stripSuffix("-docs")

    val Scala3Versions = List("3.8.3")

    val ProjectScalaVersion = Scala3Versions.head

    lazy val licenses = List(License.MIT)

    val IncludeTest = "compile->compile;test->test"

  }

// scalafmt: off
def prefixedProjectName(name: String) =
  s"${props.RepoName}${if (name.isEmpty) "" else s"-$name"}"
// scalafmt: on
