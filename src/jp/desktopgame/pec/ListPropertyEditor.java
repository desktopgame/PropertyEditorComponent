/*
 * PropertyEditorComponent
 *
 * Copyright (c) 2020 desktopgame
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package jp.desktopgame.pec;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ListSelectionModel;

/**
 * リストを編集するためのエディターの実装です.
 *
 * @author desktopgame
 */
public class ListPropertyEditor extends AbstractPropertyEditor implements AnnotatedPropertyEditor {

    private JButton okButton;
    private JList list;
    private DefaultListModel listModel;
    private PropertyEditor innerEditor;
    private List buffer;
    private List newBuffer;

    public ListPropertyEditor() {
        this.okButton = new JButton(Messages.EDIT);
        this.list = new JList((this.listModel = new DefaultListModel<>()));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        okButton.addActionListener(this::onAction);
    }

    private void onAction(ActionEvent e) {
        Objects.requireNonNull(innerEditor);
        JFrame nullFrame = null;
        JDialog dialog = new JDialog(nullFrame);
        JButton dialogOkButton = new JButton(Messages.OK);
        JButton dialogCancelButton = new JButton(Messages.CANCEL);
        JButton addButton = new JButton(Messages.ADD);
        JButton delButton = new JButton(Messages.DEL);
        addButton.addActionListener((ae) -> {
            Object iv = innerEditor.getInputValue();
            listModel.addElement(iv);
            newBuffer.add(iv);
            innerEditor.update(Activator.newInstance(iv));
        });
        delButton.addActionListener((ae) -> {
            int i = list.getSelectedIndex();
            if (i >= 0) {
                listModel.remove(i);
                newBuffer.remove(i);
            }
        });
        dialogOkButton.addActionListener((ae) -> {
            buffer.clear();
            buffer.addAll(newBuffer);
            this.firePropertyInput();
            dialog.dispose();
        });
        dialogCancelButton.addActionListener((ae) -> {
            dialog.dispose();
            listModel.clear();
            for (Object a : buffer) {
                listModel.addElement(a);
            }
        });
        JPanel panel = new JPanel(new BorderLayout());
        PropertyEditorPane vbox = new PropertyEditorPane() {
            {
                addLine(new JScrollPane(list));
                addLine(innerEditor.getComponent());
                addLine(LayoutUtilities.createButtonBar(addButton, delButton));
                addLine(new JSeparator());
                addLine(LayoutUtilities.createButtonBar(dialogOkButton, dialogCancelButton)); //add(LayoutUtilities.createButtonBar(dialogOkButton, dialogCancelButton));
                addFooter();
            }
        };
        panel.add(vbox, BorderLayout.CENTER);
        dialog.setTitle(Messages.LIST_EDIT_DIALOG);
        dialog.setLayout(new BorderLayout());
        dialog.add(panel, BorderLayout.CENTER);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    @Override
    public void update(Object value) {
        Objects.requireNonNull(innerEditor);
        if (value == null) {
            value = new ArrayList();
        }
        if (value instanceof ArrayList) {
            this.buffer = new ArrayList((List) value);
        } else if (value instanceof LinkedList) {
            this.buffer = new LinkedList((List) value);
        } else {
            if (value instanceof List) {
                this.buffer = new ArrayList((List) value);
            } else {
                throw new Error();
            }
        }
        this.newBuffer = new ArrayList(buffer);
        List listVal = (List) value;
        listModel.clear();
        buffer.clear();
        for (Object a : listVal) {
            listModel.addElement(a);
            buffer.add(a);
        }
    }

    @Override
    public Object getInputValue() {
        Objects.requireNonNull(innerEditor);
        return buffer;
    }

    @Override
    public JComponent getComponent() {
        Objects.requireNonNull(innerEditor);
        return okButton;
    }

    @Override
    public void setup(Annotation ann) {
        ListProperty listProp = (ListProperty) ann;
        Class<?> listType = listProp.value();
        this.innerEditor = BeanEditorPane.createPropertyEditor(listType);
        if (innerEditor instanceof ObjectPropertyEditor) {
            ((ObjectPropertyEditor) innerEditor).setup(listType);
        }
    }
}
