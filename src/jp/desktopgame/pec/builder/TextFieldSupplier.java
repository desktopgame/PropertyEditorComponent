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
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author desktopgame
 */
public class TextFieldSupplier implements Supplier<JTextField> {

    private JTextField textField;

    public TextFieldSupplier() {
        this.textField = new JTextField();
    }

    public TextFieldSupplier overwrite(String text) {
        textField.setText(text);
        return this;
    }

    public String text() {
        return textField.getText();
    }

    public TextFieldSupplier move(int i) {
        textField.setCaretPosition(i);
        return this;
    }

    public int caret() {
        return textField.getCaretPosition();
    }

    public TextFieldSupplier onUpdate(ChangeListener listener) {
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent arg0) {
                listener.stateChanged(new ChangeEvent(textField));
            }

            @Override
            public void removeUpdate(DocumentEvent arg0) {
                listener.stateChanged(new ChangeEvent(textField));
            }

            @Override
            public void changedUpdate(DocumentEvent arg0) {
                listener.stateChanged(new ChangeEvent(textField));
            }
        });
        return this;
    }

    @Override
    public JTextField get() {
        return textField;
    }
}
