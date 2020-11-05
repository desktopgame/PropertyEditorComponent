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
 * テキストフィールドのラッパーです.
 *
 * @author desktopgame
 */
public class TextFieldSupplier implements Supplier<JTextField> {

    private JTextField textField;

    public TextFieldSupplier() {
        this.textField = new JTextField();
    }

    /**
     * 現在のテキストを上書きします.
     *
     * @param text
     * @return
     */
    public TextFieldSupplier overwrite(String text) {
        textField.setText(text);
        return this;
    }

    /**
     * 入力されているテキストを返します.
     *
     * @return
     */
    public String text() {
        return textField.getText();
    }

    /**
     * カレットを移動します.
     *
     * @param i
     * @return
     */
    public TextFieldSupplier move(int i) {
        textField.setCaretPosition(i);
        return this;
    }

    /**
     * カレットの位置を返します.
     *
     * @return
     */
    public int caret() {
        return textField.getCaretPosition();
    }

    /**
     * イベントリスナーを追加します.
     *
     * @param listener
     * @return
     */
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
