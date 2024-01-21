package agh.ics.oop.resource;

public class TestObject {
    @Exported(name = "integer")
    public int paramInteger;
    @Exported(name = "float")
    public float paramFloat;
    @Exported
    public final String name;

    public TestObject(int paramInteger, float paramFloat, String name) {
        this.paramInteger = paramInteger;
        this.paramFloat = paramFloat;
        this.name = name;
    }
}
