
val specs2 = "org.specs2" %% "specs2-core" % "3.7.2" % "test"
val mockito = "org.mockito" % "mockito-all" % "1.9.5" % "test"
val junit = "junit" % "junit" % "4.8.1" % "test"
val specs2Junit = "org.specs2" %% "specs2-junit" % "3.8.2" % "test"
val orientdbServer = "com.orientechnologies" % "orientdb-server" % "2.1.+"



//==============================================================================

// https://mvnrepository.com/artifact/org.scala-lang.modules/scala-xml_2.11
val xml = "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.6"

// https://mvnrepository.com/artifact/com.orientechnologies/orientdb-core
val orientdbCore = "com.orientechnologies" % "orientdb-core" % "2.2.12"

// https://mvnrepository.com/artifact/com.tinkerpop.blueprints/blueprints-core
val bluprintsCore = "com.tinkerpop.blueprints" % "blueprints-core" % "2.6.0"

// https://mvnrepository.com/artifact/com.orientechnologies/orientdb-graphdb
val orientdbGraph = "com.orientechnologies" % "orientdb-graphdb" % "2.2.12"

// https://mvnrepository.com/artifact/com.orientechnologies/orientdb-enterprise
val orientdbEnterprise = "com.orientechnologies" % "orientdb-enterprise" % "2.1.24"

// https://mvnrepository.com/artifact/com.orientechnologies/orientdb-client
val orientdbClient = "com.orientechnologies" % "orientdb-client" % "2.2.12"

// https://mvnrepository.com/artifact/com.orientechnologies/orientdb-tools
val orientdbTools = "com.orientechnologies" % "orientdb-tools" % "2.2.12"

// https://mvnrepository.com/artifact/com.typesafe.play/play-json_2.11
val jsonFromPLay = "com.typesafe.play" % "play-json_2.11" % "2.5.9"


lazy val commonSettings = Seq(
	organization := "org.generic_configurator", 	
	version := "0.1.0",
	scalaVersion := "2.11.8"
)

lazy val root = (project in file(".")).
	settings(commonSettings: _*).
	settings(
		name := "configurator",
		libraryDependencies ++= Seq(
		  xml
      ,orientdbCore
			,bluprintsCore
		  ,orientdbGraph
			,orientdbEnterprise
		  ,orientdbClient
//		  ,jsonFromPLay
		)
	)
	
