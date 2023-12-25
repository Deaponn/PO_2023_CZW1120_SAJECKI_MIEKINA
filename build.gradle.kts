plugins {
    id("java")
    id("application")
    id("org.openjfx.javafxplugin").version("0.0.13")
}

group = "agh.ics.oop"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

javafx {
    version = "21"
    modules(
        "javafx.base",
        "javafx.controls",
        "javafx.fxml",
        "javafx.graphics",
        "javafx.media",
        "javafx.swing",
        "javafx.web"
    )
}

dependencies {
    implementation("junit:junit:4.13.1")
}

application {
    mainClass.set("agh.ics.oop.App")
}

tasks.test {
    useJUnitPlatform()
}