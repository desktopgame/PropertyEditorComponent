/*
 * PropertyEditorComponent
 *
 * Copyright (c) 2020 desktopgame
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package jp.desktopgame.pec.builder;

import java.util.function.Supplier;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;

/**
 *
 * @author desktopgame
 */
public class IntegerSpinnerSupplier implements Supplier<JSpinner> {

    private SpinnerNumberModel model;
    private JSpinner spinner;

    public IntegerSpinnerSupplier() {
        this.model = new SpinnerNumberModel(0, 0, 100, 1);
        this.spinner = new JSpinner(model);
    }

    public IntegerSpinnerSupplier overwrite(int v) {
        model.setValue(v);
        return this;
    }

    public IntegerSpinnerSupplier range(int v, int min, int max, int step) {
        model.setMinimum(min);
        model.setMaximum(max);
        model.setStepSize(step);
        model.setValue(step);
        return this;
    }

    public int current() {
        return (int) model.getValue();
    }

    public IntegerSpinnerSupplier onUpdate(ChangeListener listener) {
        spinner.addChangeListener(listener);
        return this;
    }

    @Override
    public JSpinner get() {
        return spinner;
    }

}
