// -*- scala -*-
import AssemblyKeys._

name := "log-mongo"

organization := "net.mtgto"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.9.2"

resolvers += "mtgto repos" at "http://scala-irc-bot.github.com/scala-irc-bot/maven/"

libraryDependencies := Seq(
  "net.mtgto" %% "scala-irc-bot" % "0.1.0-SNAPSHOT" % "provided",
  "org.mongodb" % "casbah_2.9.1" % "3.0.0-M2",
  "org.specs2" %% "specs2" % "1.12.2" % "test",
  "org.mockito" % "mockito-all" % "1.9.0" % "test"
)

scalacOptions ++= Seq("-deprecation", "-unchecked", "-encoding", "UTF8")

assemblySettings

assembleArtifact in packageScala := false
