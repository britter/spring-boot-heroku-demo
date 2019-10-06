/*
 * Copyright 2019 Benedikt Ritter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
plugins {
    id("com.gradle.build-scan") version "2.4.2"
    java
    jacoco
    id("org.springframework.boot") version "2.1.8.RELEASE"
    id("com.github.kt3k.coveralls") version "2.8.4"
    id("com.palantir.docker-run") version "0.22.1"
    kotlin("jvm") version "1.3.50"
    kotlin("plugin.spring") version "1.3.50"
}
apply(from = "gradle/git-version-data.gradle")
apply(from = "gradle/build-scan-data.gradle")

group = "com.github.britter"
version = "0.2.9"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation(platform("org.springframework.boot:spring-boot-dependencies:2.1.8.RELEASE"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.hsqldb:hsqldb:2.3.3")
    implementation("javax.xml.bind:jaxb-api:2.2.11")
    implementation("javax.validation:validation-api:1.1.0.Final")
    implementation("org.postgresql:postgresql:9.4-1203-jdbc42")

    testImplementation(platform("org.springframework.boot:spring-boot-dependencies:2.1.8.RELEASE"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.2")
    testImplementation("org.assertj:assertj-core:3.13.2")
    testImplementation("io.mockk:mockk:1.9")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks {
    processResources {
        filesMatching("**/*.properties") {
            expand(project.properties)
        }
    }

    compileJava {
        options.encoding = "UTF-8"
    }

    compileKotlin {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }

    test {
        useJUnitPlatform()
    }

    val jacocoTestReport = named<JacocoReport>("jacocoTestReport") {
        reports {
            xml.isEnabled = true
        }
    }

    coveralls {
        jacocoReportPath = jacocoTestReport.map { it.reports.xml.destination }
    }

    bootJar {
        archiveFileName.set("app.jar")
    }

    bootRun {
        if (project.hasProperty("postgres")) {
            setArgsString("--spring.profiles.active=postgres")
        }
    }
}

dockerRun {
    image = "postgres:9.4.4"
    name = "postgres-db"
    ports("5432:5432")
    env(mapOf(
        "POSTGRES_PASSWORD" to "spring-boot-heroku-example",
        "POSTGRES_USER" to "spring-boot-heroku-example"
    ))
}

buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = "yes"

    publishAlways()
}
