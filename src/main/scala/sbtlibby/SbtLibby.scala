import sbt._
import Keys._
import sbt.plugins.JvmPlugin

object SbtLibby extends AutoPlugin {
  import autoImport._

  object UtilSettings {
    // LogBack
    lazy val logBackClassicVersion = "1.2.3"
    val logBack = "ch.qos.logback" % "logback-classic" % logBackClassicVersion
    //ScalaTime from JodaTime
    lazy val nScalaTimeVersion = "2.16.0"
    lazy val nScalaTime = "com.github.nscala-time" %% "nscala-time" % nScalaTimeVersion
    lazy val pprintVersion = "0.5.3"
    lazy val pprint = "com.lihaoyi" %% "pprint" % pprintVersion
    lazy val enumeratumVersion = "1.5.12"
    lazy val enumeratum = "com.beachape" %% "enumeratum" % enumeratumVersion
    lazy val dependencies: Seq[ModuleID] = Seq(logBack, nScalaTime, pprint, enumeratum)
  }

  object ScalaMeta {
    lazy val scalaMetaVersion = "1.8.0"
    lazy val scalaMeta = "org.scalameta" %% "scalameta" % scalaMetaVersion 
    lazy val scalaMetaProvided = "org.scalameta" %% "scalameta" % scalaMetaVersion % Provided 
    lazy val metaParadiseVersion = "3.0.0-M10"
    lazy val paradiseCompilerPlugin = "org.scalameta" % "paradise" % metaParadiseVersion cross CrossVersion.full
    lazy val dependencies: Seq[ModuleID] = Seq(scalaMeta)       
  }

  object MetaProgramming {
    lazy val sourceCodeVersion = "0.1.3"
    lazy val sourceCode: ModuleID = "com.lihaoyi" %% "sourcecode" % sourceCodeVersion
    lazy val scalaFmtVersion = "1.0.0-RC2"
    lazy val scalaFmt: ModuleID = "com.geirsson" %% "scalafmt-core" % scalaFmtVersion
    lazy val fastParseVersion = "0.4.2" 
    lazy val fastParse: ModuleID = "com.lihaoyi" %% "fastparse" % fastParseVersion
    lazy val guavaVersion = "22.0"
    lazy val guava: ModuleID = "com.google.guava" % "guava" % guavaVersion
    lazy val apacheLangCommonsVersion = "3.5"
    lazy val apacheLangCommons: ModuleID = "org.apache.commons" % "commons-lang3" % apacheLangCommonsVersion
    val dependencies: Seq[ModuleID] = Seq(sourceCode, scalaFmt, fastParse, guava, apacheLangCommons)
  }

  object IODefault {
    lazy val betterFilesVersion = "3.0.0"
    lazy val betterFiles = "com.github.pathikrit" %% "better-files" % betterFilesVersion
    val dependencies: Seq[ModuleID] = Seq(betterFiles)
  }

  object TypeLevel {
    lazy val scalacticVersion =  "3.0.1"
    lazy val scalactic = "org.scalactic" %% "scalactic" % scalacticVersion
    lazy val shapelessVersion = "2.3.2"
    lazy val shapeless = "com.chuusai" %% "shapeless" % shapelessVersion
    lazy val catsVersion = "1.0.0-MF"
    lazy val cats: ModuleID = "org.typelevel" %% "cats-core" % catsVersion
    lazy val fs2CoreVersion = "0.10.0-M8"
    lazy val fs2Core: ModuleID = "co.fs2" %% "fs2-core" % fs2CoreVersion
    lazy val fs2IOVersion = "0.10.0-M8"
    lazy val fs2IO: ModuleID = "co.fs2" %% "fs2-io" % fs2IOVersion
    lazy val fs2Http: ModuleID = "com.spinoco" %% "fs2-http" % "0.2.0-SNAPSHOT"
    val dependencies: Seq[ModuleID] = Seq(scalactic, shapeless, cats, fs2Core, fs2IO, fs2Http)
  }

