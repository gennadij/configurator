
name := """configOnPLay"""

organization := "org.genericConfig"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.3"

libraryDependencies += guice

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test

libraryDependencies += "org.specs2" % "specs2-junit_2.12" % "3.8.6" % "test"

libraryDependencies += "com.tinkerpop.blueprints" % "blueprints-core" % "2.6.0"
libraryDependencies += "com.orientechnologies" % "orientdb-graphdb" % "2.2.12"
libraryDependencies += "com.orientechnologies" % "orientdb-client" % "2.2.12"
libraryDependencies += "com.orientechnologies" % "orientdb-enterprise" % "2.1.24"
libraryDependencies += "com.orientechnologies" % "orientdb-core" % "2.2.12"

//dependencyOverrides += "com.google.guava" % "guava" % "22.0"
dependencyOverrides += "com.typesafe.akka" % "akka-stream_2.12" % "2.5.4"
dependencyOverrides += "com.typesafe.akka" % "akka-actor_2.12" % "2.5.4"
//dependencyOverrides += "net.java.dev.jna" % "jna-platform" % "4.0.0"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "org.genericConfig.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "org.genericConfig.binders._"
