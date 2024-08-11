plugins {
    kotlin("jvm") version "1.9.+"
    kotlin("plugin.spring") version "1.9.+"
    id("org.springframework.boot") version "3.3.+"
    id("io.spring.dependency-management") version "1.1.+"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

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
