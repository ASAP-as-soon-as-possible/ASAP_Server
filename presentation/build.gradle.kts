allOpen {
    annotation("jakarta.persistence.RestController")
}

dependencies {
    implementation(project(":domain-service"))

    implementation("org.springframework.boot:spring-boot-starter-web")
}

kotlin {
    jvmToolchain(17)
}