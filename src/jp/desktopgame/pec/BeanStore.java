/*
 * PropertyEditorComponent
 *
 * Copyright (c) 2020 desktopgame
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package jp.desktopgame.pec;

/**
 * プロパティの一覧を保存する機能を提供します.
 *
 * @author desktopgame
 * @param <T>
 */
public interface BeanStore<T> {

    /**
     * 指定のオブジェクトのプロパティを全て保存します.
     *
     * @param instance
     */
    public void store(T instance);
}
