plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.maptester"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.maptester"
        minSdk = 29
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // 네이버 지도 SDK
    implementation("com.naver.maps:map-sdk:3.18.0")
    // Google Location
    implementation("com.google.android.gms:play-services-location:21.3.0")
    // retrofit2
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    // moshi
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    // okHttp
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    // gson
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    // proj4js
    implementation("org.locationtech.proj4j:proj4j:1.3.0")
    implementation("org.locationtech.proj4j:proj4j-epsg:1.3.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}