import sbt._

import Keys._
import AndroidKeys._

object General {
  val settings = Defaults.defaultSettings ++ Seq (
    name := "$name$",
    version := "0.1",
    versionCode := 0,
    scalaVersion := "$scala_version$",
    platformName in Android := "android-$api_level$"
    //fix sbt settings for eclipse layout
    manifestPath in Android <<= (baseDirectory, manifestName in Android) map ((s, m) => Seq(s / m)),
    mainAssetsPath in Android <<= (baseDirectory, assetsDirectoryName in Android)(_ / _),
    mainResPath in Android <<= (baseDirectory, resDirectoryName in Android)(_ / _),
    managedJavaPath <<= (baseDirectory)(_ / "gen"),
    managedJavaPath in Android <<= (baseDirectory)(_ / "gen"),
    nativeLibrariesPath in Android <<= (sourceDirectory)(_ / "libs"),
    resourcesApkName in Android := "resources.ap_",
    resourcesApkPath in Android <<= (crossTarget, resourcesApkName in Android)(_ / _),
    classesDexPath in Android <<= (crossTarget, classesDexName in Android)(_ / _),
    packageApkName in Android <<= (name)((a) => "%s.apk".format(a)),
    packageApkPath in Android <<= (crossTarget, packageApkName in Android)(_ / _),
    crossTarget <<= (baseDirectory)(_ / "bin")
  )

  val proguardSettings = Seq (
    useProguard in Android := $useProguard$
  )

  lazy val fullAndroidSettings =
    General.settings ++
    AndroidProject.androidSettings ++
    TypedResources.settings ++
    proguardSettings ++
    AndroidManifestGenerator.settings ++
    AndroidMarketPublish.settings ++ Seq (
      keyalias in Android := "change-me",
      libraryDependencies += "org.scalatest" %% "scalatest" % "$scalatest_version$" % "test"
    )
}

object AndroidBuild extends Build {
  lazy val main = Project (
    "$name$",
    file("."),
    settings = General.fullAndroidSettings
  )

  lazy val tests = Project (
    "tests",
    file("tests"),
    settings = General.settings ++
               AndroidTest.androidSettings ++
               General.proguardSettings ++ Seq (
      name := "$name$Tests"
    )
  ) dependsOn main
}
