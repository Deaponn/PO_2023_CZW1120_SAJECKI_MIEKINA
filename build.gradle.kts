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
    testCompileOnly("junit:junit:4.13.1")
}

sourceSets.test {
    java {
        srcDirs("test")
    }
}

application {
    mainClass.set("agh.ics.oop.App")
}

tasks.test {
    useJUnit()
}