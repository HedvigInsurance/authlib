@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.plugin.mpp.apple.XCFramework
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    kotlin("multiplatform") version "1.9.10"
    alias(libs.plugins.serialization)
    alias(libs.plugins.vanniktechGradleMavenPublish)
}

group = "com.hedvig.authlib"

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
        val jvmMain by getting {
            dependencies {
                api(libs.ktor.okhttp)
            }
        }
        val jvmTest by getting
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
                binaryOption("bundleId", "authlib")
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

mavenPublishing {
    val groupId = "com.hedvig.authlib"
    val version = buildString {
        val versionString = extra["AUTHLIB_VERSION_NAME"] as String
        append(versionString)
        if (versionString.contains("alpha").not()) return@buildString
        if (project.hasProperty("AUTHLIB_VERSION_ALPHA_TIMESTAMP").not()) return@buildString
        val alphaTimestamp = project.findProperty("AUTHLIB_VERSION_ALPHA_TIMESTAMP") as String
        if (alphaTimestamp.isBlank()) return@buildString
        append("-")
        append(alphaTimestamp)
    }
    coordinates(
        groupId = groupId,
        artifactId = "authlib",
        version = version,
    )
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

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinJvmCompile>().configureEach {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
}
