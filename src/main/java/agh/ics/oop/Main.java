package agh.ics.oop;

import agh.ics.oop.model.EquatorialWorldMap;

public class Main {
    public static void main(String[] args) {
        Configuration conf = new Configuration();
        EquatorialWorldMap map = new EquatorialWorldMap(conf);
        App.run(args);
    }
}
