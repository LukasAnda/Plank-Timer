import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
}

android {
    namespace = "com.lukascodes.planktimer"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.lukascodes.planktimer.androidApp"
        minSdk = 24
        targetSdk = 34
        versionCode = properties["APP_VERSION_CODE"]?.toString()?.toInt() ?: 1
        versionName = properties["APP_VERSION_NAME"]?.toString() ?: "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        getByName("debug") {
            storeFile = rootProject.file("release/app-debug.jks")
            storePassword = "mAaCKAajr3k0l6RcJcMhHyL"
            keyAlias = "debug"
            keyPassword = "mAaCKAajr3k0l6RcJcMhHyL"
        }

        create("release") {
            if (rootProject.file("release/app-release.jks").exists()) {
                var keystorePropertiesFile: File? = null
                val projectDir = File(System.getProperty("user.dir"))
                for (file in projectDir.listFiles() ?: emptyArray()) {
                    if (file.name == "keystore.properties") {
                        keystorePropertiesFile = file
                        break
                    }
                }
                val keystoreProperties = Properties()
                if (keystorePropertiesFile != null) {
                    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
                } else {
                    println("keystore.properties file not found.")
                }

                storeFile = rootProject.file("release/app-release.jks")
                storePassword = keystoreProperties["KEYSTORE_PASSWORD"]?.toString() ?: ""
                keyAlias = keystoreProperties["KEY_ALIAS"]?.toString() ?: ""
                keyPassword = keystoreProperties["KEY_PASSWORD"]?.toString() ?: ""
            }
        }
    }
    packaging {
        resources.excludes += setOf(
            // Exclude AndroidX version files
            "META-INF/*.version",
            // Exclude consumer proguard files
            "META-INF/proguard/*",
            // Exclude the Firebase/Fabric/other random properties files
            "/*.properties",
            "fabric/*.properties",
            "META-INF/*.properties",
            // License files
            "LICENSE*",
            // Exclude Kotlin unused files
            "META-INF/**/previous-compilation-data.bin",
        )
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs["debug"]
            versionNameSuffix = "-dev"
        }

        release {
            signingConfig = signingConfigs.findByName("release") ?: signingConfigs["debug"]
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
}

dependencies {
    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.androidx.startup.runtime)
    implementation(libs.androidx.activityCompose)
    implementation(project.dependencies.platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)
    implementation(project(":composeApp"))
    coreLibraryDesugaring(libs.core.library.desugaring)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}