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
import java.util.Objects;
import java.util.Optional;

/**
 * リフレクションを用いて動的に編集画面をダイアログとして構築するクラスです.
 *
 * @author desktopgame
 */
public class BeanEditorDialog<T> extends PropertyEditorDialog<BeanEditorPane<T>> {

    private Class<T> clazz;
    private BeanStore<T> store;

    public BeanEditorDialog(Class<T> clazz, BeanStore<T> store) {
        this.clazz = Objects.requireNonNull(clazz);
        this.store = Objects.requireNonNull(store);
        setTitle(Messages.OBJECT_EDIT_DIALOG);
    }

    @Override
    protected final BeanEditorPane<T> createEditorPane() {
        return new BeanEditorPane<T>(clazz) {
            {
                setImmediate(false);
            }

            @Override
            public void setDirty(boolean dirty) {
                if (dirty) {
                    statusModified();
                }
                super.setDirty(dirty); //To change body of generated methods, choose Tools | Templates.
            }
        };
    }

    /**
     * 現在エディターに入力されている値をリフレクションを使用してインスタンスに書き込みます.
     */
    public final void saveToInstance() {
        getEditorPane().saveToInstance();
    }

    @Override
    protected final void onOK(ActionEvent e) {
        getEditorPane().saveToInstance();
        super.onOK(e); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected final void onApply(ActionEvent e) {
        getEditorPane().saveToInstance();
        store.store(getEditorPane().getTarget().get());
        statusApplied();
    }

    /**
     * リフレクションの実行対象となるインスタンスを設定します.
     *
     * @param target
     */
    public final void setTarget(T target) {
        getEditorPane().setTarget(target);
    }

    /**
     * リフレクションの実行対象となるインスタンスを返します.
     *
     * @return
     */
    public final Optional<T> getTarget() {
        return getEditorPane().getTarget();
    }
}
