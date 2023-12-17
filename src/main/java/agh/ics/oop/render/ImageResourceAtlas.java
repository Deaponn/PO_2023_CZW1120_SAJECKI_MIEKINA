package agh.ics.oop.render;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ImageResourceAtlas {
    private final Map<String, Image> imageMap;

    public ImageResourceAtlas() {
        this.imageMap = new HashMap<>();
    }

    public ImageResourceAtlas(Map<String, String> imagePathMap, String parentPath) {
        this.imageMap = new HashMap<>();
        this.loadImages(imagePathMap, parentPath);
    }

    public ImageResourceAtlas(Map<String, String> imagePathMap) {
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
            throw new RuntimeException("Image '" + relativePath + "' could not be loaded");
        }
    }

    public void loadImages(Map<String, String> imagePathMap, String parentPath) {
        imagePathMap.forEach((key, value) -> this.loadImage(key, value, parentPath));
    }

    public Image getImage(String imageKey) {
        return this.imageMap.get(imageKey);
    }

    public ImageView getImageView(String imageKey) {
        return new ImageView(this.getImage(imageKey));
    }
}
