lazy val root = (project in file(".")).
	settings(
		name := "configurator",
		version := "1.0",
		scalaVersion := "2.11.7"
	)
	
	libraryDependencies += "org.json4s" %% "json4s-jackson" % "3.2.11"

