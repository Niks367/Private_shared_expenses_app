plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("kapt")
    kotlin("plugin.serialization") version "1.9.22" # Note: Consider updating this version to align with Kotlin 2.2.10
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
        // CONSIDER UPGRADING to JavaVersion.VERSION_17
        sourceCompatibility = JavaVersion.VERSION_11 
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        // CONSIDER UPGRADING to "17"
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
        // WARNING: 1.5.10 is likely incompatible with Kotlin 2.2.10
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
    // MOVED to libs.versions.toml
    implementation(libs.google.accompanist.permissions) 
    
    // ---------- MATERIAL ICONS (from expensesAndGroups) ----------
    // You could move this to the TOML file too, but keeping it simple for now
    implementation("androidx.compose.material:material-icons-extended-android:1.6.8") 

    // ---------- OPTIONAL IMAGE LIBRARIES ----------
    implementation(libs.landscapist)
    implementation(libs.landscapist.coil3)

    // ---------- TEST AND DATA LAYER LIBRARIES (Cleaned up) ----------
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Retrofit (Now uses TOML reference)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.google.gson)

    // Room KAPT (Now uses TOML reference)
    kapt(libs.androidx.room.compiler)

    // Ktor for WebSocket client (Now uses TOML reference)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.websockets)
    implementation(libs.kotlinx.serialization.json)
}