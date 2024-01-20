package agh.ics.oop.windowx.input;

import javafx.beans.property.Property;

public abstract class InputField<V, I> {
    private final I fieldID;
    private final DialogMediator<I> dialogMediator;

    public InputField(I fieldID, DialogMediator<I> dialogMediator) {
        this.fieldID = fieldID;
        this.dialogMediator = dialogMediator;
    }

    protected void createEmitter() {
        this.createValueProperty()
                .addListener((observable, previousValue, newValue) ->
                        this.emitValue(previousValue, newValue));
    }

    protected abstract Property<V> createValueProperty();

    public void emitValue(V previousValue, V newValue) {
        this.dialogMediator.emitValue(this, previousValue, newValue);
    }

    public I getFieldID() {
        return this.fieldID;
    }

    public abstract V getValue();

    public abstract void setValue(V value);
}
