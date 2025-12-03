plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "cl.inacap.eval"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "cl.inacap.eval"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

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
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // BOM de Compose (elige uno, aquí uso el de catálogo de libs)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation.layout)

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.8.3")

    // Retrofit + Moshi + OkHttp
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.11.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // ViewModel + DataStore
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.datastore:datastore-preferences:1.1.7")

    // SplashScreen
    implementation("androidx.core:core-splashscreen:1.0.1")

    // Lottie (solo la última versión)
    implementation("com.airbnb.android:lottie-compose:6.4.0")
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.navigation.testing)
    implementation(libs.androidx.appcompat)

    implementation("com.google.code.gson:gson:2.10.1")
    implementation(libs.androidx.compose.animation.core.lint)
    implementation("androidx.compose.material:material-icons-extended:<versión>")
    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
