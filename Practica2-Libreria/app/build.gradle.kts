import java.util.Properties


plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "es.ucm.fdi.googlebooksclient"
    compileSdk = 34

    viewBinding {
        enable = true
    }

    defaultConfig {
        applicationId = "es.ucm.fdi.googlebooksclient"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        android.buildFeatures.buildConfig = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        //carga los valores del archivo .properties
        val keystoreFile = project.rootProject.file("apikeys.properties")
        val properties = Properties()
        properties.load(keystoreFile.inputStream())

        //devuelve una clave vacía en caso de que algo salga mal
        val apiKey = properties.getProperty("API_KEY") ?: ""

        buildConfigField("String", "BOOKS_API_KEY", "\"$apiKey\"")
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.squareup.picasso:picasso:2.8") // libreria para mostrar la imagen del libro si tiene
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

