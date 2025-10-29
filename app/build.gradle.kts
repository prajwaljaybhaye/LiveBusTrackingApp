plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.android.libraries.mapsplatform.secrets.gradle.plugin)
}

android {
    namespace = "com.litsbros.livebustrackingsystem"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.litsbros.livebustrackingsystem"
        minSdk = 25
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.play.services.location)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)


    // Maps + Compose
    implementation(libs.play.services.maps)
    implementation(libs.maps.compose)
    implementation ("com.google.accompanist:accompanist-permissions:0.37.3")
    implementation("com.google.maps.android:android-maps-utils:3.19.0")


    //animataion
    implementation("com.github.p72b:PolylineAnimator:0.1.3")

    // Icons + coroutines
    implementation(libs.androidx.compose.material.icons.extended)

    implementation("androidx.compose.material3:material3:1.4.0")
    implementation(libs.kotlinx.coroutines.play.services)

    //retrofit clint
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.4")
    //relod refresh feature
    implementation("com.google.accompanist:accompanist-swiperefresh:0.36.0")
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.androidx.compose.material3.window.size.class1)




    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}
