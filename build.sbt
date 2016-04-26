val json4s = "org.json4s" %% "json4s-jackson" % "3.2.11"
val xml = "org.scala-lang.modules" %% "scala-xml" % "1.0.2"
val specs2 = "org.specs2" %% "specs2-core" % "3.7.2" % "test"
val mockito = "org.mockito" % "mockito-all" % "1.9.5" % "test"

lazy val commonSettings = Seq(
	organization := "org.generic_configurator",
	version := "0.1.0",
	scalaVersion := "2.11.8"
)

lazy val root = (project in file(".")).
	settings(commonSettings: _*).
	settings(
		name := "configurator",
		libraryDependencies ++= Seq(json4s, xml, specs2, mockito),
			scalacOptions in Test ++= Seq("-Yrangepos")
	)
