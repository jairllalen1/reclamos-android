plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.jairllalen.reclamos_android"

    compileSdk = 37

    defaultConfig {
        applicationId = "com.jairllalen.reclamos_android"

        minSdk = 24
        targetSdk = 37

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }
}

dependencies {

    // =========================
    // Jetpack Compose
    // =========================

    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // Íconos Compose
    implementation("androidx.compose.material:material-icons-extended")

    debugImplementation(libs.androidx.compose.ui.tooling)

    // =========================
    // Activity
    // =========================

    implementation(libs.androidx.activity.compose)

    // =========================
    // Navigation
    // =========================

    implementation(libs.androidx.navigation.compose)

    // =========================
    // Lifecycle / ViewModel
    // =========================

    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.1")
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // =========================
    // Retrofit
    // =========================

    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.retrofit.gson)

    // =========================
    // OkHttp
    // =========================

    implementation(libs.squareup.okhttp)
    implementation(libs.squareup.okhttp.logging)

    // =========================
    // Tests
    // =========================

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}