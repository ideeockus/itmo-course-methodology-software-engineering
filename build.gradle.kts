val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version : String by project
val h2_version : String by project

plugins {
    kotlin("jvm") version "1.8.22"
    id("io.ktor.plugin") version "2.3.1"
                id("org.jetbrains.kotlin.plugin.serialization") version "1.8.22"
}

group = "com.memoryerasureservice"
version = "0.0.1"
//application {
//    mainClass.set("com.example.ApplicationKt")
//
//    val isDevelopment: Boolean = project.ext.has("development")
//    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
//}

configurations {
    all {
        exclude(group = "com.anamnesia", module = "plugins")
    }
}

repositories {
    mavenCentral()
}

dependencies {
//    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
//    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
//    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
//    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
//    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
//    implementation("com.h2database:h2:$h2_version")
    implementation("io.ktor:ktor-server-openapi:$ktor_version")
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktor_version")
//    implementation("io.ktor:ktor-server-host-common-jvm:$ktor_version")
//    implementation("io.ktor:ktor-server-resources:$ktor_version")
//    implementation("io.ktor:ktor-server-sessions-jvm:$ktor_version")
//    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
//    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktor_version")
//    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
//    implementation("ch.qos.logback:logback-classic:$logback_version")
//    implementation("org.xerial:sqlite-jdbc:3.34.0")
//    implementation("org.postgresql:postgresql:42.3.1")
//    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
//    implementation("com.zaxxer:HikariCP:4.0.3")
//    implementation("io.ktor:ktor-server-netty:1.5.4")
////    implementation("io.ktor:ktor-jackson:1.5.4")
//    implementation("org.jetbrains.exposed:exposed-core:0.31.1")
//    implementation("org.jetbrains.exposed:exposed-dao:0.31.1")
//    implementation("org.jetbrains.exposed:exposed-jdbc:0.31.1")
//    implementation("com.zaxxer:HikariCP:4.0.3")
//    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.1.0")
//    implementation("org.postgresql:postgresql:42.2.19")
//    implementation("ch.qos.logback:logback-classic:1.2.3")

    implementation("io.ktor:ktor-server-netty:2.0.0") // Обновите до актуальной версии
    implementation("io.ktor:ktor-server-core:2.0.0")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.0.0")
    implementation("org.jetbrains.exposed:exposed-core:0.35.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.35.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.35.1")
    implementation("com.zaxxer:HikariCP:5.0.0")
    implementation("org.postgresql:postgresql:42.2.23")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    testImplementation("io.ktor:ktor-server-tests:2.0.0")
    implementation("io.ktor:ktor-server-content-negotiation:$$ktor_version")
//    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1")
    implementation("io.ktor:ktor-client-serialization:$$ktor_version")
    implementation("io.ktor:ktor-server-status-pages:$ktor_version")
    implementation("io.ktor:ktor-server-sessions:$ktor_version")
//    implementation("org.postgresql:postgresql:42.2.18")
    implementation("org.jetbrains.exposed:exposed-java-time:0.35.1") // Проверьте версию

    implementation("ch.qos.logback:logback-classic:1.2.3")
    testImplementation("io.ktor:ktor-server-tests:1.5.4")
    implementation("io.ktor:ktor-server-swagger:$ktor_version")
    implementation("org.mindrot:jbcrypt:0.4")

    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
//    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    // build.gradle.kts для Kotlin DSL
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.4.32")
    testImplementation("io.mockk:mockk:1.10.6")
    testImplementation("org.jetbrains.exposed:exposed-core:0.31.1")
    testImplementation("org.jetbrains.exposed:exposed-dao:0.31.1")
    testImplementation("org.jetbrains.exposed:exposed-jdbc:0.31.1")
    testImplementation("com.h2database:h2:1.4.200") // Используем H2 для тестовой БД
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:1.5.10")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.7.0")

}

application {
    mainClass.set("com.memoryerasureservice.ApplicationKt")
}

tasks.test {
    useJUnitPlatform()
}