/*
 * PropertyEditorComponent
 *
 * Copyright (c) 2020 desktopgame
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package jp.desktopgame.pec.builder;

import java.awt.Component;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import jp.desktopgame.pec.PropertyEditorDialog;
import jp.desktopgame.pec.PropertyEditorPane;

/**
 *
 * @author desktopgame
 */
public class PropertyEditorBuilder {

    private List<AbstractMap.SimpleEntry<String, Supplier<? extends Component>>> properties;
    private List<Component> footers;

    public PropertyEditorBuilder() {
        this.properties = new ArrayList<>();
        this.footers = new ArrayList<>();
    }

    public <T> ComboBoxSupplier<T> comboBox(String label) {
        ComboBoxSupplier ret = new ComboBoxSupplier();
        properties.add(new AbstractMap.SimpleEntry<>(label, ret));
        return ret;
    }

    public TextFieldSupplier textField(String label) {
        TextFieldSupplier ret = new TextFieldSupplier();
        properties.add(new AbstractMap.SimpleEntry<>(label, ret));
        return ret;
    }

    public IntegerSpinnerSupplier intSpinner(String label) {
        IntegerSpinnerSupplier ret = new IntegerSpinnerSupplier();
        properties.add(new AbstractMap.SimpleEntry<>(label, ret));
        return ret;
    }

    public DoubleSpinnerSupplier doubleSpinner(String label) {
        DoubleSpinnerSupplier ret = new DoubleSpinnerSupplier();
        properties.add(new AbstractMap.SimpleEntry<>(label, ret));
        return ret;
    }

    public CheckBoxSupplier checkBox(String label) {
        CheckBoxSupplier ret = new CheckBoxSupplier();
        properties.add(new AbstractMap.SimpleEntry<>(label, ret));
        return ret;
    }

    public <T extends Component> T footer(T footer) {
        this.footers.add(footer);
        return footer;
    }

    public PropertyEditorPane buildPane() {
        return new PropertyEditorPane() {
            {
                for (AbstractMap.SimpleEntry<String, Supplier<? extends Component>> kv : properties) {
                    addLine(kv.getKey(), kv.getValue().get());
                }
                for (Component c : footers) {
                    addLine(c);
                }
                addFooter();
            }
        };
    }

    public PropertyEditorDialog buildDialog(String title) {
        return new PropertyEditorDialog() {
            {
                setHiddenApplyButton(true);
                setHiddenCancelButton(true);
                setHiddenOKButton(true);
                setTitle(title);
            }

            @Override
            protected void init() {
                for (AbstractMap.SimpleEntry<String, Supplier<? extends Component>> kv : properties) {
                    addLine(kv.getKey(), kv.getValue().get());
                }
                for (Component c : footers) {
                    addLine(c);
                }
                addFooter();
            }
        };
    }
}
