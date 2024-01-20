package agh.ics.oop.windowx.input;

import agh.ics.oop.Configuration;

public class ConfigurationDialogMediator extends DialogMediator<Configuration.Fields> {
    private final Configuration configuration;

    public ConfigurationDialogMediator(Configuration configuration) {
        super();
        this.configuration = configuration;
    }

    @Override
    public <V> void emitValue(InputField<V, Configuration.Fields> inputField,
                              V previousValue, V newValue) {
        Configuration.Fields fieldKey = inputField.getFieldID();
        try {
            this.configuration.set(fieldKey, newValue);
            this.inputChangeNotify(InputEvent.passed(inputField));
        } catch (IllegalArgumentException e) {
            inputField.setValue(previousValue);
            this.inputChangeNotify(InputEvent.invalid(inputField));
        }
    }
}
