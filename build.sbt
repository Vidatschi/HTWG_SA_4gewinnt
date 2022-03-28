name := "Connect Four in scala"

version := "0.1"

scalaVersion := "2.13.7"

libraryDependencies ++=Seq(
  "org.scalactic" %% "scalactic" % "3.2.11",
  "org.scalatest" %% "scalatest" % "3.2.11" % "test",
  "org.scala-lang.modules" %% "scala-swing" % "3.0.0",
  "com.google.inject" % "guice" % "5.0.1",
  "net.codingwell" %% "scala-guice" % "5.0.2",
  "org.scala-lang.modules" %% "scala-xml" % "2.0.1",
  "com.typesafe.play" %% "play-json" % "2.10.0-RC6"
)