plugins {
    java
    id("io.quarkus")
}

group = "io.gluonify.source"
version = "0.1.0-SNAPSHOT"
description = "Starter Quarkus 4 service: REST, Quinoa UI, Charm authentication, data (distributed files or Gdown), Photon webhooks, PDF documents (Crystallon)."

val quarkusVersion: String by project
val quinoaVersion: String by project
val gluonifySdkVersion: String by project

// Maven repository of the Gluonify SDK, served by gluonify-sea (public read, nothing to configure but this declaration).
// A Gradle property (its default is in gradle.properties) so that a mirror or a local copy can replace it:
//   ./gradlew test -Pgluonify.sea.maven.url=file:///path/to/repository
val seaMavenUrl = providers.gradleProperty("gluonify.sea.maven.url").get()

repositories {
    mavenCentral()
    // Added to Maven Central, not instead of it. Gluonify's builder (Bup) declares this same repository itself for the builds it runs.
    maven {
        name = "gluonify-sea"
        url = uri(seaMavenUrl)
    }
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
    // PDF documents: the Crystallon client of the Gluonify SDK (Java 17, no CDI; it uses the Jackson of the application, here Jackson 3). Optional at run time: see io.gluonify.source.documents
    implementation("io.gluonify.plasma:gluonify-plasma-crystallon-client:$gluonifySdkVersion")

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