  object TestSettings {
    // Specs
    lazy val specs2Version = "3.9.0"
    lazy val specs2 : Seq[ModuleID] = Seq("org.specs2" %% "specs2-core" % specs2Version % "test")
    lazy val scalaCheckVersion =  "test"
    lazy val scalaCheck : Seq[ModuleID] = Seq("org.scalacheck" %% "scalacheck" % "1.13.5" % scalaCheckVersion)
    lazy val scalaTestVersion = "3.0.3" 
    lazy val scalaTest: Seq[ModuleID] = Seq("org.scalatest" %% "scalatest" % "3.0.1" % "test")
    val dependencies: Seq[ModuleID] = specs2 ++ scalaCheck ++ scalaTest
  }

  object DatabaseSettings {
    // Postgres
    lazy val postgresVersion = "9.4.1212"
    lazy val postgres: Seq[ModuleID] = Seq("org.postgresql" % "postgresql" % postgresVersion)

    // H2 Database
    lazy val h2Version = "1.4.195"
    lazy val h2 : Seq[ModuleID] = Seq("com.h2database" % "h2" % h2Version)

    // Quill
    lazy val quillVersion = "1.2.1"
    lazy val quill: Seq[ModuleID] = Seq("io.getquill" %% "quill-jdbc" % quillVersion)

    lazy val prodDb = postgres ++ quill
  }

  object LiftWebSettings {
    import UtilSettings._
    // Lift
    lazy val liftVersion = "3.0.1"
    lazy val liftExtras = "1.0.1"
    lazy val lift : Seq[ModuleID] = Seq(
      "net.liftmodules"   %% "lift-jquery-module_3.0"                  % "2.10",
      "net.liftweb"       %% "lift-webkit"        % liftVersion        % "compile",
      "net.liftweb"       %% "lift-mapper"        % liftVersion        % "compile",
      "net.liftmodules"   %% "extras_3.0"         % liftExtras
    )
    // Jetty
    lazy val jettyVersion = "9.4.6.v20170531"
    val jetty: Seq[ModuleID]  = Seq(
      "org.eclipse.jetty" % "jetty-webapp"        % jettyVersion % "container,test",
      "org.eclipse.jetty" % "jetty-plus"          % jettyVersion % "container,test" // For Jetty Config
    )
    // Servlet
    lazy val servletVersion = "3.0.0.v201112011016" 
    lazy val servlet: Seq[ModuleID]  = Seq(
      "org.eclipse.jetty.orbit" % "javax.servlet" % servletVersion % "container,test" artifacts Artifact("javax.servlet", "jar", "jar")
    )
   
    lazy val dependencies: Seq[ModuleID] = 
     (logBack +: nScalaTime +: lift) ++ jetty ++ servlet ++ TestSettings.dependencies ++ DatabaseSettings.prodDb
  }

  override lazy val projectSettings = Seq(
    liftWebDeps := LiftWebSettings.dependencies,
    testDeps := TestSettings.dependencies,
    typeLevelDeps := TypeLevel.dependencies,
    metaDeps := MetaProgramming.dependencies,
    scalaMetaDeps := ScalaMeta.dependencies,
    macroParadisePluginDep := ScalaMeta.paradiseCompilerPlugin,
    utilDeps := UtilSettings.dependencies,
    fileDeps := IODefault.dependencies,
    defaultCompileOptions := Seq(
      "-deprecation",
      "-encoding",
      "UTF-8",
      "-feature",
      "-language:existentials",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-unchecked",
      "-Yno-adapted-args",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen",
      "-Xfuture",
      "-Xlint"
    )
  )

  override def trigger = allRequirements
  override def requires = JvmPlugin

  object autoImport {
    val liftWebDeps = settingKey[Seq[ModuleID]]("Basic dependency stack for a Lift Web project")
    val testDeps = settingKey[Seq[ModuleID]]("Basic dependency stack for JVM Testing")
    val typeLevelDeps = settingKey[Seq[ModuleID]]("Basics needed to do typelevel programming")
    val metaDeps = settingKey[Seq[ModuleID]]("Basics needed to do meta programming - without scalameta itself")
    val scalaMetaDeps = settingKey[Seq[ModuleID]]("ScalaMeta Dependency")
    val macroParadisePluginDep = settingKey[ModuleID]("Macro Paradise meta plugin")
    val utilDeps = settingKey[Seq[ModuleID]]("Util Dependencies like logging, jodatime for scala, pprint etc")
    val fileDeps = settingKey[Seq[ModuleID]]("Filehandler, better-files are used for now")
    val defaultCompileOptions = settingKey[Seq[String]]("standard compiler Options")
  }

}
