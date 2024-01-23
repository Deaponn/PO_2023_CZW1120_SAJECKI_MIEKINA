package agh.ics.oop.resource;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public abstract class Exporter<T> {
    protected final List<T> objectList;
    private final List<Field> objectFieldList;

    public Exporter(Class<T> objectClass) {
        this.objectFieldList = Arrays.stream(objectClass.getFields())
                .filter(Exporter::hasExportedAnnotation)
                .toList();
        this.objectList = new LinkedList<>();
    }

    public Exporter<T> exportToOutputStream(OutputStream outputStream) throws IOException {
        this.writeRepresentation(outputStream);
        return this;
    }

    public Exporter<T> exportToPath(Path path) throws ResourceNotFoundException {
        String filePath = path.toAbsolutePath().toString();
        try (OutputStream outputStream = new FileOutputStream(filePath)) {
            return this.exportToOutputStream(outputStream);
        } catch (IOException e) {
            throw new ResourceNotFoundException(filePath);
        }
    }

    public Exporter<T> exportToURL(URL url) throws ResourceNotFoundException {
        try (OutputStream outputStream = url.openConnection().getOutputStream()) {
            return this.exportToOutputStream(outputStream);
        } catch (IOException e) {
            throw new ResourceNotFoundException(url.getFile());
        }
    }

    public Exporter<T> pushObject(T object) {
        this.objectList.add(object);
        System.out.println("object list size " + this.objectList.size());
        return this;
    }

    public Exporter<T> pushObjects(Collection<T> objects) {
        this.objectList.addAll(objects);
        return this;
    }

    private static boolean hasExportedAnnotation(Field field) {
        return field.isAnnotationPresent(Exported.class);
    }

    protected static String getExportedFieldName(Field field) {
        String originalName = field.getName();
        Exported exportedAnnotation = field.getAnnotation(Exported.class);
        String overrideName = exportedAnnotation.name();

        return overrideName.isEmpty() ? originalName : overrideName;
    }

    private Object getFieldValue(T object, Field field) {
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    protected Stream<Object> getExportedFieldValueStream(T object) {
        return this.objectFieldList.stream()
                .map(field -> this.getFieldValue(object, field));

    }

    protected Stream<String> getExportedFieldNameStream() {
        return this.objectFieldList.stream()
                .map(Exporter::getExportedFieldName);
    }

    protected abstract void writeRepresentation(OutputStream outputStream) throws IOException;
}
