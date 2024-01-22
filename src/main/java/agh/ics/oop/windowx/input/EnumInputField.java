package agh.ics.oop.windowx.input;

import javafx.beans.property.Property;
import javafx.scene.control.ChoiceBox;

public class EnumInputField<E extends Enum<E>, I> extends InputField<E, I> {
    private final ChoiceBox<E> choiceBox;

    public EnumInputField(I fieldID,
                          ChoiceBox<E> choiceBox,
                          Class<E> enumClass,
                          E initialValue,
                          DialogMediator<I> fieldMediator) {
        super(fieldID, fieldMediator);
        this.choiceBox = choiceBox;
        this.choiceBox.getItems().addAll(enumClass.getEnumConstants());
        this.setValue(initialValue);
        this.createEmitter();
    }

    @Override
    protected Property<E> createValueProperty() {
        return this.choiceBox.valueProperty();
    }

    @Override
    public E getValue() {
        return this.choiceBox.getValue();
    }

    @Override
    public void setValue(E value) {
        this.choiceBox.setValue(value);
    }
}
