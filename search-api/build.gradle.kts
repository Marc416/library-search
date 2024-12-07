import org.springframework.boot.gradle.tasks.bundling.BootJar

tasks.named<BootJar>("bootJar") {
    enabled = true
}