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
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * 文字列を編集するためのエディターの実装です.
 *
 * @author desktopgame
 */
public class StringPropertyEditor extends AbstractPropertyEditor implements DocumentListener {

    private JTextField textField;

    public StringPropertyEditor() {
        this.textField = new JTextField();
        textField.getDocument().addDocumentListener(this);
    }

    @Override
    public void update(Object value) {
        if (value == null) {
            value = "";
        }
        beginIgnoreEvents();
        textField.setText(value instanceof String ? (String) value : value.toString());
        endIgnoreEvents();
    }

    @Override
    public Object getInputValue() {
        return textField.getText();
    }

    @Override
    public JComponent getComponent() {
        return textField;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        firePropertyInput();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        firePropertyInput();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        firePropertyInput();
    }

}
