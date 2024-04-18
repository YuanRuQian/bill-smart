import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import com.diffplug.gradle.spotless.SpotlessExtension

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
  alias(libs.plugins.androidApplication) apply false
  alias(libs.plugins.jetbrainsKotlinAndroid) apply false
  alias(libs.plugins.spotless).apply(false)
  id("org.jmailen.kotlinter") version "4.3.0" apply false
  id("com.google.gms.google-services") version "4.4.1" apply false
  kotlin("kapt") version "1.9.23"
  id("com.google.dagger.hilt.android") version "2.51.1" apply false
}

buildscript {
  repositories {
    // other repositories...
    mavenCentral()
  }
  dependencies {
    // other plugins...
    classpath(libs.hilt.android.gradle.plugin)
  }
}


subprojects {
  plugins.matching { anyPlugin -> anyPlugin is AppPlugin || anyPlugin is LibraryPlugin }
    .whenPluginAdded {
      apply(plugin = libs.plugins.spotless.get().pluginId)
      extensions.configure<SpotlessExtension> {
        kotlin {
          target("**/*.kt")
          targetExclude("${layout.buildDirectory}/**/*.kt")
          ktlint()
            .setEditorConfigPath("${project.rootDir}/.editorconfig")
        }
      }
    }
}
