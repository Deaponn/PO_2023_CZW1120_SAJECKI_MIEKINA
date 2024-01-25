package agh.ics.oop.windowx.input;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.util.LinkedList;
import java.util.List;

public abstract class DialogMediator<I>
        implements InputChangeEmitter<InputField<?, I>, InputEvent<InputField<?, I>>> {
    public abstract <V> void emitValue(InputField<V, I> inputField, V previousValue, V newValue);

    private final List<InputChangeListener<InputEvent<InputField<?, I>>>> listenerList;

    public DialogMediator() {
        this.listenerList = new LinkedList<>();
    }

    @Override
    public void inputChangeSubscribe(InputChangeListener<InputEvent<InputField<?, I>>> listener) {
        this.listenerList.add(listener);
    }

    @Override
    public void inputChangeUnsubscribe(InputChangeListener<InputEvent<InputField<?, I>>> listener) {
        this.listenerList.remove(listener);
    }

    @Override
    public void inputChangeNotify(InputEvent<InputField<?, I>> inputEvent) {
        this.listenerList.forEach(listener -> listener.inputChanged(inputEvent));
    }

    public void addStringField(I key, TextField textField, String initialValue) {
        new StringInputField<>(key, textField,
                initialValue, this);
    }

    public void addIntegerField(I key, TextField textField, Integer initialValue) {
        new IntegerInputField<>(key, textField,
                initialValue, this);
    }

    public void addFloatField(I key, TextField textField, Float initialValue) {
        new FloatInputField<>(key, textField,
                initialValue, this);
    }

    public  <T extends Enum<T>> void addEnumField(
            I key,
            ChoiceBox<T> choiceBox,
            Class<T> enumClass,
            T initialValue) {
        new EnumInputField<>(key, choiceBox, enumClass,
                initialValue, this);
    }

    public void addBooleanField(I key, CheckBox checkBox, Boolean initialValue) {
        new BooleanInputField<>(key, checkBox,
                initialValue, this);
    }

}
