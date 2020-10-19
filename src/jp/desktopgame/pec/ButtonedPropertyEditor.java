/*
 * PropertyEditorComponent
 *
 * Copyright (c) 2020 desktopgame
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package jp.desktopgame.pec;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * 特定の種類のプロパティにおいて、値の表示と値の編集で異なるコンポーネントを使用する場合に使用できます.
 * このクラスを継承してonActionの内部で編集のためのダイアログを起動できます。
 *
 * @author desktopgame
 */
public class ButtonedPropertyEditor extends AbstractPropertyEditor {

    protected PropertyEditor editor;
    private JPanel panel;

    public ButtonedPropertyEditor(PropertyEditor editor) {
        this.editor = editor;
        this.panel = new JPanel(new BorderLayout());
        JButton button = createButton();
        panel.add(editor.getComponent(), BorderLayout.CENTER);
        panel.add(button, BorderLayout.EAST);
        button.addActionListener(this::onAction);
    }

    protected JButton createButton() {
        return new JButton(Messages.EDIT);
    }

    protected void onAction(ActionEvent e) {
    }

    @Override
    public void update(Object value) {
        editor.update(value);
    }

    @Override
    public Object getInputValue() {
        return editor.getInputValue();
    }

    @Override
    public JComponent getComponent() {
        return panel;
    }
}
