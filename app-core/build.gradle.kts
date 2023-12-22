import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

val jar: Jar by tasks
val bootJar: BootJar by tasks

bootJar.enabled = false
jar.enabled = true

plugins {
    id("org.springframework.boot") version "3.1.5"
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.spring") version "1.9.20"
}

group = "com.hmcnetworks"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}
configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // 헥사곤 의존
    implementation(project(":domain"))
    implementation(project(":common"))

    implementation("org.springframework.boot:spring-boot-starter-web:3.1.5")
    implementation("org.springframework:spring-tx:5.3.22")

    implementation("org.jetbrains.kotlin:kotlin-reflect")

    developmentOnly("org.springframework.boot:spring-boot-devtools:3.1.5")

    testImplementation("org.springframework.boot:spring-boot-starter-test:3.1.5")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<Test> {
    useJUnitPlatform()
}
