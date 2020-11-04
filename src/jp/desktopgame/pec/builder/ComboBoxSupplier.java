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
 * コンボボックスを操作するサプライアの実装です.
 *
 * @author desktopgame
 */
public class ComboBoxSupplier<T> implements Supplier<JComboBox<T>> {

    private DefaultComboBoxModel<T> model;
    private JComboBox<T> comboBox;

    public ComboBoxSupplier() {
        this.model = new DefaultComboBoxModel<>();
        this.comboBox = new JComboBox<>(model);
    }

    public ComboBoxSupplier add(T item) {
        model.addElement(item);
        return this;
    }

    public ComboBoxSupplier addAll(T... items) {
        return addAll(Arrays.asList(items));
    }

    public ComboBoxSupplier addAll(List<T> items) {
        for (T item : items) {
            add(item);
        }
        return this;
    }

    public ComboBoxSupplier overwrite(T... items) {
        return overwrite(Arrays.asList(items));
    }

    public ComboBoxSupplier overwrite(List<T> items) {
        removeAll();
        return addAll(items);
    }

    public ComboBoxSupplier remove(T item) {
        model.removeElement(item);
        return this;
    }

    public ComboBoxSupplier remove(int i) {
        model.removeElementAt(i);
        return this;
    }

    public ComboBoxSupplier removeAll() {
        model.removeAllElements();
        return this;
    }

    public ComboBoxSupplier select(int i) {
        comboBox.setSelectedIndex(i);
        return this;
    }

    public List<T> list() {
        List<T> ret = new ArrayList<>();
        for (int i = 0; i < model.getSize(); i++) {
            ret.add(model.getElementAt(i));
        }
        return ret;
    }

    public T at(int i) {
        return model.getElementAt(i);
    }

    public int index() {
        return comboBox.getSelectedIndex();
    }

    public int size() {
        return model.getSize();
    }

    public ComboBoxSupplier onSelect(ItemListener listener) {
        comboBox.addItemListener(listener);
        return this;
    }

    @Override
    public JComboBox<T> get() {
        return comboBox;
    }

}
