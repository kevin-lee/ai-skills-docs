logLevel := sbt.Level.Warn

addDependencyTreePlugin

addSbtPlugin("org.scalameta"   % "sbt-mdoc"        % "2.9.2")
addSbtPlugin("io.kevinlee"     % "sbt-docusaur"    % "0.23.0")

val sbtDevOopsVersion = "3.9.0"
addSbtPlugin("io.kevinlee" % "sbt-devoops-scala"     % sbtDevOopsVersion)
addSbtPlugin("io.kevinlee" % "sbt-devoops-sbt-extra" % sbtDevOopsVersion)
addSbtPlugin("io.kevinlee" % "sbt-devoops-github"    % sbtDevOopsVersion)
addSbtPlugin("io.kevinlee" % "sbt-devoops-starter"   % sbtDevOopsVersion)
