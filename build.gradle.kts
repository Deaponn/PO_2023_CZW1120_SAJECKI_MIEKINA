plugins {
    id("java")
    id("application")
    id("org.openjfx.javafxplugin").version("0.0.13")
    id("com.github.johnrengelman.shadow").version("8.1.1")
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
    implementation("ja")
    implementation("org.jetbrains:annotations:24.0.0")
    testCompileOnly("junit:junit:4.13.1")
}

sourceSets.test {
    java {
        srcDirs("test")
    }
}

application {
    mainClass.set("agh.ics.oop.Main")
}

tasks.jar {
    manifest {
        from("MANIFEST.MF")
        attributes("Main-Class" to application.mainClass.get())
    }
}

tasks.build {
    copy {
        from("res")
        into("build/libs/res")
    }
}

tasks.test {
    useJUnit()
}