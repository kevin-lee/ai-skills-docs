logLevel := sbt.Level.Warn

addDependencyTreePlugin

addSbtPlugin("org.scalameta"   % "sbt-mdoc"        % "2.8.2")
addSbtPlugin("io.kevinlee"     % "sbt-docusaur"    % "0.21.0")

val sbtDevOopsVersion = "3.5.1"
addSbtPlugin("io.kevinlee" % "sbt-devoops-scala"     % sbtDevOopsVersion)
addSbtPlugin("io.kevinlee" % "sbt-devoops-sbt-extra" % sbtDevOopsVersion)
addSbtPlugin("io.kevinlee" % "sbt-devoops-github"    % sbtDevOopsVersion)
addSbtPlugin("io.kevinlee" % "sbt-devoops-starter"   % sbtDevOopsVersion)
