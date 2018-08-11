import sbt.url

organization := "io.onema"

name := "SConsole"

version := "0.0.3"

scalaVersion := "2.12.6"

libraryDependencies ++= {
  Seq(

    // Json De/Serializer
    "org.rogach"                % "scallop_2.12"              % "3.1.3",

    // Logging
    "com.typesafe.scala-logging"  %% "scala-logging"                      % "3.7.2",
    "ch.qos.logback"              % "logback-classic"                     % "1.1.7",

    // Testing
    "org.scalatest"             %% "scalatest"                % "3.0.0"       % "test"
  )
}

// Maven Central Repo boilerplate configuration
pomIncludeRepository := { _ => false }
licenses := Seq("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0"))
homepage := Some(url("https://github.com/onema/SConsole"))
scmInfo := Some(
  ScmInfo(
    url("https://github.com/onema/SConsole"),
    "scm:git@github.com:onema/SConsole.git"
  )
)
developers := List(
  Developer(
    id    = "onema",
    name  = "Juan Manuel Torres",
    email = "software@onema.io",
    url   = url("https://github.com/onema/")
  )
)
publishMavenStyle := true
publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}
publishArtifact in Test := false
