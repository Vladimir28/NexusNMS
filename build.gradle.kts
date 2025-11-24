plugins {
    java
    application
    id("org.jetbrains.kotlin.jvm") version "1.9.23"
    id("org.javamodularity.moduleplugin") version "1.8.12"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "2.25.0"
}

group = "org.vltes"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val junitVersion = "5.10.2"


tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainModule.set("org.vltes.nexusnms")
    mainClass.set("org.vltes.nexusnms.HelloApplication")
}
kotlin {
    jvmToolchain( 17 )
}

javafx {
    version = "17.0.6"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.web", "javafx.swing" , "javafx.media")
}

dependencies {
    implementation("org.jline:jline-terminal:3.26.0")
    implementation("org.jline:jline-reader:3.26.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:1.8.1")
    implementation("com.hierynomus:sshj:0.39.0")
    implementation("org.kordamp.ikonli:ikonli-javafx:12.3.1")
    implementation("org.controlsfx:controlsfx:11.2.1")
    implementation("com.dlsc.formsfx:formsfx-core:11.6.0") {
      exclude(group = "org.openjfx")
    }
    implementation("org.kordamp.bootstrapfx:bootstrapfx-core:0.4.0")
    implementation("eu.hansolo:tilesfx:17.1.31") {
        exclude(group = "org.openjfx")
    }
    implementation("com.github.almasb:fxgl:17.3") {
        exclude(group = "org.openjfx")
        exclude(group = "org.jetbrains.kotlin")
    }
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jlink {
    imageZip.set(layout.buildDirectory.file("/distributions/app-${javafx.platform.classifier}.zip"))
    options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
    launcher {
        name = "app"
    }
}
