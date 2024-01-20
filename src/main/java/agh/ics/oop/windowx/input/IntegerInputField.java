package agh.ics.oop.windowx.input;

import javafx.beans.property.Property;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

public class IntegerInputField<I> extends InputField<Integer, I> {
    private final TextField textField;
    private final TextFormatter<Integer> formatter;

    public IntegerInputField(I fieldID,
                             TextField textField,
                             Integer initialValue,
                             DialogMediator<I> fieldMediator) {
        super(fieldID, fieldMediator);
        this.textField = textField;
        this.formatter = new TextFormatter<>(new IntegerStringConverter());
        textField.setTextFormatter(this.formatter);
        this.setValue(initialValue);
        this.createEmitter();
    }

    @Override
    protected Property<Integer> createValueProperty() {
        return this.formatter.valueProperty();
    }

    @Override
    public Integer getValue() {
        return this.formatter.getValue();
    }

    @Override
    public void setValue(Integer value) {
        this.textField.setText(value.toString());
        this.formatter.setValue(value);
    }
}
