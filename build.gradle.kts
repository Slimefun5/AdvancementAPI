plugins {
    java
    `maven-publish`
    id("com.gradleup.shadow")
    id("io.github.intisy.github-gradle")
}

fun latestGitTagVersion(): String? = try {
    val out = providers.exec { workingDir = rootDir; commandLine("git", "describe", "--tags", "--abbrev=0", "--match", "v*"); isIgnoreExitValue = true }
    if (out.result.get().exitValue == 0) out.standardOutput.asText.get().trim().removePrefix("v").takeIf { it.isNotBlank() } else null
} catch (e: Exception) { null }

group = "net.roxeez"
version = (project.findProperty("artifact_version") as String?)?.removePrefix("v")?.takeIf { it.isNotBlank() } ?: latestGitTagVersion() ?: "1.0.0"
description = "Library for creating custom advancements"

github {
    accessToken = System.getenv("GITHUB_TOKEN") ?: ""
    publish {
        tag = System.getenv("GITHUB_REF_NAME")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
    compileOnly("org.jetbrains:annotations:20.1.0")
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }
    jar {
        enabled = false
    }
    shadowJar {
        archiveFileName.set("AdvancementAPI-${project.version}.jar")
        archiveClassifier.set("")
    }
    build {
        dependsOn(shadowJar)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "net.roxeez"
            artifactId = "AdvancementAPI"
            version = project.version.toString()
            artifact(tasks.shadowJar)
        }
    }
}
