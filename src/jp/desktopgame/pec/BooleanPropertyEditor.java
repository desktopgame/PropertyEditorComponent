/*
 * PropertyEditorComponent
 *
 * Copyright (c) 2020 desktopgame
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package jp.desktopgame.pec;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBox;
import javax.swing.JComponent;

/**
 * 真偽値を編集するためのエディターの実装です.
 *
 * @author desktopgame
 */
public class BooleanPropertyEditor extends AbstractPropertyEditor implements ActionListener {

    private JCheckBox checkBox;

    public BooleanPropertyEditor() {
        this.checkBox = new JCheckBox("");
        checkBox.addActionListener(this);
    }

    @Override
    public void update(Object value) {
        beginIgnoreEvents();
        checkBox.setSelected((boolean) value);
        endIgnoreEvents();
    }

    @Override
    public Object getInputValue() {
        return checkBox.isSelected();
    }

    @Override
    public JComponent getComponent() {
        return checkBox;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        firePropertyInput();
    }

}
