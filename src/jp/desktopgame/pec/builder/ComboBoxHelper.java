/*
 * PropertyEditorComponent
 *
 * Copyright (c) 2020 desktopgame
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package jp.desktopgame.pec.builder;

import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 * コンボボックスのラッパーです.
 *
 * @author desktopgame
 */
public class ComboBoxHelper<T> implements Supplier<JComboBox<T>> {

    private DefaultComboBoxModel<T> model;
    private JComboBox<T> comboBox;

    public ComboBoxHelper() {
        this.model = new DefaultComboBoxModel<>();
        this.comboBox = new JComboBox<>(model);
    }

    /**
     * 要素を追加します.
     *
     * @param item
     * @return
     */
    public ComboBoxHelper add(T item) {
        model.addElement(item);
        return this;
    }

    /**
     * 要素を全て追加します.
     *
     * @param items
     * @return
     */
    public ComboBoxHelper addAll(T... items) {
        return addAll(Arrays.asList(items));
    }

    /**
     * 要素を全て追加します.
     *
     * @param items
     * @return
     */
    public ComboBoxHelper addAll(List<T> items) {
        for (T item : items) {
            add(item);
        }
        return this;
    }

    /**
     * 要素を上書きします.
     *
     * @param items
     * @return
     */
    public ComboBoxHelper overwrite(T... items) {
        return overwrite(Arrays.asList(items));
    }

    /**
     * 要素を上書きします.
     *
     * @param items
     * @return
     */
    public ComboBoxHelper overwrite(List<T> items) {
        removeAll();
        return addAll(items);
    }

    /**
     * 要素を削除します.
     *
     * @param item
     * @return
     */
    public ComboBoxHelper remove(T item) {
        model.removeElement(item);
        return this;
    }

    /**
     * 要素を削除します.
     *
     * @param i
     * @return
     */
    public ComboBoxHelper remove(int i) {
        model.removeElementAt(i);
        return this;
    }

    /**
     * 要素を全て削除します.
     *
     * @return
     */
    public ComboBoxHelper removeAll() {
        model.removeAllElements();
        return this;
    }

    /**
     * 指定の要素を選択状態にします.
     *
     * @param i
     * @return
     */
    public ComboBoxHelper select(int i) {
        comboBox.setSelectedIndex(i);
        return this;
    }

    /**
     * 項目の一覧を返します.
     *
     * @return
     */
    public List<T> list() {
        List<T> ret = new ArrayList<>();
        for (int i = 0; i < model.getSize(); i++) {
            ret.add(model.getElementAt(i));
        }
        return ret;
    }

    /**
     * 指定位置の要素を返します.
     *
     * @param i
     * @return
     */
    public T at(int i) {
        return model.getElementAt(i);
    }

    /**
     * 選択位置を返します.
     *
     * @return
     */
    public int index() {
        return comboBox.getSelectedIndex();
    }

    /**
     * 要素数を返します.
     *
     * @return
     */
    public int size() {
        return model.getSize();
    }

    /**
     * イベントリスナーを追加します.
     *
     * @param listener
     * @return
     */
    public ComboBoxHelper onSelect(ItemListener listener) {
        comboBox.addItemListener(listener);
        return this;
    }

    @Override
    public JComboBox<T> get() {
        return comboBox;
    }

}
