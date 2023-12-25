package agh.ics.oop.render;

import javafx.scene.image.Image;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ImageMap {
    private final Map<String, Image> imageMap;

    public ImageMap() {
        this.imageMap = new HashMap<>();
    }

    public ImageMap(Map<String, String> imagePathMap, String parentPath) {
        this.imageMap = new HashMap<>();
        this.loadImages(imagePathMap, parentPath);
    }

    public ImageMap(String searchPath, String extension) {
        this();

        File searchFile = new File(searchPath);
        String extensionPattern = "." + extension;
        File[] imageFiles = searchFile.listFiles(
                pathname -> pathname.isFile() && pathname.getName().contains(extensionPattern)
        );

        if (imageFiles != null) {
            Map<String, String> imagePathMap = Arrays.stream(imageFiles)
                    .collect(Collectors.toMap(ImageMap::getImageID, File::getName));
            this.loadImages(imagePathMap, searchPath);
        }
    }

    public void loadImage(String imageKey, Image image) {
        this.imageMap.put(imageKey, image);
    }

    public void loadImage(String imageKey, String relativePath, String parentPath) {
        Path path = Path.of(parentPath, relativePath);
        try {
            this.loadImage(imageKey, new Image(Files.newInputStream(path)));
        } catch (IOException e) {
            throw new RuntimeException("image '" + relativePath + "' could not be loaded");
        }
    }

    public void loadImages(Map<String, String> imagePathMap, String parentPath) {
        imagePathMap.forEach((key, value) -> this.loadImage(key, value, parentPath));
    }

    public Image getImage(String imageKey) {
        return this.imageMap.get(imageKey);
    }

    private static String getImageID(File file) {
        if (!file.isFile())
            return null;
        return file.getName().replaceFirst("\\.[^.]+$", "");
    }
}
