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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

/**
 * オブジェクトのプロパティを編集するためのエディター画面をダイアログとして構築するためのクラスです.
 *
 * @author desktopgame
 * @param <T>
 */
public class PropertyEditorDialog<T extends PropertyEditorPane> {

    /**
     * ダイアログの状態です.
     */
    public enum Status {
        /**
         * 最後に行われた変更が適用されずにOKが押された場合.
         * 変更はインスタンスに書き込まれますが、バッキングストアには保存されていない変更があります。
         */
        SAVED,
        /**
         * 最後に行われた変更が適用されたあと、OKか閉じるボタンによって閉じた場合.
         * 変更はインスタンスに書き込まれ、バッキングストアに保存されています。
         */
        APPLIED,
        /**
         * 変更済みでまだOKも適用もされていない状態です. 内部的に使用される状態です。
         * ダイアログが終了した時点ではこの状態になっていることはありません。
         */
        MODIFIED,
        /**
         * 変更が行われたが適用する前に閉じるボタンを押した場合. もしくは何も変更を行わずに閉じた場合。
         */
        CANCELED,
    }

    private T editorPane;
    private boolean bInit;
    private Box buttonBox;
    private JButton okButton, applyButton, cancelButton;
    private boolean hiddenOKButton, hiddenApplyButton, hiddenCancelButton;
    private Status status;
    private String title;

    public PropertyEditorDialog() {
        this.title = Messages.PROPERTY_EDIT_DIALOG;
    }

    /**
     * ダイアログを表示します. 適用ボタンは押せない状態で初期化されます。
     *
     * @param parent
     */
    public void show(JFrame parent) {
        show(parent, false);
    }

