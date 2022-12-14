val scala3Version = "3.1.3"

lazy val root = project
  .in(file("."))
  .settings(
    name := "mazes",
    version := "1.0.0",

    scalaVersion := scala3Version,
    
    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test
  )

  // Add dependency on ScalaFX library
libraryDependencies += "org.scalafx" %% "scalafx" % "16.0.0-R25"

// Main file for sbt assembly
assembly / mainClass := Some("view.DrawMaze")

// sbt assembly merge strategy
assemblyMergeStrategy in assembly := {
      case "module-info.class" => MergeStrategy.discard
      case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    }

// Determine OS version of JavaFX binaries
lazy val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux")   => "linux"
  case n if n.startsWith("Mac")     => "mac"
  case n if n.startsWith("Windows") => "win"
  case _ => throw new Exception("Unknown platform!")
}


// Add dependency on JavaFX libraries, OS dependent
lazy val javaFXModules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
libraryDependencies ++= javaFXModules.map(m =>
  "org.openjfx" % s"javafx-$m" % "16" classifier osName
)
