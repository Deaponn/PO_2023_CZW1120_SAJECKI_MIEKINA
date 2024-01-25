package agh.ics.oop.windowx.input;

import java.util.Map;

public class StringDialogMediator extends DialogMediator<String> {
    private final Map<String, String> paramMap;

    public StringDialogMediator(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    @Override
    public <V> void emitValue(InputField<V, String> inputField,
                              V previousValue, V newValue) {
        String fieldID = inputField.getFieldID();
        if (!fieldID.isEmpty()) {
            this.paramMap.put(fieldID, newValue.toString());
            this.inputChangeNotify(InputEvent.passed(inputField));
        } else {
            this.inputChangeNotify(InputEvent.invalid(inputField));
        }
    }
}
