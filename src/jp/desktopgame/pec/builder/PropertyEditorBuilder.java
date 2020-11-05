/*
 * PropertyEditorComponent
 *
 * Copyright (c) 2020 desktopgame
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package jp.desktopgame.pec.builder;

import java.awt.Component;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import jp.desktopgame.pec.PropertyEditorDialog;
import jp.desktopgame.pec.PropertyEditorPane;

/**
 * コンポーネントを並べるためのビルダークラスです.
 *
 * @author desktopgame
 */
public class PropertyEditorBuilder {

    private List<AbstractMap.SimpleEntry<String, Supplier<? extends Component>>> properties;
    private List<Component> footers;
    private Component fill;

    public PropertyEditorBuilder() {
        this.properties = new ArrayList<>();
        this.footers = new ArrayList<>();
    }

    /**
     * コンボボックスを追加します.
     *
     * @param <T>
     * @param label
     * @return
     */
    public <T> ComboBoxHelper<T> comboBox(String label) {
        ComboBoxHelper ret = new ComboBoxHelper();
        properties.add(new AbstractMap.SimpleEntry<>(label, ret));
        return ret;
    }

    /**
     * テキストフィールドを追加します.
     *
     * @param label
     * @return
     */
    public TextFieldHelper textField(String label) {
        TextFieldHelper ret = new TextFieldHelper();
        properties.add(new AbstractMap.SimpleEntry<>(label, ret));
        return ret;
    }

    /**
     * スピナーを追加します.
     *
     * @param label
     * @return
     */
    public IntegerSpinnerHelper intSpinner(String label) {
        IntegerSpinnerHelper ret = new IntegerSpinnerHelper();
        properties.add(new AbstractMap.SimpleEntry<>(label, ret));
        return ret;
    }

    /**
     * スピナーを追加します.
     *
     * @param label
     * @return
     */
    public DoubleSpinnerHelper doubleSpinner(String label) {
        DoubleSpinnerHelper ret = new DoubleSpinnerHelper();
        properties.add(new AbstractMap.SimpleEntry<>(label, ret));
        return ret;
    }

    /**
     * チェックボックスを追加します.
     *
     * @param label
     * @return
     */
    public CheckBoxHelper checkBox(String label) {
        CheckBoxHelper ret = new CheckBoxHelper();
        properties.add(new AbstractMap.SimpleEntry<>(label, ret));
        return ret;
    }

    /**
     * フッターを追加します.
     *
     * @param <T>
     * @param footer
     * @return
     */
    public <T extends Component> T footer(T footer) {
        this.footers.add(footer);
        return footer;
    }

    /**
     * 余白を潰すためのコンポーネントを指定します. 指定しなかった場合には透明のコンポーネントが設定されます。
     *
     * @param <T>
     * @param fill
     * @return
     */
    public <T extends Component> T fill(T fill) {
        this.fill = fill;
        return fill;
    }

    /**
     * 現在の設定でエディターペインを生成します.
     *
     * @return
     */
    public PropertyEditorPane buildPane() {
        return new PropertyEditorPane() {
            {
                for (AbstractMap.SimpleEntry<String, Supplier<? extends Component>> kv : properties) {
                    addLine(kv.getKey(), kv.getValue().get());
                }
                for (Component c : footers) {
                    addLine(c);
                }
                if (fill != null) {
                    addFooter(fill);
                } else {
                    addFooter();
                }
            }
        };
    }

    /**
     * 現在の設定でエディターダイアログを生成します.
     *
     * @param title
     * @return
     */
    public PropertyEditorDialog buildDialog(String title) {
        return new PropertyEditorDialog() {
            {
                setHiddenApplyButton(true);
                setHiddenCancelButton(true);
                setHiddenOKButton(true);
                setTitle(title);
            }

            @Override
            protected void init() {
                for (AbstractMap.SimpleEntry<String, Supplier<? extends Component>> kv : properties) {
                    addLine(kv.getKey(), kv.getValue().get());
                }
                for (Component c : footers) {
                    addLine(c);
                }
                if (fill != null) {
                    addFooter(fill);
                } else {
                    addFooter();
                }
            }
        };
    }
}
