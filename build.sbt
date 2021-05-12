
name := "scala-automation-samples"

version := "0.1"

scalaVersion := "2.13.5"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.3.0-SNAP2" % Test
libraryDependencies += "com.github.daddykotex" %% "courier" % "3.0.0-M2a"
libraryDependencies += "com.typesafe" % "config" % "1.4.1"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.3"
libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % "2.6.9"
libraryDependencies += "com.typesafe.akka" %% "akka-slf4j" % "2.6.9"
libraryDependencies += "org.scalatestplus" %% "selenium-3-141" % "3.2.7.0" % "test"
libraryDependencies += "org.scalatest" %% "scalatest-flatspec" % "3.2.7" % "test"
libraryDependencies += "org.scalatest" %% "scalatest-shouldmatchers" % "3.2.7" % "test"
libraryDependencies += "com.google.guava" % "guava" % "30.1.1-jre"


enablePlugins(AkkaGrpcPlugin)