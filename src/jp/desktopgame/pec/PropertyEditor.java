/*
 * PropertyEditorComponent
 *
 * Copyright (c) 2020 desktopgame
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package jp.desktopgame.pec;

import javax.swing.JComponent;

/**
 * 特定の名前に紐づいたデータを編集するための機能を提供するインターフェイスです.
 *
 * @author desktopgame
 */
public interface PropertyEditor {

    /**
     * プロパティの変更を監視するイベントリスナーを追加します.
     *
     * @param listener
     */
    public void addPropertyInputListener(PropertyInputListener listener);

    /**
     * プロパティの変更を監視するイベントリスナーを削除します.
     *
     * @param listener
     */
    public void removePropertyInputListener(PropertyInputListener listener);

    /**
     * エディターに表示される値を更新します. この変更によってイベントが発行されることはありません。
     *
     * @param value
     */
    public void update(Object value);

    /**
     * エディターに入力されている値を返します.
     *
     * @return
     */
    public Object getInputValue();

    /**
     * 画面に表示されるコンポーネントを返します.
     *
     * @return
     */
    public JComponent getComponent();
}
