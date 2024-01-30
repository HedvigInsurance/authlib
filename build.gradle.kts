import co.touchlab.faktory.versionmanager.ManualVersionManager

plugins {
    kotlin("multiplatform") version libs.versions.kotlin.get()
    alias(libs.plugins.serialization)
    alias(libs.plugins.kmmBridge)
    alias(libs.plugins.vanniktechGradleMavenPublish)
}

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { target: org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget ->
        target.binaries.framework {
            baseName = "authlib"
            isStatic = true
            binaryOption("bundleId", "authlib")
        }
    }
    jvm {
        compilations.configureEach {
            kotlinOptions.jvmTarget = "1.8"
        }
    }
    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.ktor.core)
            implementation(libs.ktor.json)
            implementation(libs.kotlinx.serializationJson)
            implementation(libs.ktor.client.contentNegotiation)
            implementation(libs.ktor.client.logging)
        }
        jvmMain.dependencies {
            api(libs.ktor.okhttp)
        }
        iosMain.dependencies {
            implementation(libs.ktor.darwin)
        }
    }
}


val authlibGroupId = extra["AUTHLIB_GROUP"] as String
val authlibVersion = buildString {
    val versionString = extra["AUTHLIB_VERSION_NAME"] as String
    append(versionString)
    if (versionString.contains("alpha").not()) return@buildString
    if (project.hasProperty("AUTHLIB_VERSION_ALPHA_TIMESTAMP").not()) return@buildString
    val alphaTimestamp = project.findProperty("AUTHLIB_VERSION_ALPHA_TIMESTAMP") as String
    if (alphaTimestamp.isBlank()) return@buildString
    append("-")
    append(alphaTimestamp)
}
version = authlibVersion
group = authlibGroupId

@Suppress("UnstableApiUsage")
mavenPublishing {
    coordinates(
        groupId = authlibGroupId,
        artifactId = "authlib",
        version = authlibVersion,
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

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
}

kmmbridge {
    frameworkName.set("authlib")
    spm(project.rootDir.absolutePath)
    mavenPublishArtifacts()
    versionManager.set( ManualVersionManager)
}
