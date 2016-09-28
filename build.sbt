name := "Ahoy Akka"
version := "1.0"
  
scalaVersion := "2.11.8"
   
libraryDependencies ++= Seq(
  "com.typesafe" % "config" % "1.3.0",
  "com.typesafe.akka" %% "akka-actor" % "2.4.10",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.10",
  "com.typesafe.akka" %% "akka-http-spray-json-experimental" % "2.4.10"
)

enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)

