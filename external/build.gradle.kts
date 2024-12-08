subprojects {
    tasks.named<Jar>("jar") {
        enabled = true
    }

    dependencies {
        implementation(project(":common"))
        api("org.springframework.cloud:spring-cloud-starter-openfeign:4.2.0")
    }
}
