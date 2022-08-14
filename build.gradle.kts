import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
}

group = "de.coaster.cringepvp"
version = "1.1-SNAPSHOT"

val moltenVersion: String = "1.0-PRE-14"
val exposedVersion: String = "0.38.2"

repositories {
    mavenCentral()
    maven {
        url = uri("https://papermc.io/repo/repository/maven-public/")
    }
    maven("https://repo.kryptonmc.org/releases")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://jitpack.io")
}

dependencies {
    // Kotlin Base Dependencies
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.3-native-mt")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")

    // Minecraft PaperMC Dependencies
    compileOnly("io.papermc.paper:paper-api:1.19-R0.1-SNAPSHOT")
    compileOnly("me.neznamy", "tab-api", "3.1.2")
    compileOnly("com.github.decentsoftware-eu", "decentholograms", "2.5.3")
    compileOnly("me.clip:placeholderapi:2.11.2")

    // Reflection Dependencies for automatic registration of commands and listeners
    implementation("net.oneandone.reflections8:reflections8:0.11.7")

    // Molten Kotlin Framework (https://github.com/TheFruxz/MoltenKT)
    implementation("com.github.TheFruxz.MoltenKT:moltenkt-core:$moltenVersion")
    implementation("com.github.TheFruxz.MoltenKT:moltenkt-paper:$moltenVersion")
    implementation("com.github.TheFruxz.MoltenKT:moltenkt-unfold:$moltenVersion")

    // Database Dependencies - Kotlin Exposed
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "17"
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
}
