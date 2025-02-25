import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "me.xkyrell"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.extendedclip.com/releases/")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")
    compileOnly("com.zaxxer:HikariCP:5.0.1")
    compileOnly("com.google.code.gson:gson:2.8.8")
    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("com.google.dagger:dagger:2.48")
    annotationProcessor("com.google.dagger:dagger-compiler:2.48")

    implementation("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
    implementation("com.github.MilkBowl:VaultAPI:1.7")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:4.8.0")
    testImplementation("org.mockito:mockito-inline:4.8.0")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(16))
    }
}

tasks {
    test {
        useJUnitPlatform()
    }

    withType<ShadowJar> {
        archiveFileName.set("${rootProject.name}-${rootProject.version}.jar")
        destinationDirectory.set(rootProject.rootDir.resolve("build"))

        exclude("net/**")
        exclude("com/**")
        exclude("org/**")
        exclude("co/**")
        exclude("io/**")
        exclude("junit/**")
        exclude("javax/**")

        exclude("**/META-INF/**", "**/mojang-translations/**", "**/LICENSE.txt")
    }

    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}

bukkit {
    name = rootProject.name
    author = "Xkyrell"
    version = project.version.toString()
    main = "me.xkyrell.kseconomy.EconomyPlugin"
    softDepend = listOf("PlaceholderAPI", "Vault")
    apiVersion = "1.16"
    libraries = listOf(
        "com.zaxxer:HikariCP:5.0.1",
        "com.google.dagger:dagger:2.48",
        "com.google.code.gson:gson:2.8.8"
    )
}