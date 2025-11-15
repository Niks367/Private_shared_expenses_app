import org.gradle.kotlin.dsl.invoke

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose) }

android {
    namespace = "com.example.myapplication"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    packagingOptions {
        pickFirst("META-INF/gradle/incremental.annotation.processors")
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.10"
    }
}

dependencies {
    // ---------- ONE COMPOSE BOM ----------
    implementation(platform(libs.androidx.compose.bom.v20251100))

    // ---------- CORE AND ACTIVITY ----------
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)

    // ---------- COMPOSE UI ----------
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.room.common.jvm)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.ads.mobile.sdk)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // ---------- NAVIGATION ----------
    implementation(libs.androidx.navigation.compose)

    // ---------- LIFECYCLE ----------
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // ---------- OTHER GOOGLE / SUPPORT ----------
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    // ---------- OPTIONAL IMAGE LIBRARIES ----------
    implementation(libs.landscapist)
    implementation(libs.landscapist.coil3)

    // ---------- TEST ----------
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation(libs.retrofit)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.google.code.gson:gson:2.10.1")

}