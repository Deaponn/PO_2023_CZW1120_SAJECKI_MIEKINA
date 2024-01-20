package agh.ics.oop.windowx.input;

import javafx.beans.property.Property;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.FloatStringConverter;

public class FloatInputField<I> extends InputField<Float, I> {
    private final TextField textField;
    private final TextFormatter<Float> formatter;

    public FloatInputField(I fieldID,
                           TextField textField,
                           Float initialValue,
                           DialogMediator<I> fieldMediator) {
        super(fieldID, fieldMediator);
        this.textField = textField;
        this.formatter = new TextFormatter<>(new FloatStringConverter());
        textField.setTextFormatter(this.formatter);
        this.setValue(initialValue);
        this.createEmitter();
    }

    @Override
    protected Property<Float> createValueProperty() {
        return this.formatter.valueProperty();
    }

    @Override
    public Float getValue() {
        return this.formatter.getValue();
    }

    @Override
    public void setValue(Float value) {
        this.textField.setText(value.toString());
        this.formatter.setValue(value);
    }
}
