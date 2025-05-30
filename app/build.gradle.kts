import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
    kotlin("plugin.serialization") version "1.9.0"
}

val props = Properties()
val localPropsFile = rootProject.file("local.properties")
if (localPropsFile.exists()) {
    localPropsFile.inputStream().use { props.load(it) }
}

android {
    namespace = "com.example.antique"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.antique"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "EMAIL_USERNAME", "\"${props["EMAIL_USERNAME"]}\"")
        buildConfigField("String", "EMAIL_PASSWORD", "\"${props["EMAIL_PASSWORD"]}\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    packaging {
        resources {
            excludes += setOf(
                "/META-INF/LICENSE.md",
                "/META-INF/LICENSE-notice.md",
                "/META-INF/NOTICE.md"
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
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.androidx.media3.common.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(platform("com.google.firebase:firebase-bom:33.11.0"))
    implementation("com.google.firebase:firebase-analytics")
    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.4")
    // Icon
    implementation("androidx.compose.material:material-icons-extended:1.6.0")
    //viewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
    // ViewModel utilities for Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")
    // Lifecycles only (without ViewModel or LiveData)
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    // Saved state module for ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.8.6")
    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    //GsonConverterFactory
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    // Coil Compose
    implementation("io.coil-kt:coil-compose:2.2.2")
    // Accompanist FlowLayout
    implementation ("com.google.accompanist:accompanist-flowlayout:0.30.1")
    // Material
    implementation ("androidx.compose.material:material:1.5.4")
    // Cloudinary
    implementation("com.cloudinary:cloudinary-android:2.0.0")
    // Jetpack Compose UI core
    implementation("androidx.compose.ui:ui:1.5.4")
    // Jetpack Compose Foundation
    implementation("androidx.compose.foundation:foundation:1.5.4")
    // JavaMail API
    implementation ("com.sun.mail:android-mail:1.6.7")
    implementation ("com.sun.mail:android-activation:1.6.7")

}