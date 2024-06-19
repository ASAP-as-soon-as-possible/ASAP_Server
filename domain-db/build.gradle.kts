plugins {
    kotlin("kapt")
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

val queryDslVersion = "5.0.0" // QueryDSL Version Setting

dependencies {
    // jpa
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    // QueryDSL Implementation
    api("com.querydsl:querydsl-jpa:${queryDslVersion}:jakarta")
    kapt("com.querydsl:querydsl-apt:${queryDslVersion}:jakarta")
    annotationProcessor("jakarta.annotation:jakarta.annotation-api")
    annotationProcessor("jakarta.persistence:jakarta.persistence-api")
    implementation(kotlin("stdlib-jdk8"))

    // DB
    runtimeOnly("com.mysql:mysql-connector-j")
}

kotlin {
    jvmToolchain(17)
}