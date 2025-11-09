plugins {
    idea
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    jacoco
    kotlin("jvm") version libs.versions.kotlin.jvm
    kotlin("plugin.spring") version libs.versions.kotlin.plugin.spring
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

dependencyLocking {
    lockAllConfigurations()
    lockMode = LockMode.STRICT
}

buildscript {
    configurations.classpath {
        resolutionStrategy {
            activateDependencyLocking()
        }
    }

    configurations.all {
        resolutionStrategy {
            componentSelection {
                all allComponentSelection@ {
                    if (Regex("(?i)Beta|Alpha|RC|M").containsMatchIn(candidate.version)) {
                        reject("Rejecting ${candidate.version} as it's an excluded version")
                    }
                }
            }
        }
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }

    all {
        resolutionStrategy {
            componentSelection {
                all allComponentSelection@ {
                    if (
                        (candidate.group == "io.opentelemetry.proto" && candidate.module == "opentelemetry-proto")
                        || (candidate.group == "com.google.guava" && candidate.module == "listenablefuture")
                    ) {
                        return@allComponentSelection
                    }
                    if (Regex("(?i)Beta|Alpha|RC|M").containsMatchIn(candidate.version)) {
                        reject("Rejecting ${candidate.version} as it's an excluded version")
                    }
                }
            }
        }
    }
}

dependencies {
    implementation(libs.apache.http.components.client5)
    implementation(libs.flyway.core)
    implementation(libs.flyway.postgresql)
    implementation(libs.google.maps.places)
    implementation(libs.google.maps.services)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.telegram.bot)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.reactor)
    implementation(libs.micrometer.registry.otlp)
    implementation(libs.micrometer.tracing.bridge.brave)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.flyway)
    implementation(libs.spring.boot.starter.jdbc)
    implementation(libs.spring.boot.starter.jetty)
    implementation(libs.spring.boot.starter.webmvc) {
        // Exclude Tomcat as the embedded server since Undertow is used
        exclude(group = libs.spring.boot.starter.tomcat.get().group, module = libs.spring.boot.starter.tomcat.get().name)
    }
    implementation(libs.spring.boot.starter.webflux)

    developmentOnly(libs.spring.boot.devtools)

    runtimeOnly(libs.micrometer.registry.prometheus)
    runtimeOnly(libs.postgresql)

    annotationProcessor(libs.spring.boot.configuration.processor)

    testImplementation("${libs.google.api.gax.grpc.get()}:testlib")
    testImplementation("${libs.google.maps.places.get()}:tests")
    testImplementation(libs.grpc.google.maps.places.v1)
    testImplementation(libs.kotest)
    testImplementation(libs.kotest.extensions.spring)
    testImplementation(libs.kotlin.test.junit5)
    testImplementation(libs.mockk)
    testImplementation(libs.spring.boot.starter.actuator.test)
    testImplementation(libs.spring.boot.starter.flyway.test)
    testImplementation(libs.spring.boot.starter.jdbc.test)
    testImplementation(libs.spring.boot.starter.webmvc.test)
    testImplementation(libs.spring.boot.starter.webflux.test)
    testImplementation(libs.wiremock)

    testRuntimeOnly(libs.junit.platform.launcher)

    testAndDevelopmentOnly(libs.spring.boot.docker.compose)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xjsr305=strict",
            "-Xannotation-default-target=param-property"
        )
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)

    jvmArgs = listOf(
        "-javaagent:${configurations.testRuntimeClasspath.get().find { it.name.contains("mockito-core") }}",
        "-javaagent:${configurations.testRuntimeClasspath.get().find { it.name.contains("byte-buddy-agent") }}",
    )
}

tasks.jacocoTestReport {
    dependsOn(tasks.withType<Test>())
    reports {
        xml.required.set(true)
    }
}

tasks.jar {
    enabled = false
}

tasks.bootJar {
    archiveFileName.set("${project.name}-service.jar")
}
