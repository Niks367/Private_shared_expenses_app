plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("kapt")
    kotlin("plugin.serialization") version "1.9.22"
}

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
        freeCompilerArgs = listOf("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
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
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.material3)
    implementation(libs.ui)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // ---------- NAVIGATION ----------
    implementation(libs.androidx.navigation.compose)

    // ---------- LIFECYCLE ----------
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // ---------- OTHER GOOGLE / SUPPORT ----------
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation("com.google.accompanist:accompanist-permissions:0.34.0")
    
    // ---------- MATERIAL ICONS (from expensesAndGroups) ----------
    implementation("androidx.compose.material:material-icons-extended-android:1.6.8")

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
    kapt("androidx.room:room-compiler:2.8.4")

    // Ktor for WebSocket client
    implementation("io.ktor:ktor-client-core:2.3.6")
    implementation("io.ktor:ktor-client-okhttp:2.3.6")
    implementation("io.ktor:ktor-client-websockets:2.3.6")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")


}