import sbt._
import Keys._

object RootBuild extends Build {

  object dependencies {
    val akkaVersion = "2.3.11"
    val akkaStreamVersion = "1.0-RC4"

    val common = Seq(
      "junit" % "junit" % "4.12" % Test,
      "com.novocode" % "junit-interface" % "0.11" % Test
    )

    val driverApi = common

    val rediscala = common ++ Seq(
      "com.etaty.rediscala" %% "rediscala" % "1.4.2-redis-pipeline"
    )

    val streams = common ++ Seq(
      "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
      "com.typesafe.akka" %% "akka-stream-experimental" % akkaStreamVersion,
      "com.typesafe.akka" %% "akka-stream-testkit-experimental" % akkaStreamVersion % Test,
      "org.reactivestreams" % "reactive-streams-tck" % "1.0.0" % Test,
      "org.scalatest" %% "scalatest" % "2.2.5" % Test
    )
  }

  override val settings = super.settings ++ Seq(
    organization := "net.cakesolutions",
    version := "0.1-SNAPSHOT",
    scalaVersion := "2.11.7"
  )

  def defaultSettings = Seq(
    resolvers += "Spring remote" at "http://repo.spring.io/libs-release-remote",
    javacOptions in (Compile, compile) ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint", "-Werror"),
    javacOptions in (Compile, doc)  ++= Seq("-source", "1.8"),
    publishArtifact in Test := true
  )

  def moduleId(name: String) = "reactive-redis-" + name

  def module(name: String) = Project(id = moduleId(name), base = file(name), settings = defaultSettings)

  def driverModule(name: String) = module("driver-" + name)

  lazy val driverApi = driverModule("api") settings (
    libraryDependencies ++= dependencies.driverApi
  )

  lazy val rediscala = driverModule("rediscala").dependsOn(driverApi % "test->test;compile->compile") settings (
    libraryDependencies ++= dependencies.rediscala
  )

  lazy val streams = module("streams").dependsOn(driverApi) settings (
    libraryDependencies ++= dependencies.streams
  )

  lazy val tests = module("tests").dependsOn(driverApi, rediscala, streams) settings (
    libraryDependencies ++= dependencies.rediscala ++ dependencies.streams
  )

  lazy val root = project.in(file("."))
    .aggregate(driverApi, rediscala, streams, tests)

}