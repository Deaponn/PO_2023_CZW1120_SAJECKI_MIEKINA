package agh.ics.oop.resource;

public class ResourceNotFoundException extends Exception {
    public ResourceNotFoundException(Class<?> receiver, String path) {
        super("resource " + path + " (Cannot be found in any derived package of " +
                receiver.getPackageName() + " with class loader of " +
                receiver.getCanonicalName() + ".)");
    }

    public ResourceNotFoundException(String path) {
        super("file " + path + " (Cannot be found.)");
    }
}