plugins {
    base
}

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.2.0"))
    }
}

allprojects {
    group = "ru.endlesscode.lzw"
    version = "0.1.0"

    repositories {
        mavenCentral()
    }
}

dependencies {
    subprojects.forEach { archives(it) }
}
