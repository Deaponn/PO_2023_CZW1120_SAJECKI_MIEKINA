package agh.ics.oop.render.image;

import agh.ics.oop.model.Vector2D;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class ImageSamplerMap {
    private final ImageMap imageMap;

    private final Map<String, ImageSampler> imageSamplerMap;
    private final Map<String, ImageAtlasSampler> imageAtlasSamplerMap;

    public ImageSamplerMap(ImageMap imageMap) {
        this.imageMap = imageMap;

        this.imageSamplerMap = new HashMap<>();
        this.imageAtlasSamplerMap = new HashMap<>();

        this.imageMap.getImageKeys().forEach(this::addDefaultImageSampler);
    }

    public void addDefaultImageSampler(String imageKey) {
        this.addImageSampler(imageKey, imageKey, ImageSampler::new);
    }

    public void addImageSampler(
            String imageKey,
            String samplerKey,
            Function<Image, ImageSampler> samplerFunction) {
        Image image = this.imageMap.getImage(imageKey);
        if (this.imageSamplerMap.containsKey(samplerKey))
            throw new IllegalArgumentException("ImageSamplerMap: key " + samplerKey +
                    " already exists.");
        this.imageSamplerMap.put(samplerKey, samplerFunction.apply(image));
    }

    public void addImageAtlasSampler(
            String imageKey,
            String samplerKey,
            Function<Image, ImageAtlasSampler> samplerFunction) {
        Image image = this.imageMap.getImage(imageKey);
        if (this.imageAtlasSamplerMap.containsKey(samplerKey))
            throw new IllegalArgumentException("ImageSamplerMap: key " + samplerKey +
                    " already exists.");
        this.imageAtlasSamplerMap.put(samplerKey, samplerFunction.apply(image));
    }

    public void addFontAtlasSampler(
            String imageKey,
            String samplerKey,
            Vector2D glyphSize) {
        this.addImageAtlasSampler(imageKey, samplerKey,
                (image) -> new ImageAtlasSampler(image, glyphSize));
    }

    public ImageSampler getImageSampler(String samplerKey) {
        ImageSampler imageSampler = this.imageSamplerMap.get(samplerKey);
        if (imageSampler == null)
            throw new NoSuchElementException("ImageSamplerMap: key " + samplerKey +
                    " does not exist.");
        return imageSampler;
    }

    public ImageAtlasSampler getImageAtlasSampler(String samplerKey) {
        ImageAtlasSampler imageAtlasSampler = this.imageAtlasSamplerMap.get(samplerKey);
        if (imageAtlasSampler == null)
            throw new NoSuchElementException("ImageSamplerMap: key " + samplerKey +
                    " does not exist.");
        return imageAtlasSampler;
    }
}
