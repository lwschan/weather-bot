plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    jacoco
    kotlin("jvm") version libs.versions.kotlin.jvm
    kotlin("plugin.spring") version libs.versions.kotlin.plugin.spring
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

buildscript {
    configurations.classpath {
        resolutionStrategy {
            activateDependencyLocking()

            componentSelection {
                all {
                    val candidateVersion = candidate.version
                    if (candidateVersion.contains("Beta") || candidateVersion.contains("Alpha") || candidateVersion.contains("RC")) {
                        reject("Rejecting $candidateVersion as it's an excluded version")
                    }
                }
            }
        }
    }
}

dependencyLocking {
    lockAllConfigurations()
    lockMode = LockMode.STRICT
}

dependencies {
    implementation(libs.apache.http.components.client5)
    implementation(libs.flyway.core)
    implementation(libs.flyway.postgresql)
    implementation(libs.google.maps.places)
    implementation(libs.google.maps.services)
    implementation(libs.jackson.datatype.jsr310)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.telegram.bot)
    implementation(libs.kotlinx.coroutines.reactor)
    implementation(libs.micrometer.tracing.bridge.brave)
    implementation(libs.retrofit2)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.jdbc)
    implementation(libs.spring.boot.starter.web)

    developmentOnly(libs.spring.boot.devtools)

    runtimeOnly(libs.micrometer.registry.prometheus)
    runtimeOnly(libs.postgresql)

    annotationProcessor(libs.spring.boot.configuration.processor)

    testImplementation("${libs.google.api.gax.grpc.get()}:testlib")
    testImplementation("${libs.google.maps.places.get()}:tests")
    testImplementation(libs.grpc.google.maps.places.v1)
    testImplementation(libs.kotest)
    testImplementation(libs.kotest.extensions.spring)
    testImplementation(libs.kotest.framework.datatest)
    testImplementation(libs.kotlin.test.junit5)
    testImplementation(libs.mockk)
    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.boot.starter.webflux)
    testImplementation(libs.wiremock)

    testRuntimeOnly(libs.junit.platform.launcher)

    testAndDevelopmentOnly(libs.spring.boot.docker.compose)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.withType<Test>())
    reports {
        xml.required.set(true)
    }
}
