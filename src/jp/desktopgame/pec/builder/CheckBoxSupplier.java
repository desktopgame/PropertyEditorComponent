/*
 * PropertyEditorComponent
 *
 * Copyright (c) 2020 desktopgame
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package jp.desktopgame.pec.builder;

import java.awt.event.ActionListener;
import java.util.function.Supplier;
import javax.swing.JCheckBox;

/**
 *
 * @author desktopgame
 */
public class CheckBoxSupplier implements Supplier<JCheckBox> {

    private JCheckBox checkBox;

    public CheckBoxSupplier() {
        this.checkBox = new JCheckBox();
    }

    public CheckBoxSupplier overwrite(boolean b) {
        checkBox.setSelected(b);
        return this;
    }

    public boolean check() {
        return checkBox.isSelected();
    }

    public CheckBoxSupplier onUpdate(ActionListener listener) {
        checkBox.addActionListener(listener);
        return this;
    }

    @Override
    public JCheckBox get() {
        return checkBox;
    }

}
