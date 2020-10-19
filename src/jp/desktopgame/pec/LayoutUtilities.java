/*
 * PropertyEditorComponent
 *
 * Copyright (c) 2020 desktopgame
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package jp.desktopgame.pec;

import java.awt.FlowLayout;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

class LayoutUtilities {

    private LayoutUtilities() {
    }

    public static Box createButtonBar(JButton... buttons) {
        Box buttonBox = Box.createHorizontalBox();
        JPanel flow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        for (JButton button : buttons) {
            flow.add(button);
        }
        buttonBox.add(Box.createHorizontalGlue());
        buttonBox.add(flow);
        return buttonBox;
    }
}
