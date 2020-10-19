/*
 * PropertyEditorComponent
 *
 * Copyright (c) 2020 desktopgame
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package jp.desktopgame.pec;

import java.util.EventListener;

/**
 * エディターに入力されている値の変更を監視するイベントリスナーです.
 *
 * @author desktopgame
 */
public interface PropertyInputListener extends EventListener {

    /**
     * エディターに入力されている値が変化すると呼ばれます.
     *
     * @param e
     */
    public void propertyInput(PropertyInputEvent e);
}
