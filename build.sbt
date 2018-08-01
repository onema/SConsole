organization := "io.onema"

name := "SConsole"

version := "0.0.1"

scalaVersion := "2.12.6"

libraryDependencies ++= {
  Seq(

    // Json De/Serializer
    "org.rogach"                % "scallop_2.12"              % "3.1.3",

    // Testing
    "org.scalatest"             %% "scalatest"                % "3.0.0"       % "test"
  )
}

publishMavenStyle := true
publishTo := Some("Onema Snapshots" at "s3://s3-us-east-1.amazonaws.com/ones-deployment-bucket/snapshots")
