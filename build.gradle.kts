import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.0.0-Beta5"
    id("io.papermc.paperweight.userdev") version "1.5.15"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "de.coaster.cringepvp"
version = "2.0-SNAPSHOT"

val ascendVersion = "2024.1.2"
val stackedVersion = "2024.1.1"
val exposedVersion: String = "0.48.0"
val minecraftVersion: String = "1.20.4"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.kryptonmc.org/releases")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://repo.fruxz.dev/releases/")
}

val shadowDependencies = listOf(
    "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1-Beta",
    "org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3",
    "io.github.cdimascio:dotenv-kotlin:6.4.1",
    "net.oneandone.reflections8:reflections8:0.11.7",
    "dev.fruxz:ascend:$ascendVersion",
    "dev.fruxz:stacked:$stackedVersion",
    "org.jetbrains.exposed:exposed-core:$exposedVersion",
    "org.jetbrains.exposed:exposed-dao:$exposedVersion",
    "org.jetbrains.exposed:exposed-jdbc:$exposedVersion",
    "org.jetbrains.exposed:exposed-java-time:$exposedVersion",
    "com.mysql:mysql-connector-j:8.3.0",
    "com.zaxxer:HikariCP:5.1.0",
)

dependencies {

    // Shadow Dependencies
    shadowDependencies.forEach { dependency ->
        implementation(dependency)
        shadow(dependency)
    }

    // Minecraft PaperMC Dependencies
    paperweight.paperDevBundle("${minecraftVersion}-R0.1-SNAPSHOT")
    compileOnly("me.neznamy", "tab-api", "4.0.2")
    compileOnly("com.github.decentsoftware-eu", "decentholograms", "2.8.6")
    compileOnly("com.comphenix.protocol", "ProtocolLib", "4.8.0")
    compileOnly("me.clip:placeholderapi:2.11.5")
    compileOnly("com.github.NuVotifier.NuVotifier:nuvotifier-common:2.7.2")
    compileOnly("com.github.NuVotifier.NuVotifier:nuvotifier-bukkit:2.7.2")
    compileOnly("com.github.NuVotifier.NuVotifier:nuvotifier-api:2.7.2")
}

tasks {
    build {
        dependsOn("shadowJar")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlin {
        compilerOptions {
            apiVersion.set(KotlinVersion.KOTLIN_2_0)
            jvmTarget.set(JvmTarget.JVM_21)
        }
    }

    withType<ShadowJar> {
        mergeServiceFiles()
        configurations = listOf(project.configurations.shadow.get())
        archiveFileName.set("CringePvP.jar")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}


