plugins {
    kotlin("plugin.spring") version "1.9.23"
    kotlin("plugin.jpa") version "1.9.23"
    kotlin("jvm") version "1.9.21"
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = "com.asap"
    version = "0.0.1-SNAPSHOT"

    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.jetbrains.kotlin.plugin.jpa")

    dependencies {
        // test
        testImplementation("org.jetbrains.kotlin:kotlin-test")
        testImplementation("org.springframework.boot:spring-boot-starter-test")

        // kotest
        testImplementation("io.kotest:kotest-property:5.9.0")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.getByName("jar") {
        enabled = true
    }

    tasks.getByName("bootJar") {
        enabled = false
    }
}
