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
 * 何も行わない{@link jp.desktopgame.pec.BeanStore}の実装です. デバッグにのみ使用してください。
 *
 *
 * @author desktopgame
 * @param <T>
 */
public class NullBeanStore<T> implements BeanStore<T> {

    @Override
    public void store(T instance) {
    }

}
