import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.apkmnn.diffusion"
    compileSdk = 36
    ndkVersion = "27.2.12479018"

    signingConfigs {
        create("release") {
            val props = Properties()
            val lp = rootProject.file("local.properties")
            if (lp.exists()) props.load(FileInputStream(lp))
            val keystoreFile = props.getProperty("KEYSTORE_FILE", "")
            if (keystoreFile.isNotEmpty()) {
                storeFile = file(keystoreFile)
                storePassword = props.getProperty("KEYSTORE_PASSWORD", "")
                keyAlias = props.getProperty("KEY_ALIAS", "")
                keyPassword = props.getProperty("KEY_PASSWORD", "")
            }
        }
    }

    defaultConfig {
        applicationId = "com.apkmnn.diffusion"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "0.0.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            abiFilters.add("arm64-v8a")
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
        }
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    packaging {
        jniLibs {
            pickFirsts += setOf(
                "lib/arm64-v8a/libc++_shared.so"
            )
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.runtime)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.mmkv)
}