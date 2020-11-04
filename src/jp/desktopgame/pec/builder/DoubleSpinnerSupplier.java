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
public class DoubleSpinnerSupplier implements Supplier<JSpinner> {

    private SpinnerNumberModel model;
    private JSpinner spinner;

    public DoubleSpinnerSupplier() {
        this.model = new SpinnerNumberModel(0.0, 0.0, 100.0, 1.0);
        this.spinner = new JSpinner(model);
    }

    public DoubleSpinnerSupplier overwrite(double v) {
        model.setValue(v);
        return this;
    }

    public DoubleSpinnerSupplier range(double v, double min, double max, double step) {
        model.setMinimum(min);
        model.setMaximum(max);
        model.setStepSize(step);
        model.setValue(step);
        return this;
    }

    public double current() {
        return (double) model.getValue();
    }

    public DoubleSpinnerSupplier onUpdate(ChangeListener listener) {
        spinner.addChangeListener(listener);
        return this;
    }

    @Override
    public JSpinner get() {
        return spinner;
    }
}
