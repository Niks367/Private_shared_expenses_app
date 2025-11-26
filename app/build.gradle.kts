plugins {
    // 1. Plugins SHOULD NOT be applied here. They are usually applied in the root
    // settings.gradle.kts or build.gradle.kts and inherited by the app module.
    // However, to keep it building (as you noted), we keep them for now, but
    // the application plugin should NOT be in the plugins block AND at the same time
    // declared in the root build.gradle.kts with apply false.
    // The preferred way for the app module is to use id(...) just for the application:
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
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
    
    // Set up the correct test source set directories for clarity and consistency
    sourceSets {
        getByName("test") {
            // Your pure JUnit tests (e.g., DebtCalculatorTest.kt) go here
            java.srcDirs("src/test/kotlin")
        }
        getByName("androidTest") {
            // Your Instrumented tests (e.g., UserRepositoryTest.kt) go here
            java.srcDirs("src/androidTest/kotlin")
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        // You should align this version with the Kotlin version in your root build.gradle.kts
        kotlinCompilerExtensionVersion = "1.5.10"
    }

    compileOptions {
        // Use Java 17 for both source and target compatibility
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // 2. Add dependencies required for the Coroutine tests (UserRepositoryTest.kt)
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.compose.ui:ui:1.5.10")
    implementation("androidx.compose.material3:material3:1.3.2")
    implementation("androidx.activity:activity-compose:1.8.2")

    // Local Unit Tests (DebtCalculatorTest, TransactionDtoTest)
    testImplementation("junit:junit:4.13.2")
    
    // Add Coroutine testing library needed for UserRepositoryTest
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3") 
    
    // Add Mocking library needed for UserRepositoryTest
    // Replace with MockK or other library if preferred
    testImplementation("org.mockito:mockito-core:4.8.0") 

    // Instrumented Android Tests (UserRepositoryTest - if using Android APIs)
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    
    // 3. Add necessary Compose test dependencies (if you have Compose UI tests)
    val composeBom = platform("androidx.compose:compose-bom:2023.10.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}