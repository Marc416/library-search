import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("plugin.jpa") version "2.1.0"
}

tasks.named<BootJar>("bootJar") {
    enabled = true
}

dependencies {
    implementation(project(":common"))
    implementation(project(":external:naver-client"))
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.h2database:h2")
}