package agh.ics.oop.render;

import javafx.scene.image.Image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ImageMap {
    private final Map<String, Image> imageMap;

    public ImageMap() {
        this.imageMap = new HashMap<>();
    }

    public ImageMap(Map<String, String> imagePathMap, String parentPath) {
        this.imageMap = new HashMap<>();
        this.loadImages(imagePathMap, parentPath);
    }

    public ImageMap(Map<String, String> imagePathMap) {
        this(imagePathMap, "");
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
}
