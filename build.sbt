
val json4s = "org.json4s" %% "json4s-jackson" % "3.2.11"
val xml = "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.6"
val specs2 = "org.specs2" % "specs2-core_2.11" % "3.8.6" % "test"
val mockito = "org.mockito" % "mockito-all" % "1.9.5" % "test"
val junit = "junit" % "junit" % "4.8.1" % "test"
val specs2Junit = "org.specs2" %% "specs2-junit" % "3.8.2" % "test"
val orientdbServer = "com.orientechnologies" % "orientdb-server" % "2.2.+"
val orientdbEnterprise = "com.orientechnologies" % "orientdb-enterprise" % "2.1.+"
val orientdbGraph = "com.orientechnologies" % "orientdb-graphdb" % "2.2.+"
val orientdbTools = "com.orientechnologies" % "orientdb-tools" % "2.2.+"
val orientdbCore = "com.orientechnologies" % "orientdb-core" % "2.2.+"
val orientdbClient = "com.orientechnologies" % "orientdb-client" % "2.2.+"
val orientdbObject = "com.orientechnologies" % "orientdb-object" % "2.2.3"
val bluprintsCore = "com.tinkerpop.blueprints" % "blueprints-core" % "2.6.+"
val jsonFromPLay = "com.typesafe.play" % "play-json_2.10" % "2.4.8"



lazy val commonSettings = Seq(
	organization := "org.generic_configurator", 	
	version := "0.1.0",
	scalaVersion := "2.11.+"
)

lazy val root = (project in file(".")).
	settings(commonSettings: _*).
	settings(
		name := "configurator",
		libraryDependencies ++= Seq(
		xml, 
		specs2,
		orientdbCore,
		bluprintsCore,
		orientdbGraph,
		orientdbEnterprise,
		orientdbClient,
		jsonFromPLay
		),
		scalacOptions in Test ++= Seq("-Yrangepos")
	)
	
