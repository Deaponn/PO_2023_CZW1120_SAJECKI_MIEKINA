package agh.ics.oop.resource;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class Resources {
    public static Optional<URL> getResourceURL(Class<?> receiver, String path) {
        return Optional.ofNullable(receiver.getResource(path));
    }

    public static Optional<InputStream> getResourceInputStream(Class<?> receiver, String path) {
        return Optional.ofNullable(receiver.getResourceAsStream(path));
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

    public static Object deserializeFromXML(String path)
            throws ResourceNotFoundException {
        try (XMLDecoder xmlDecoder = new XMLDecoder(Files.newInputStream(Path.of(path)))) {
            return xmlDecoder.readObject();
        } catch (IOException e) {
            throw new ResourceNotFoundException(path);
        }
    }

    public static void serializeToXML(String path, Object object)
            throws ResourceNotFoundException {
        try (XMLEncoder xmlEncoder = new XMLEncoder(Files.newOutputStream(Path.of(path)))) {
            xmlEncoder.writeObject(object);
        } catch (IOException e) {
            throw new ResourceNotFoundException(path);
        }
    }
}
