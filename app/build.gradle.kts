plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("kotlin-android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.mp_draft10"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.example.mp_draft10"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        android.buildFeatures.buildConfig = true
        val apiKey = properties["API_KEY"]?.toString() ?: "\"\""

        buildConfigField("String", "API_KEY", apiKey)
        // Define BASE_URL as a string literal here
        buildConfigField("String", "BASE_URL", "\"https://pixabay.com/api/\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
        pickFirst("META-INF/LICENSE.md")
        pickFirst("META-INF/LICENSE-notice.md")
        pickFirst ("mockito-extensions/org.mockito.plugins.MockMaker")


    }

}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation ("androidx.compose.runtime:runtime-livedata")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation ("androidx.navigation:navigation-compose:2.7.7")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation(platform("com.google.firebase:firebase-bom:32.7.2"))
    implementation("com.google.firebase:firebase-database:20.3.1")
    implementation("com.google.firebase:firebase-firestore:24.10.0")
    implementation("androidx.datastore:datastore-core:1.0.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.paging:paging-common-android:3.3.0-beta01")
    implementation("androidx.compose.material3:material3-window-size-class-android:1.2.1")
    implementation("androidx.compose.material3:material3-android:1.2.1")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation ("androidx.compose.material:material:1.6.3")
    implementation ("androidx.compose.material3:material3:1.2.1")
    implementation ("androidx.compose.material:material-icons-extended:1.6.3")
    implementation ("androidx.compose.runtime:runtime")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    implementation("com.google.dagger:hilt-android:2.48.1")
    kapt("com.google.dagger:hilt-android-compiler:2.48.1")
    implementation("androidx.core:core-ktx:1.12.0")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    annotationProcessor ("com.google.dagger:hilt-compiler:2.48.1")

    kapt("androidx.hilt:hilt-compiler:1.2.0")
    implementation("androidx.hilt:hilt-navigation-fragment:1.2.0")
    implementation ("androidx.hilt:hilt-navigation-compose:1.2.0")
    kaptAndroidTest ("androidx.hilt:hilt-compiler:1.2.0")


    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:32.7.3"))

    // Declare the dependency for the Cloud Firestore library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-firestore")
    implementation ("androidx.compose.runtime:runtime-livedata")

    implementation ("com.google.firebase:firebase-auth:latest_version")

// JUnit
    testImplementation ("org.mockito:mockito-inline:4.0.0") // For final classes
// Coroutines Test
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
// Architecture Components
    testImplementation ("androidx.arch.core:core-testing:2.2.0")

    //Android UI testing
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
//    androidTestImplementation ("dagger.hilt.android.testing:hilt-android-test:2.38.1")
//    kaptAndroidTest ("dagger.hilt.android.compiler:hilt-compiler:2.38.1")

    androidTestAnnotationProcessor ("com.google.dagger:hilt-android-compiler:2.48.1")

    kaptTest("com.google.dagger:hilt-android-compiler:2.48.1")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.48.1")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.48.1")
    androidTestImplementation ("com.google.dagger:hilt-android-testing:2.50")
    kaptAndroidTest ("com.google.dagger:hilt-android-compiler:2.48.1")
    testImplementation ("org.mockito:mockito-core:4.0.0")
    androidTestImplementation ("org.mockito:mockito-android:4.0.0")
    testImplementation ("io.mockk:mockk:1.13.4")
    androidTestImplementation ("io.mockk:mockk-android:1.13.4")
    testImplementation ("app.cash.turbine:turbine:1.0.0")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
    testImplementation ("org.mockito.kotlin:mockito-kotlin:4.0.0")
    androidTestImplementation ("org.mockito:mockito-inline:4.0.0")
//    testImplementation ("androidx.arch.core:core-testing:2.2.0")
    testImplementation("android.arch.core:core-testing:1.1.1")
    androidTestImplementation("android.arch.core:core-testing:1.1.1")

    testImplementation ("junit:junit:4.13.2")
    testImplementation ("org.mockito:mockito-core:4.0.0")
    testImplementation ("org.mockito:mockito-inline:4.0.0")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
    testImplementation ("org.mockito:mockito-core:4.0.0")
    testImplementation ("androidx.arch.core:core-testing:2.2.0")
    androidTestImplementation ("androidx.arch.core:core-testing:2.2.0")
    implementation ("com.google.code.gson:gson:2.10.1")

// Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-moshi:2.9.0")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.9.2")
    // Gson converter for Retrofit
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
    implementation ("androidx.recyclerview:recyclerview:1.3.2")
    implementation ("com.squareup.moshi:moshi:1.12.0")
    implementation ("com.squareup.moshi:moshi-kotlin:1.12.0")
    kapt ("com.squareup.moshi:moshi-kotlin-codegen:1.12.0")
    implementation ("io.coil-kt:coil-compose:2.4.0")
    implementation ("com.squareup.okhttp3:okhttp:4.11.0")

    implementation ("androidx.compose.ui:ui:latest-compose-ui-version")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.3")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.moshi:moshi:1.12.0")
    implementation ("com.squareup.moshi:moshi-kotlin:1.12.0")
    kapt ("com.squareup.moshi:moshi-kotlin-codegen:1.12.0")
    implementation ("com.squareup.retrofit2:converter-moshi:2.9.0")
    testImplementation ("com.google.truth:truth:1.1.3")
    implementation ("com.squareup.moshi:moshi-kotlin:1.12.0")

    implementation ("com.squareup.moshi:moshi:1.12.0")
    implementation ("com.squareup.moshi:moshi-kotlin:1.12.0")
    implementation ("org.jetbrains.kotlin:kotlin-reflect:1.9.20")

    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")

}

kapt {
    correctErrorTypes = true
}