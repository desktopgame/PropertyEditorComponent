/*
 * PropertyEditorComponent
 *
 * Copyright (c) 2020 desktopgame
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package jp.desktopgame.pec;

import java.util.EventObject;

/**
 * エディターに入力されている値が変化したことを通知するイベントです.
 *
 * @author desktopgame
 */
public class PropertyInputEvent extends EventObject {

    public PropertyInputEvent(PropertyEditor o) {
        super(o);
    }

    @Override
    public PropertyEditor getSource() {
        return (PropertyEditor) super.getSource(); //To change body of generated methods, choose Tools | Templates.
    }

}
