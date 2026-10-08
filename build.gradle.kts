plugins {
    java
    id("io.quarkus")
}

group = "io.gluonify.source"
version = "0.1.0-SNAPSHOT"
description = "Starter Quarkus 4 service: REST, Quinoa UI, Charm authentication, data (distributed files or Gdown), Photon webhooks."

val quarkusVersion: String by project
val quinoaVersion: String by project

repositories {
    mavenCentral()
}

dependencies {
    implementation(enforcedPlatform("io.quarkus.platform:quarkus-bom:$quarkusVersion"))

    // REST + JSON (Jackson 3)
    implementation("io.quarkus:quarkus-rest-jackson")
    // Request body validation (@NotBlank, @Size...)
    implementation("io.quarkus:quarkus-hibernate-validator")
    // Health: REQUIRED by Gluonify (builder rule R-SANTE): traffic only arrives if /q/health/ready answers 200
    implementation("io.quarkus:quarkus-smallrye-health")
    // Authentication: gluonify-charm tokens (OpenID Connect, "service" mode)
    implementation("io.quarkus:quarkus-oidc")
    // OpenAPI contract (/q/openapi) and Swagger UI (/q/swagger-ui)
    implementation("io.quarkus:quarkus-smallrye-openapi")
    // Prometheus metrics (/q/metrics): the platform scrapes them
    implementation("io.quarkus:quarkus-micrometer-registry-prometheus")
    // Web UI: Quinoa builds and serves the Vue project in src/main/webui
    implementation("io.quarkiverse.quinoa:quarkus-quinoa:$quinoaVersion")

    testImplementation("io.quarkus:quarkus-junit")
    testImplementation("io.quarkus:quarkus-test-security")
    testImplementation("io.rest-assured:rest-assured")
}

java {
    toolchain { languageVersion = JavaLanguageVersion.of(25) }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}

tasks.test {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}
