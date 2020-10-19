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
 * 値型と同様、完全なコピーを生成することができるクラスに実装されるインターフェイスです.
 *
 * @author desktopgame
 */
public interface ValueObject {

    /**
     * このオブジェクトの完全なコピーを返します.
     *
     * @return
     */
    public Object copy();
}
