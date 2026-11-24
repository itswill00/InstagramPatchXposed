plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.golda.patchinstagram"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.golda.patchinstagram"
        minSdk = 27
        targetSdk = 36
        versionCode = 1
        versionName = "0.1.0"
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("debug")
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    packaging {
        resources.excludes += listOf("META-INF/*", "META-INF/androidx/**", "META-INF/com/**", "kotlin/**", "**.properties")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    testImplementation(libs.junit)
    compileOnly(libs.libxposed.api)
    compileOnly(files("libs/xposed-api-82.jar"))
}
