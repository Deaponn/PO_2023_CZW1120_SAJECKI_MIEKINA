package agh.ics.oop.windowx.input;

import javafx.beans.property.Property;
import javafx.scene.control.TextField;

public class StringInputField<I> extends InputField<String, I> {
    private final TextField textField;

    public StringInputField(I fieldID,
                             TextField textField,
                             String initialValue,
                             DialogMediator<I> fieldMediator) {
        super(fieldID, fieldMediator);
        this.textField = textField;
        this.setValue(initialValue);
        this.createEmitter();
    }

    @Override
    protected Property<String> createValueProperty() {
        return this.textField.textProperty();
    }

    @Override
    public String getValue() {
        return this.textField.getText();
    }

    @Override
    public void setValue(String value) {
        this.textField.setText(value);
        this.textField.setText(value);
    }
}
