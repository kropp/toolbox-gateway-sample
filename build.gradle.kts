plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.serialization)
    `java-library`
}

repositories {
    mavenCentral()
    maven("https://packages.jetbrains.team/maven/p/tbx/gateway")
}

dependencies {
    implementation(libs.gateway.api)
    implementation(libs.slf4j)
    implementation(libs.bundles.serialization)
    implementation(libs.coroutines.core)
    implementation(libs.okhttp)
}

tasks.compileKotlin {
    kotlinOptions.freeCompilerArgs += listOf(
        "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
    )
}
