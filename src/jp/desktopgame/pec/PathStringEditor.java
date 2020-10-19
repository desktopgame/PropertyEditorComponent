/*
 * PropertyEditorComponent
 *
 * Copyright (c) 2020 desktopgame
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package jp.desktopgame.pec;

import java.awt.event.ActionEvent;
import java.util.Objects;
import javax.swing.JFileChooser;

/**
 * パス文字列を編集するためのエディターの実装です.
 *
 * @author desktopgame
 */
public class PathStringEditor extends ButtonedPropertyEditor {

    private PathStringType type;

    public PathStringEditor(PropertyEditor editor, PathStringType type) {
        super(editor);
        this.type = Objects.requireNonNull(type);
    }

    @Override
    protected void onAction(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        if (type == PathStringType.FILE) {
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        } else if (type == PathStringType.DIRECTORY) {
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        } else if (type == PathStringType.FILE_OR_DIRECTORY) {
            fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        }
        fc.setMultiSelectionEnabled(false);
        if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            update(fc.getSelectedFile().getPath());
            firePropertyInput();
        }
    }

}
