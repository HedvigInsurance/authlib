@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("multiplatform") version "1.7.20"
    alias(libs.plugins.serialization)
    alias(libs.plugins.vanniktechGradleMavenPublish)
}

group = "com.hedvig.authlib"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js(BOTH) {
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }

    val xcf = XCFramework("authlib")

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.ktor.core)
                implementation(libs.ktor.json)
                implementation(libs.kotlinx.serializationJson)
                implementation(libs.ktor.client.contentNegotiation)
                implementation(libs.ktor.client.logging)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting
        val jvmTest by getting
        val jsMain by getting
        val jsTest by getting
        val iosMain by creating {
            dependencies {
                implementation(libs.ktor.darwin)
            }
        }
        val iosTest by creating

        listOf(
            iosX64(),
            iosArm64(),
            iosSimulatorArm64()
        ).forEach { target: org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget ->
            target.binaries.framework {
                baseName = "authlib"
                xcf.add(this)
            }
            getByName("${target.targetName}Main") {
                dependsOn(iosMain)
            }
            getByName("${target.targetName}Test") {
                dependsOn(iosTest)
            }
        }
    }
}

group = "com.hedvig.authlib"

mavenPublishing {
    pom {
        name.set("authlib")
        description.set("OAuth Authentication library for Hedvig applications")
        inceptionYear.set("2022")
        url.set("https://github.com/HedvigInsurance/authlib/")
        licenses {
            license {
                name.set("GNU AFFERO GENERAL PUBLIC LICENSE")
                url.set("https://www.gnu.org/licenses/agpl-3.0.en.html")
                distribution.set("https://www.gnu.org/licenses/agpl-3.0.en.html")
            }
        }
        developers {
            developer {
                id.set("hedviginsurance")
                name.set("HedvigInsurance")
                url.set("https://github.com/HedvigInsurance/")
            }
        }
        scm {
            url.set("https://github.com/HedvigInsurance/authlib/")
            connection.set("scm:git:git://github.com/HedvigInsurance/authlib.git")
            developerConnection.set("scm:git:ssh://git@github.com/HedvigInsurance/authlib.git")
        }
    }
}

publishing {
    repositories {
        maven {
            setUrl(extra["GITHUB_MAVEN_URL"] as String)
            credentials {
                username = project.findProperty("HEDVIG_GITHUB_PACKAGES_USER") as? String ?: ""
                password = project.findProperty("HEDVIG_GITHUB_PACKAGES_TOKEN") as? String ?: ""
            }
        }
    }
}