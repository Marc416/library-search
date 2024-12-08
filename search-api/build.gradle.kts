import org.springframework.boot.gradle.tasks.bundling.BootJar

tasks.named<BootJar>("bootJar") {
    enabled = true
}

dependencies {
    api("org.springframework.cloud:spring-cloud-starter-openfeign:4.2.0")
}

subprojects{
    dependencies{
        implementation(project(":common"))
        implementation(project(":external:naver-client"))
    }
}