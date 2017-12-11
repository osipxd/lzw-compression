import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("kotlin-platform-jvm")
}

dependencies {
    expectedBy(project(":common"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("info.picocli:picocli:2.1.0")
    testCompile(kotlin("test"))
    testCompile(kotlin("test-junit"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
