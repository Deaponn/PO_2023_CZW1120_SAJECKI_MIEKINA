package agh.ics.oop.resource;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class Resources {
    public static Optional<URL> getResourceURL(Class<?> receiver, String path) {
        return Optional.ofNullable(receiver.getResource(path));
    }

    public static URL tryGetResourceURL(Class<?> receiver, String path)
            throws ResourceNotFoundException {
        Optional<URL> optionalURL = Resources.getResourceURL(receiver, path);
        return optionalURL.orElseThrow(() -> new ResourceNotFoundException(receiver, path));
    }

    public static Optional<InputStream> getResourceAsStream(Class<?> receiver, String path) {
        return Optional.ofNullable(receiver.getResourceAsStream(path));
    }

    public static InputStream tryGetResourceAsStream(Class<?> receiver, String path)
            throws ResourceNotFoundException {
        Optional<InputStream> optionalStream = Resources.getResourceAsStream(receiver, path);
        return optionalStream.orElseThrow(() -> new ResourceNotFoundException(receiver, path));
    }

    public static Optional<InputStream> getFileAsStream(String path) {
        try {
            InputStream fileStream = new FileInputStream(path);
            return Optional.of(fileStream);
        } catch (FileNotFoundException e) {
            return Optional.empty();
        }
    }

    public static InputStream tryGetFileAsStream(String path)
            throws ResourceNotFoundException {
        Optional<InputStream> optionalStream = Resources.getFileAsStream(path);
        return optionalStream.orElseThrow(() -> new ResourceNotFoundException(path));
    }

    public static Stream<File> listFilesAtPath(String parentPath, FileFilter fileFilter)
            throws ResourceNotFoundException {
        File parentFile = new File(parentPath);
        if (!parentFile.exists())
            throw new ResourceNotFoundException(parentPath);

        File[] files = parentFile.listFiles(fileFilter);
        if (files == null)
            return Stream.empty();

        return Arrays.stream(files);
    }

    public static Stream<Map.Entry<String, InputStream>> listFilesAtPathAsNamedStream(
            String parentPath,
            FileFilter fileFilter,
            Function<File, String> keyMapper)
            throws ResourceNotFoundException {
        return Resources.listFilesAtPath(parentPath, fileFilter)
                .map(file -> getNamedInputStreamFromFile(file, keyMapper.apply(file)))
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    private static Optional<InputStream> getInputStreamFromFile(File file) {
        try {
            return Optional.of(Files.newInputStream(file.toPath()));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    private static Optional<Map.Entry<String, InputStream>> getNamedInputStreamFromFile(File file, String name) {
        return Resources.getInputStreamFromFile(file)
                .map(inputStream -> Map.entry(name, inputStream));
    }
}
