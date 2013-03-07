// -*- scala -*-
import AssemblyKeys._

name := "log-mongo"

organization := "net.mtgto"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.10.0"

resolvers += "mtgto repos" at "http://scala-irc-bot.github.com/scala-irc-bot/maven/"

libraryDependencies := Seq(
  "net.mtgto" %% "scala-irc-bot" % "0.2.0" % "provided",
  "org.mongodb" %% "casbah" % "2.5.0",
  "junit" % "junit" % "4.10" % "test",
  "org.specs2" %% "specs2" % "1.14" % "test",
  "org.mockito" % "mockito-all" % "1.9.0" % "test"
)

scalacOptions ++= Seq("-deprecation", "-unchecked", "-Xlint", "-feature", "-encoding", "UTF8")

assemblySettings

assembleArtifact in packageScala := false

ScctPlugin.instrumentSettings

testOptions in ScctTest += Tests.Argument(TestFrameworks.Specs2, "console", "junitxml")

org.scalastyle.sbt.ScalastylePlugin.Settings
