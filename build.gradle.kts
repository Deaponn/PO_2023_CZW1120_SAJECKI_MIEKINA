plugins {
    id("java")
    id("application")
    id("org.openjfx.javafxplugin").version("0.0.13")
    id("org.beryx.jlink").version("2.26.0")
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
    implementation("org.jetbrains:annotations:24.0.0")
    testCompileOnly("junit:junit:4.13.1")
}

sourceSets.test {
    java {
        srcDirs("test")
    }
}

application {
    mainModule.set("Project.main")
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
    copy {
        from("res")
        into("build/image/bin/res")
    }
}

tasks.test {
    useJUnit()
}

jlink {
    launcher {
        name = "PO_2023_CZW1120_SAJECKI_MIEKINA"
    }
}