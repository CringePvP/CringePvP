import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
}

group = "de.coaster.cringepvp"
version = "1.1-SNAPSHOT"

val ascendVersion = "1.0.0-RC"
val stackedVersion = "1.0.0-RC"
val sparkleVersion = "1.0.0-PRE-18-RC3"
val exposedVersion: String = "0.38.2"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.kryptonmc.org/releases")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.dmulloy2.net/repository/public/")
}

dependencies {

    // Kotlin Base Dependencies
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.3-native-mt")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")

    // Minecraft PaperMC Dependencies
    compileOnly("io.papermc.paper:paper-api:1.19-R0.1-SNAPSHOT")
    compileOnly("me.neznamy", "tab-api", "3.1.2")
    compileOnly("com.github.decentsoftware-eu", "decentholograms", "2.5.3")
    compileOnly("com.comphenix.protocol", "ProtocolLib", "4.8.0")
    compileOnly("me.clip:placeholderapi:2.11.2")
    compileOnly("com.github.NuVotifier.NuVotifier:nuvotifier-common:2.7.2")
    compileOnly("com.github.NuVotifier.NuVotifier:nuvotifier-bukkit:2.7.2")
    compileOnly("com.github.NuVotifier.NuVotifier:nuvotifier-api:2.7.2")

    // Reflection Dependencies for automatic registration of commands and listeners
    implementation("net.oneandone.reflections8:reflections8:0.11.7")

    // Molten Kotlin Libraries:
    implementation("com.github.TheFruxz:Ascend:$ascendVersion") // (https://github.com/TheFruxz/Ascend)
    implementation("com.github.TheFruxz:Stacked:$stackedVersion") // (https://github.com/TheFruxz/Stacked)
    implementation("com.github.TheFruxz:Sparkle:$sparkleVersion") // (https://github.com/TheFruxz/Sparkle)


    // Database Dependencies - Kotlin Exposed
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "17"
    kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn" + "-Xcontext-receivers" + "-Xuse-experimental=kotlin.Experimental"
}
