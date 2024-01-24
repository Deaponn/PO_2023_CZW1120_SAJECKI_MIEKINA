package agh.ics.oop.resource;

import agh.ics.oop.util.Reactive;

public class ReactiveTestObject {
    @Exported()
    public Reactive<Long> id;
    @Exported(name = "content")
    public Reactive<String> text;

    public ReactiveTestObject(long id, String text) {
        this.id = new Reactive<>(id);
        this.text = new Reactive<>(text);
    }
}
