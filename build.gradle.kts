plugins {
    java
    jacoco
    id("org.springframework.boot") version "2.1.8.RELEASE"
    id("com.github.kt3k.coveralls") version "2.8.4"
}
apply(from = "gradle/git-version-data.gradle")

group = "com.github.britter"
version = "0.2.7"

repositories {
    mavenCentral()
}

dependencies {
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
    testImplementation("junit:junit:4.12")
    testImplementation("de.bechte.junit:junit-hierarchicalcontextrunner:4.12.1")
    testImplementation("org.hamcrest:hamcrest-all:1.3")
    testImplementation("org.mockito:mockito-all:2.0.2-beta")
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
}
