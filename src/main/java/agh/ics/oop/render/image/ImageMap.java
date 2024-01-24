package agh.ics.oop.render.image;

import agh.ics.oop.resource.ResourceNotFoundException;
import agh.ics.oop.resource.Resources;
import javafx.scene.image.Image;

import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImageMap {
    private final Map<String, Image> imageMap;

    public ImageMap() {
        this.imageMap = new HashMap<>();
    }

    public ImageMap(List<String> searchPaths, String extension) throws ResourceNotFoundException {
        this();
        String extensionPattern = "." + extension;
        for (String searchPath : searchPaths) {
            Stream<Map.Entry<String, InputStream>> namedInputStreamStream = Resources.listFilesAtPathAsNamedStream(
                    searchPath,
                    file -> file.getName().contains(extensionPattern),
                    ImageMap::getImageKeyFromFile
            );

            Map<String, InputStream> imageStreamMap = namedInputStreamStream.collect(
                    Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)
            );
            
            this.loadImages(imageStreamMap);
        }
    }

    public void loadImage(String imageKey, Image image) {
        this.imageMap.put(imageKey, image);
    }

    public void loadImage(String imageKey, InputStream inputStream) {
        Image image = new Image(inputStream);
        this.loadImage(imageKey, image);
    }

    public void loadImages(Map<String, InputStream> imageStreamMap) {
        imageStreamMap.forEach(this::loadImage);
    }

    public Image getImage(String imageKey) {
        Image image = this.imageMap.get(imageKey);
        if (image == null)
            throw new NoSuchElementException("ImageMap: key " + imageKey +
                    " does not exist.");
        return image;
    }

    public Set<String> getImageKeys() {
        return this.imageMap.keySet();
    }

    private static String getImageKeyFromFile(File file) {
        if (!file.isFile())
            return null;
        return file.getName().replaceFirst("\\.[^.]+$", "");
    }
}
