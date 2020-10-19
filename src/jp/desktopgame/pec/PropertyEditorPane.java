/*
 * PropertyEditorComponent
 *
 * Copyright (c) 2020 desktopgame
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package jp.desktopgame.pec;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * オブジェクトのプロパティを編集するためのエディター画面を構築するためのクラスです.
 * このクラスを継承して、addLine()を編集項目の数だけ呼び出したのちaddFooter()を呼び出すことでフォームが作成されます。
 *
 * @author desktopgame
 */
public class PropertyEditorPane extends JPanel {

    private GridBagLayout gbl;
    private GridBagConstraints gbc;

    public PropertyEditorPane() {
        this.gbl = new GridBagLayout();
        this.gbc = new GridBagConstraints();
        setLayout(gbl);
    }

    /**
     * フッターを追加して上部の余白を潰します. 全てのaddLine()呼び出しが終わった後で呼び出してください。
     */
    protected void addFooter() {
        addFooter(new JPanel());
    }

    /**
     * フッターを追加して上部の余白を潰します.全てのaddLine()呼び出しが終わった後で呼び出してください。
     *
     * @param footer
     */
    protected void addFooter(Component footer) {
        gbc.weightx = gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        putComponent(footer);
    }

    /**
     * ラベルとエディターコンポーネントの二列で構成される行を追加します.
     *
     * @param label
     * @param c
     */
    protected void addLine(String label, Component c) {
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.NONE;
        putComponent(new JLabel(label));
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        putComponent(c);
    }

    /**
     * 対象のコンポーネント数で等分割される行を追加します.
     *
     * @param cc
     */
    protected void addLine(Component... cc) {
        for (int i = 0; i < cc.length; i++) {
            if (i != cc.length - 1) {
                gbc.gridwidth = 1;
                gbc.weightx = 1;
                gbc.fill = GridBagConstraints.NONE;
            } else {
                gbc.gridwidth = GridBagConstraints.REMAINDER;
            }
            putComponent(cc[i]);
        }
    }

    /**
     * 横幅いっぱいに広がるコンポーネントを一行として追加します.
     *
     * @param c
     */
    protected void addLine(Component c) {
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        putComponent(c);
    }

    private void putComponent(Component c) {
        gbl.setConstraints(c, gbc);
        this.add(c);
    }
}
