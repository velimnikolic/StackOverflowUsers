plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.hilt.gradlePlugin)
    implementation(libs.kotlin.compose.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.ksp.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "stackoverflowusers.android.application"
            implementationClass = "me.nikola.stackoverflowusers.AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "stackoverflowusers.android.application.compose"
            implementationClass = "me.nikola.stackoverflowusers.AndroidApplicationComposeConventionPlugin"
        }
        register("androidHilt") {
            id = "stackoverflowusers.android.hilt"
            implementationClass = "me.nikola.stackoverflowusers.AndroidHiltConventionPlugin"
        }
    }
}
