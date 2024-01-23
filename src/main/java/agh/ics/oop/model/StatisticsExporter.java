package agh.ics.oop.model;

import agh.ics.oop.resource.CSVExporter;
import agh.ics.oop.resource.Exporter;
import agh.ics.oop.resource.ResourceNotFoundException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class StatisticsExporter implements ObjectEventListener<Statistics> {
    Exporter<Statistics> exporter = new CSVExporter<>(Statistics.class);
    private final String pathToFile;

    public StatisticsExporter(String filename) {
        Path folderPath = Path.of("res/save");
        if (!Files.exists(folderPath.toAbsolutePath()))
            new File("res/save").mkdirs();

        this.pathToFile = "res/save/" + filename + ".csv";
        File saveFile = new File(this.pathToFile);
        try {
            saveFile.createNewFile();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void sendEventData(Statistics load, String message) {
        if (!message.equals("statistics updated")) return;
        this.exporter
                .pushObject(load);
    }

    public void saveToFile() {
        try {
            exporter.exportToPath(Path.of(this.pathToFile));
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
