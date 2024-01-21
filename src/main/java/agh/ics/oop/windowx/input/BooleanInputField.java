package agh.ics.oop.windowx.input;

import javafx.beans.property.Property;
import javafx.scene.control.CheckBox;

public class BooleanInputField<I> extends InputField<Boolean, I> {
    private final CheckBox checkBox;

    public BooleanInputField(I fieldID,
                             CheckBox checkBox,
                             Boolean initialValue,
                             DialogMediator<I> fieldMediator) {
        super(fieldID, fieldMediator);
        this.checkBox = checkBox;
        this.setValue(initialValue);
        this.createEmitter();
    }

    @Override
    protected Property<Boolean> createValueProperty() {
        return this.checkBox.selectedProperty();
    }

    @Override
    public Boolean getValue() {
        return this.checkBox.isSelected();
    }

    @Override
    public void setValue(Boolean value) {
        this.checkBox.setSelected(value);
    }
}
