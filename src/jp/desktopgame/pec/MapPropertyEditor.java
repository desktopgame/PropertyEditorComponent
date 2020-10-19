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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * マップを編集するためのエディターの実装です.
 *
 * @author desktopgame
 */
public class MapPropertyEditor extends AbstractPropertyEditor implements AnnotatedPropertyEditor {

    private Map map;
    private Map newMap;
    private JButton okButton;
    private PropertyEditor keyEditor, valEditor;

    private JTable table;
    private DefaultTableModel tableModel;

    public MapPropertyEditor() {
        this.okButton = new JButton(Messages.EDIT);
        okButton.addActionListener(this::onAction);
        // キー/値確認用のテーブル
        this.tableModel = new DefaultTableModel(new String[]{Messages.KEY, Messages.VALUE}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void onAction(ActionEvent e) {
        Objects.requireNonNull(keyEditor);
        Objects.requireNonNull(valEditor);

        JFrame nullFrame = null;
        JDialog dialog = new JDialog(nullFrame);
        JButton dialogOkButton = new JButton(Messages.OK);
        JButton dialogCancelButton = new JButton(Messages.CANCEL);
        JButton addButton = new JButton(Messages.ADD);
        dialogOkButton.addActionListener((ae) -> {
            map.clear();
            map.putAll(newMap);
            this.firePropertyInput();
            dialog.dispose();
        });
        dialogCancelButton.addActionListener((ae) -> {
            removeAllRow();
            for (Object k : map.keySet()) {
                tableModel.addRow(new Object[]{k, map.get(k)});
            }
            newMap = new HashMap(map);
            dialog.dispose();
        });
        // 追加
        addButton.addActionListener((ae) -> {
            JDialog editDialog = new JDialog(nullFrame);
            editDialog.setLayout(new BorderLayout());
            JPanel panel = new JPanel(new BorderLayout());
            JButton iAddButton = new JButton(Messages.OK);
            iAddButton.addActionListener((aet) -> {
                Object kv = keyEditor.getInputValue();
                Object vv = valEditor.getInputValue();
                if (newMap.containsKey(kv)) {
                    JOptionPane.showMessageDialog(null, Messages.DUPLICATE_KEY);
                    return;
                }
                tableModel.addRow(new Object[]{kv, vv});
                newMap.put(kv, vv);
                editDialog.dispose();
            });
            JButton iDelButton = new JButton(Messages.CLOSE);
            iDelButton.addActionListener((aet) -> {
                editDialog.dispose();
            });
            PropertyEditorPane vbox = new PropertyEditorPane() {
                {
                    addLine(Messages.KEY, keyEditor.getComponent());
                    addLine(Messages.VALUE, valEditor.getComponent());
                    addLine(LayoutUtilities.createButtonBar(iAddButton, iDelButton)); //add(LayoutUtilities.createButtonBar(dialogOkButton, dialogCancelButton));
                    addFooter();
                }
            };
            panel.add(vbox, BorderLayout.CENTER);
            editDialog.setTitle(Messages.ADD_NEW_ITEM);
            editDialog.add(panel, BorderLayout.CENTER);
            editDialog.pack();
            editDialog.setLocationRelativeTo(null);
            editDialog.setModal(true);
            editDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            editDialog.setVisible(true);
        });
        // 削除
        JButton delButton = new JButton(Messages.DEL);
        delButton.addActionListener((ae) -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                Object k = tableModel.getValueAt(row, 0);
                newMap.remove(k);
                tableModel.removeRow(row);
            }
        });
        JPanel panel = new JPanel(new BorderLayout());
        PropertyEditorPane vbox = new PropertyEditorPane() {
            {
                addLine(new JScrollPane(table));
                addLine(LayoutUtilities.createButtonBar(addButton, delButton));
                addLine(new JSeparator());
                addLine(LayoutUtilities.createButtonBar(dialogOkButton, dialogCancelButton)); //add(LayoutUtilities.createButtonBar(dialogOkButton, dialogCancelButton));
                addFooter();
            }
        };
        panel.add(vbox, BorderLayout.CENTER);
        dialog.setTitle(Messages.MAP_EDIT_DIALOG);
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
        Objects.requireNonNull(keyEditor);
        Objects.requireNonNull(valEditor);
        if (value == null) {
            value = new HashMap();
        }
        beginIgnoreEvents();
        if (value instanceof HashMap) {
            this.map = new HashMap((HashMap) value);
        } else if (value instanceof TreeMap) {
            this.map = new TreeMap((TreeMap) value);
        } else {
            if (value instanceof Map) {
                this.map = new HashMap((Map) value);
            } else {
                throw new Error();
            }
        }
        this.newMap = new HashMap(map);
        removeAllRow();
        for (Object k : map.keySet()) {
            tableModel.addRow(new Object[]{k, map.get(k)});
        }
        endIgnoreEvents();
    }

    private void removeAllRow() {
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(0);
        }
    }

    @Override
    public Object getInputValue() {
        Objects.requireNonNull(keyEditor);
        Objects.requireNonNull(valEditor);
        return map;
    }

    @Override
    public JComponent getComponent() {
        Objects.requireNonNull(keyEditor);
        Objects.requireNonNull(valEditor);
        return okButton;
    }

    @Override
    public void setup(Annotation ann) {
        if (ann instanceof MapProperty) {
            MapProperty mp = (MapProperty) ann;
            setup(mp.key(), mp.value());
        }
    }

    public void setup(Class<?> key, Class<?> val) {
        this.keyEditor = BeanEditorPane.createPropertyEditor(key);
        this.valEditor = BeanEditorPane.createPropertyEditor(val);
        if (keyEditor instanceof ObjectPropertyEditor) {
            ((ObjectPropertyEditor) keyEditor).setup(key);
        }
        if (valEditor instanceof ObjectPropertyEditor) {
            ((ObjectPropertyEditor) valEditor).setup(val);
        }
    }

}
