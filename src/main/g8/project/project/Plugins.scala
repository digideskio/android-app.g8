package project

import sbt._

object Plugins extends Build {
  lazy val root = Project("root", file(".")) dependsOn(androidPlugin)
  lazy val androidPlugin = RootProject(uri("git://github.com/jberkel/android-plugin.git"))
}

