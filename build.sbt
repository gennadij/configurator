lazy val server = (project in file("server")).settings(commonSettings).settings(
  scalaJSProjects := Seq(client),
  pipelineStages in Assets := Seq(scalaJSPipeline),
  pipelineStages := Seq(digest, gzip),
  // triggers scalaJSPipeline when using compile or continuous compilation
  compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
  libraryDependencies ++= Seq(
    "com.vmunier" %% "scalajs-scripts" % "1.1.2",
    guice,
    specs2 % Test,

	"org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test,

	"org.specs2" % "specs2-junit_2.12" % "3.8.6" % "test",

	"com.orientechnologies" % "orientdb-graphdb" % "3.0.1"
  ),
  // Compile the project before generating Eclipse files, so that generated .scala or .class files for views and routes are present
  EclipseKeys.preTasks := Seq(compile in Compile),

  //PlayKeys.devSettings += "play.server.http.port" -> "8080",
  PlayKeys.devSettings += "play.server.http.idleTimeout" -> "180s",
  PlayKeys.devSettings += "akka.http.server.websocket.periodic-keep-alive-max-idle" -> "10s"

).enablePlugins(PlayScala).
  dependsOn(sharedJvm)

lazy val client = (project in file("client")).settings(commonSettings).settings(
  scalaJSUseMainModuleInitializer := true,
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.9.5",
	  "be.doeraene" %%% "scalajs-jquery" % "0.9.1",
	  "com.typesafe.play" %%% "play-json" % "2.6.9"
  ),
  skip in packageJSDependencies := false,
  jsDependencies ++= Seq(
    "org.webjars" % "jquery" % "2.1.4" / "2.1.4/jquery.js",
	"org.webjars" % "jquery-ui" % "1.11.4" / "jquery-ui.js" dependsOn "jquery.js"
  )
).enablePlugins(ScalaJSPlugin, ScalaJSWeb).
  dependsOn(sharedJs)

lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared")).settings(commonSettings).settings(
  libraryDependencies += "com.typesafe.play" %% "play-json" % "2.6.9"
)
lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js


lazy val commonSettings = Seq(
  scalaVersion := "2.12.6",
  organization := "org.genericConfig"
)

// loads the server project at sbt startup
onLoad in Global := (onLoad in Global).value andThen {s: State => "project server" :: s}
