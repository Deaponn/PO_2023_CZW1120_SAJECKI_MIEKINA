package agh.ics.oop;

import agh.ics.oop.model.MapType;
import agh.ics.oop.model.EquatorialWorldMap;

public class Main {
    public static void main(String[] args) {
        Configuration conf = new Configuration(13, 8, MapType.STANDARD, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0.2f, 1, false);
        EquatorialWorldMap map = new EquatorialWorldMap(conf);
    }
}
