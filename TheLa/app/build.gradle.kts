plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.TheLa"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.TheLa"
        minSdk = 24
        targetSdk = 35
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

    buildFeatures {
        viewBinding = true
    }

    packaging {
        resources {
            excludes += "META-INF/NOTICE.md"
            excludes += "META-INF/LICENSE.md"
        }
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.circleImageView)
    implementation(libs.jdbc)
    implementation(libs.jtds)
    implementation(libs.androidMail)
    implementation(libs.androidActivation)
    implementation(libs.securityCrypto)
    implementation(libs.retrofit)
    implementation(libs.converterGson)
    implementation(libs.volley)
    implementation(libs.circleindicator)
    implementation(libs.glide)
    annotationProcessor(libs.glide.compiler)
}