# sbtLibby

Collection of often used libraries collected in a plugin for easy setup of projects

```
addSbtPlugin("scalavision" % "sbtlibby" % "0.1-SNAPSHOT")
```

In your `build.sbt` file just add the common libraries you want to use:
```scala

enablePlugins(SbtLibby)

// Your spesific libraries
libraryDependencies ++= Seq(
 // Some library definitions
 // Then add the library composititions from this plugins
) ++ utilDeps.value ++ metaDeps.value // etc.
```
