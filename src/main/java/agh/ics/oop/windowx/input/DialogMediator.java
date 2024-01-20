package agh.ics.oop.windowx.input;

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
}
