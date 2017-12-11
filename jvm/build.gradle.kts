import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    id("kotlin-platform-jvm")
    id("com.github.johnrengelman.shadow") version "2.0.1"
}

application {
    mainClassName = "ru.endlesscode.lzw.CeymUtilKt"
}

tasks.withType<ShadowJar> {
    manifest {
        attributes.apply {
            put("Implementation-Title", "CEYM util")
            put("Implementation-Version", version)
            put("Main-Class", application.mainClassName)
        }
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

dependencies {
    expectedBy(project(":common"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("info.picocli:picocli:2.1.0")
    testCompile(kotlin("test"))
    testCompile(kotlin("test-junit"))
}
