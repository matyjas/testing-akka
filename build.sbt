
name := "Future Testing"

version := "1.0"

scalaVersion := "2.9.1"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "com.typesafe.akka" % "akka-actor" % "2.0-RC2"

libraryDependencies += "com.typesafe.akka" % "akka-testkit" % "2.0-RC2"

libraryDependencies += "net.databinder" %% "dispatch-http" % "0.8.7"

libraryDependencies += "net.databinder" %% "dispatch-lift-json" % "0.8.5"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.7.1" % "test"
