plugins {
    kotlin("jvm") version libs.versions.kotlin.jvm
    kotlin("plugin.spring") version libs.versions.kotlin.plugin.spring
    id("org.springframework.boot") version libs.versions.spring.boot
    id("io.spring.dependency-management") version libs.versions.spring.dependency.management
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

@Suppress("UnstableApiUsage")
configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

buildscript {
    configurations.classpath {
        resolutionStrategy.activateDependencyLocking()
    }
}

dependencyLocking {
    lockAllConfigurations()
    lockMode = LockMode.STRICT
}

dependencies {
    implementation("com.google.maps:google-maps-services:${libs.versions.google.maps.services.get()}")
    implementation("org.apache.httpcomponents.client5:httpclient5")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.micrometer:micrometer-tracing-bridge-brave")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    compileOnly("org.projectlombok:lombok")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
