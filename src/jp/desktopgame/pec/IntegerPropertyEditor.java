/*
 * PropertyEditorComponent
 *
 * Copyright (c) 2020 desktopgame
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package jp.desktopgame.pec;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * 整数型を編集するためのエディターの実装です.
 *
 * @author desktopgame
 */
public class IntegerPropertyEditor extends AbstractPropertyEditor implements ChangeListener {

    private JSpinner spinner;

    public IntegerPropertyEditor() {
        this(-999999, 999999, 1);
    }

    public IntegerPropertyEditor(int min, int max, int step) {
        this.spinner = new JSpinner(new SpinnerNumberModel(min, min, max, step));
        spinner.addChangeListener(this);
    }

    @Override
    public void update(Object value) {
        beginIgnoreEvents();
        spinner.setValue(value);
        endIgnoreEvents();
    }

    @Override
    public Object getInputValue() {
        return spinner.getValue();
    }

    @Override
    public JComponent getComponent() {
        return spinner;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        firePropertyInput();
    }

}
