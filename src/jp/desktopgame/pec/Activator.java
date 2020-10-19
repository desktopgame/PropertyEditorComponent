/*
 * PropertyEditorComponent
 *
 * Copyright (c) 2020 desktopgame
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package jp.desktopgame.pec;

import java.util.logging.Level;
import java.util.logging.Logger;

class Activator {

    private Activator() {
    }

    public static Object newInstance(Class<?> clazz) {
        if (clazz == int.class || clazz == Integer.class) {
            return 0;
        }
        if (clazz == float.class || clazz == Float.class) {
            return 0f;
        }
        if (clazz == boolean.class || clazz == Boolean.class) {
            return false;
        }
        if (clazz == String.class) {
            return "";
        }
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new Error(clazz.getSimpleName() + "." + clazz.getSimpleName() + "() is not found.");
        }
    }

    public static Object newInstance(Object a) {
        return newInstance(a.getClass());
    }
}
