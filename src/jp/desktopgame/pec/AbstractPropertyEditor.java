/*
 * PropertyEditorComponent
 *
 * Copyright (c) 2020 desktopgame
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package jp.desktopgame.pec;

import javax.swing.event.EventListenerList;

/**
 * エディターの共通部分の実装を提供します.
 *
 * @author desktopgame
 */
public abstract class AbstractPropertyEditor implements PropertyEditor {

    private EventListenerList listenerList;
    private int ignoreEvents;

    public AbstractPropertyEditor() {
        this.listenerList = new EventListenerList();
    }

    protected void beginIgnoreEvents() {
        ignoreEvents++;
    }

    protected void endIgnoreEvents() {
        ignoreEvents--;
    }

    @Override
    public void addPropertyInputListener(PropertyInputListener listener) {
        listenerList.add(PropertyInputListener.class, listener);
    }

    @Override
    public void removePropertyInputListener(PropertyInputListener listener) {
        listenerList.remove(PropertyInputListener.class, listener);
    }

    protected void firePropertyInput() {
        if (ignoreEvents == 0) {
            PropertyInputEvent e = new PropertyInputEvent(this);
            for (PropertyInputListener listener : listenerList.getListeners(PropertyInputListener.class)) {
                listener.propertyInput(e);
            }
        }
    }
}
