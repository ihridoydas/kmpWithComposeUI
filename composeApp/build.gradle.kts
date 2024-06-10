import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)

    kotlin("plugin.serialization") version "1.9.21" //decompose step2
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }
    
    jvm("desktop")
    //Step 1
    //Tool bundler for converting Kotlin/Native code to JavaScript code.
    js(IR){
        moduleName = "kmpWithComposeUI"
        browser(){
            commonWebpackConfig(){
                outputFileName = "kmpWithComposeUI.js"
                devServer = (devServer
                    ?: KotlinWebpackConfig.DevServer()).copy()
            }
            //it will be generate executable js file
            binaries.executable()
        }

    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true

            export("com.arkivanov.decompose:decompose:2.2.2-compose-experimental")
            export("com.arkivanov.essenty:lifecycle:1.3.0")
        }
    }
    
    sourceSets {
        val desktopMain by getting
        
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            //ktor client
            implementation(libs.ktor.client.android)

            //decompose step3
            implementation("com.arkivanov.decompose:decompose:2.2.2-compose-experimental")
            implementation("com.arkivanov.decompose:extensions-compose-jetbrains:2.2.2-compose-experimental")

            //koin step2
            implementation("io.insert-koin:koin-android:3.5.3")
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.material3)
            implementation(compose.ui)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            implementation(libs.mvvm.core)

            api(libs.image.loader)

            implementation("com.arkivanov.decompose:decompose:2.2.2-compose-experimental")
            implementation("com.arkivanov.decompose:extensions-compose-jetbrains:2.2.2-compose-experimental")
            //decompose step1

            //koin step1
            implementation("io.insert-koin:koin-core:3.5.3")

        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
        iosMain.dependencies {
            //ktor client
            implementation(libs.ktor.client.darwin)

            api("com.arkivanov.decompose:decompose:2.2.2-compose-experimental")
            api("com.arkivanov.essenty:lifecycle:1.3.0")
        }
    }
}

android {
    namespace = "info.hridoy.kmpcomposeui"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "info.hridoy.kmpcomposeui"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "info.hridoy.kmpcomposeui"
            packageVersion = "1.0.0"
        }
    }
}

//Step 3
compose.experimental {
    web.application {}
}