    /**
     * ダイアログを表示します.
     *
     * @param parent
     * @param modified trueなら最初から適用ボタンを押せる状態で表示します。
     */
    public void show(JFrame parent, boolean modified) {
        this.status = modified ? Status.MODIFIED : Status.CANCELED;
        doInit();
        applyButton.setEnabled(modified);
        onLoad();
        JDialog dialog = new JDialog(parent);
        dialog.setTitle(getTitle());
        dialog.setLayout(new BorderLayout());
        dialog.add(new JScrollPane(editorPane), BorderLayout.CENTER);
        dialog.add(buttonBox, BorderLayout.SOUTH);
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setPreferredSize(new Dimension(800, 600));
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void doInit() {
        if (!bInit) {
            bInit = true;
            sysInit();
            init();
        }
    }

    private void sysInit() {
        this.editorPane = getEditorPane();
        this.okButton = new JButton(Messages.OK);
        this.applyButton = new JButton(Messages.APPLY);
        this.cancelButton = new JButton(Messages.CLOSE);
        okButton.addActionListener(this::_onOK);
        applyButton.addActionListener(this::_onApply);
        applyButton.setEnabled(false);
        cancelButton.addActionListener(this::_onCancel);
        List<JButton> buttons = new ArrayList<>();
        if (!isHiddenOKButton()) {
            buttons.add(okButton);
        }
        if (!isHiddenApplyButton()) {
            buttons.add(applyButton);
        }
        if (!isHiddenCancelButton()) {
            buttons.add(cancelButton);
        }
        this.buttonBox = LayoutUtilities.createButtonBar(buttons.stream().toArray(JButton[]::new));
    }

    /**
     * ダイアログを初めて表示する時、直前に一度だけ呼び出されます.
     */
    protected void init() {
    }

    /**
     * このダイアログが使用するエディターペインを生成します.
     *
     * @return
     */
    protected T createEditorPane() {
        return (T) new PropertyEditorPane();
    }

    /**
     * ダイアログを表示する前に必ず呼び出されるメソッドです.
     */
    protected void onLoad() {

    }

    private void _onOK(ActionEvent e) {
        if (this.status == Status.MODIFIED || this.status == Status.CANCELED) {
            this.status = Status.SAVED;
        }
        onOK(e);
    }

    /**
     * ユーザがOKボタンを押すと呼び出されます. デフォルトの動作ではダイアログを閉じます。
     *
     * @param e
     */
    protected void onOK(ActionEvent e) {
        getDialog(e).dispose();
    }

    private void _onApply(ActionEvent e) {
        this.status = Status.APPLIED;
        onApply(e);
    }

    /**
     * ユーザが適用ボタンを押すと呼び出されます. デフォルトの動作では何も行いません。
     *
     * @param e
     */
    protected void onApply(ActionEvent e) {
    }

    private void _onCancel(ActionEvent e) {
        if (this.status == Status.MODIFIED) {
            this.status = Status.CANCELED;
        }
        onCancel(e);
    }

    /**
     * ユーザが閉じるボタンを押すと呼び出されます. デフォルトの動作ではダイアログを閉じます。
     *
     * @param e
     */
    protected void onCancel(ActionEvent e) {
        getDialog(e).dispose();
    }

    /**
     * 適用ボタンを押せるようにします.
     */
    protected void statusModified() {
        applyButton.setEnabled(true);
        this.status = Status.MODIFIED;
    }

    /**
     * 適用ボタンを押せなくします.
     */
    protected void statusApplied() {
        applyButton.setEnabled(false);
        this.status = Status.APPLIED;
    }

    /**
     * フッターを追加して上部の余白を潰します. 全てのaddLine()呼び出しが終わった後で呼び出してください。
     */
    protected void addFooter() {
        editorPane.addFooter();
    }

    /**
     * フッターを追加して上部の余白を潰します. 全てのaddLine()呼び出しが終わった後で呼び出してください。
     */
    protected void addFooter(Component footer) {
        editorPane.addFooter(footer);
    }

    /**
     * ラベルとエディターコンポーネントの二列で構成される行を追加します.
     *
     * @param label
     * @param c
     */
    protected void addLine(String label, Component c) {
        editorPane.addLine(label, c);
    }

    /**
     * 対象のコンポーネント数で等分割される行を追加します.
     *
     * @param cc
     */
    protected void addLine(Component... cc) {
        editorPane.addLine(cc);
    }

    /**
     * 横幅いっぱいに広がるコンポーネントを一行として追加します.
     *
     * @param c
     */
    protected void addLine(Component c) {
        editorPane.addLine(c);
    }

    /**
     * OKボタンを表示するかどうかを設定します.
     *
     * @param hiddenOKButton
     */
    public void setHiddenOKButton(boolean hiddenOKButton) {
        this.hiddenOKButton = hiddenOKButton;
    }

    /**
     * OKボタンを表示するかどうかを返します.
     *
     * @return
     */
    public boolean isHiddenOKButton() {
        return hiddenOKButton;
    }

    /**
     * 適用ボタンを表示するかどうかを設定します.
     *
     * @param hiddenApplyButton
     */
    public void setHiddenApplyButton(boolean hiddenApplyButton) {
        this.hiddenApplyButton = hiddenApplyButton;
    }

    /**
     * 適用ボタンを表示するかどうかを返します.
     *
     * @return
     */
    public boolean isHiddenApplyButton() {
        return hiddenApplyButton;
    }

    /**
     * 閉じるボタンを表示するかどうかを設定します.
     *
     * @param hiddenCancelButton
     */
    public void setHiddenCancelButton(boolean hiddenCancelButton) {
        this.hiddenCancelButton = hiddenCancelButton;
    }

    /**
     * 閉じるボタンを表示するかどうかを返します.
     *
     * @return
     */
    public boolean isHiddenCancelButton() {
        return hiddenCancelButton;
    }

    /**
     * タイトル文字列を設定します.
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * タイトル文字列を返します.
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * アクションイベントに紐づいているダイアログを返します.
     *
     * @param e
     * @return
     */
    protected JDialog getDialog(ActionEvent e) {
        return (JDialog) SwingUtilities.getWindowAncestor((Component) e.getSource());
    }

    /**
     * 現在のエディターペインを返します.
     *
     * @return
     */
    protected T getEditorPane() {
        if (editorPane == null) {
            editorPane = createEditorPane();
        }
        return editorPane;
    }

    /**
     * 現時点でまだバッキングストアに保存されていない変更があるなら true を返します.
     * 典型的な使い方としては、下記のように最後のプロパティ変更でまだバッキングストアに保存されていない変更があったときに
     * ダイアログの次回起動時に最初から適用ボタンを押せる状態にするために使用できます。<br>
     * <pre>
     * PropertyEditor dialog = ...
     * dialog.show(parentFrame, isModified);
     * isModified = dialog.isModified();
     * // isModified が true ... 次回起動時に最初から適用ボタンを押せる
     * // isModified が false ... 次回起動時に最初は適用ボタンを押せない
     * </pre>
     *
     * @return
     */
    public boolean isModified() {
        return status == Status.SAVED;
    }

    /**
     * 現在の状態を返します.
     *
     * @return
     */
    public Status getStatus() {
        return status;
    }

}
