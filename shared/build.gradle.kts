import org.gradle.kotlin.dsl.commonMain
import org.gradle.kotlin.dsl.commonTest
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    id("maven-publish")
    kotlin("plugin.serialization") version "1.9.0"
//    id("com.squareup.sqldelight")
}

kotlin {
    androidTarget {
        version = "1.0"
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_17)
                }
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }


    cocoapods {
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        version = "1.0"
        ios.deploymentTarget = "16.0"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
        }
//        pod("AppsFlyerFramework"){
//            version = libs.versions.pod.appsflyer.get()
//            framework {
//                baseName = "AppsFlyerFramework"
//                isStatic = true
//            }
//        }
    }
    
    sourceSets {
        iosMain.dependencies {
            implementation(libs.sqlite.bundled)
            implementation(libs.ktor.client.darwin)
            implementation(libs.runtime)
//            implementation(libs.native.driver)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
            implementation(libs.androidx.work.runtime.ktx)
//            implementation(libs.native.driver)
        }

        commonMain.dependencies {
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.ktor.serialization.kotlinx.json)
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)
            implementation(libs.ktor.client.core)
            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.runtime)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

publishing {
    publications {
        // Use 'create' with a specific name for your KMP publication
        withType<MavenPublication>().matching{ it.name == "kotlinMultiplatform" }.configureEach { // <-- Use create method and specify type
            groupId = "com.eloelo.events.analytics" // Your Group ID
            artifactId = "greenhorn-eloelo-event" // Your Artifact ID
            version = "1.0" // Your Version
        }
    }
    repositories {
        mavenLocal()
        mavenCentral()
    }
}

android {
    namespace = "com.eloelo.events.analytics"
    compileSdk = 35
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(libs.af.android.sdk)
    add("kspAndroid", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)
    add("kspIosX64", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}
