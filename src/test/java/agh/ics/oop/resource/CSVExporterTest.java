package agh.ics.oop.resource;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class CSVExporterTest {
    @Test
    public void exportTestObjects() {
        Exporter<TestObject> exporter = new CSVExporter<>(TestObject.class);
        exporter
                .pushObject(new TestObject(2, 3, "abc"))
                .pushObject(new TestObject(4, 5, "def"))
                .pushObject(new TestObject(7, 8, "ghi"));
        try {
            exporter.exportToOutputStream(System.out);
        } catch (IOException e) {
            Assert.fail("could not export: " + e.getMessage());
        }

        System.out.flush();
        Assert.assertTrue(true);
    }
}
