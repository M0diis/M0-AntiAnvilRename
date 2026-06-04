import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    id("com.gradleup.shadow") version "9.2.2"
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

group = "me.m0dii"
version = "1.12.1"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.named<ShadowJar>("shadowJar") {
    relocate("org.bstats", "me.m0dii.antianvilrename")

    archiveFileName = "M0-AntiAnvilRename-${version}.jar"
}

val doFirstEula: Task.() -> Unit = {
    val eulaFile = file("run/latest/eula.txt")
    eulaFile.parentFile.mkdirs()
    if (!eulaFile.exists()) {
        eulaFile.writeText("eula=true")
    }
}

tasks {
    runServer {
        runDirectory(file("run/latest"))
        minecraftVersion("1.21.11")

        doFirst(doFirstEula)
    }
}


tasks.processResources {
    filesMatching("**/*.yml") {
        expand("version" to project.version)
    }
}

repositories {
    mavenLocal()
    mavenCentral()

    listOf(
        "https://repo.papermc.io/repository/maven-public/",
        "https://repo.extendedclip.com/content/repositories/placeholderapi/",
        "https://repo.extendedclip.com/releases/",
        "https://ci.ender.zone/plugin/repository/everything/",
        "https://oss.sonatype.org/content/repositories/snapshots",
        "https://oss.sonatype.org/content/repositories/central"
    ).forEach { repoUrl ->
        maven { url = uri(repoUrl) }
    }
}

dependencies {
    implementation("org.bstats:bstats-bukkit:3.0.0")

    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")

    compileOnly("org.projectlombok:lombok:1.18.46")
    annotationProcessor("org.projectlombok:lombok:1.18.46")
}

tasks.test {
    useJUnitPlatform()
}

