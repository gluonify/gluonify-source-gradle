// STANDALONE project: no Gluonify parent, nothing to install first. Rename with scripts/rename.py (see README, "Rename the project").
pluginManagement {
    val quarkusVersion: String by settings
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        id("io.quarkus") version quarkusVersion
    }
}

rootProject.name = "gluonify-source"
