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
import javax.swing.JButton;

/**
 * ボタンのラッパーです.
 *
 * @author desktopgame
 */
public class ButtonHelper implements Supplier<JButton> {

    private JButton btn;

    public ButtonHelper(String text) {
        this.btn = new JButton(text);
    }

    /**
     * イベントリスナーを追加します.
     *
     * @param listener
     * @return
     */
    public ButtonHelper onPush(ActionListener listener) {
        btn.addActionListener(listener);
        return this;
    }

    @Override
    public JButton get() {
        return btn;
    }

}
