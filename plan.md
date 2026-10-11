# plan.md : gluonify-source-gradle

Variante Gradle (Kotlin DSL) de `gluonify-source` : même service, même code, même tests. Garder les deux dépôts en phase quand le code partagé change.

## Pas encore fait
- [ ] Vérifier périodiquement que ce dépôt reste synchronisé avec `gluonify-source` (aucune automatisation de synchronisation pour l'instant : copie manuelle).
- [x] Le README de `gluonify-source` renvoie vers cette variante (citation en tête des cinq README, 2026-10-08).
- [x] **Resynchronisation complète avec `gluonify-source`** (2026-10-11, branche `platform-token` du jumeau Maven) : paquet `documents/` (Crystallon : `POST /api/documents/invoice`, client du SDK 0.6.0, code généré, `source.crystallon.token` = `forwarding` | `platform`, `TokenMode`), `gluonify.json` (les services que le builder déploie : mêmes `uses` que `deploy/appspec.json`), `DeploySpecsTest`, `PlatformResource` (`documents`, `documentsToken`), `NoteStores` et `SourceConfig` (valeur `gdown` de `SOURCE_STORE`, plus `graph` ni `graphdb`), `DevAuthentication`, interface (BrandLogo, favicon, bouton de la facture d'exemple, cinq langues), `deploy/`, README dans les cinq langues, CLAUDE.md. Côté Gradle : dépendance `io.gluonify.plasma:gluonify-plasma-crystallon-client:0.6.0` (`gluonifySdkVersion` dans `gradle.properties`) et dépôt Maven de gluonify-sea, dont l'adresse se remplace par la propriété Gradle `gluonify.sea.maven.url` (`-Pgluonify.sea.maven.url=file:///…`). 86 tests Java (comme le jumeau Maven, contre 34 avant) et 33 tests d'interface (contre 15).
- [ ] **Vérifier le build natif Gradle avec le client du SDK** (`Dockerfile.build`, `./gradlew build -Dquarkus.native.enabled=true …` ; aucun build natif n'a été fait ici) et le producteur `@Startup` de `CrystallonClients`.
- [ ] Remplacer `CrystallonClients.platform` par `CrystallonTokens.platform()` quand un SDK qui l'a est publié (voir `gluonify-source/plan.md`).

## Ce qui diffère exprès du jumeau Maven (à ne pas « resynchroniser »)
Tout le reste doit être identique (`diff -r` en excluant `build/`, `target/`, `node_modules/`, `.git/`, `.gradle/`) : `src/main/java`, `src/main/webui`, `deploy/`, `gluonify.json`, tous les tests sauf `ConformityTest`.
- Construction : `build.gradle.kts`, `settings.gradle.kts`, `gradle.properties` (versions, `gluonifySdkVersion`, `gluonify.sea.maven.url`), `gradlew*`, `gradle/wrapper/` (9.8.1) à la place de `pom.xml` ; `Dockerfile.build`, `.dockerignore`, `.gitignore` ; `scripts/rename.py` (lit `build.gradle.kts` et `settings.gradle.kts`).
- `ConformityTest` : lit `build.gradle.kts` et `gradle.properties` au lieu de `pom.xml`.
- Quatre commentaires ou messages qui nomment la commande : `DevAuthentication` et `ForwardedToken` (`./gradlew quarkusDev` au lieu de `mvn quarkus:dev`), `application.properties` et `App.test.js` (`./gradlew test` / `./gradlew build` au lieu de `mvn test` / `mvn package`).
- `README*.md`, `CLAUDE.md`, `plan.md` : mêmes textes, avec les commandes Gradle, l'extrait de dépendance en Kotlin DSL et la propriété Gradle `gluonify.sea.maven.url` à la place de la propriété Maven.
