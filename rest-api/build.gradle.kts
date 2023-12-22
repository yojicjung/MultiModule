import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

val jar: Jar by tasks
val bootJar: BootJar by tasks

bootJar.enabled = true
jar.enabled = false

plugins {
    id("org.springframework.boot") version "3.1.5"
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.spring") version "1.9.20"
    kotlin("kapt") version "1.9.20"
}

group = "com.hmcnetworks"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    // 의존
    implementation(project(":domain"))
    implementation(project(":common"))
    implementation(project(":app-core"))
    implementation(project(":db-access"))

    kapt("org.springframework.boot:spring-boot-configuration-processor:2.7.2")

    // spring web
    implementation("org.springframework.boot:spring-boot-starter-web:3.1.5")
    // valid
    implementation("org.springframework.boot:spring-boot-starter-validation:3.1.5")

    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // json 변환 의존성 추가
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")

    // mapstructs
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    kapt("org.mapstruct:mapstruct-processor:1.5.5.Final")

    testImplementation("org.springframework.boot:spring-boot-starter-test:3.1.5")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc:3.0.0")
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
