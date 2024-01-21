package agh.ics.oop.resource;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

public class CSVExporter<T> extends Exporter<T> {
    public CSVExporter(Class<T> objectClass) {
        super(objectClass);
    }

    @Override
    protected void writeRepresentation(OutputStream outputStream) throws IOException {
        outputStream.write(this.getHeader().getBytes(StandardCharsets.UTF_8));
        for (T object : this.objectList) {
            String row = this.getEntryRow(object);
            outputStream.write(row.getBytes(StandardCharsets.UTF_8));
        }
    }

    private String getHeader() {
        return this.formatRow(this.getExportedFieldNameStream());
    }

    private String getEntryRow(T object) {
        return this.formatRow(this.getExportedFieldValueStream(object));
    }

    private String formatRow(Stream<?> elementStream) {
        List<String> elementStringList = elementStream
                .map(Object::toString)
                .toList();

        return String.join(",", elementStringList) + "\n";
    }
}
