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
import java.lang.annotation.Annotation;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JComponent;

/**
 * あらゆるオブジェクトを編集可能なエディターの実装です. プロパティのデータ型にマッチするエディターを見つけることができなかった場合に使用されます。
 *
 * @author desktopgame
 */
public class ObjectPropertyEditor extends AbstractPropertyEditor implements AnnotatedPropertyEditor {

    private JButton editButton;
    private Object inputValue;
    private Class<?> targetClass;

    public ObjectPropertyEditor() {
        this.editButton = new JButton(Messages.EDIT);
        editButton.addActionListener(this::onAction);
    }

    private void onAction(ActionEvent e) {
        Objects.requireNonNull(targetClass);
        if (inputValue == null) {
            inputValue = Activator.newInstance(targetClass);
        }
        BeanEditorDialog dialog = new BeanEditorDialog(inputValue.getClass(), new NullBeanStore<>());
        dialog.setTitle(Messages.OBJECT_EDIT_DIALOG);
        dialog.setHiddenApplyButton(true);
        dialog.setTarget(inputValue);
        dialog.show(null);
        if (dialog.getStatus() == PropertyEditorDialog.Status.SAVED) {
            dialog.saveToInstance();
            this.inputValue = dialog.getTarget().get();
            firePropertyInput();
        }
    }

    @Override
    public void update(Object value) {
        Objects.requireNonNull(targetClass);
        beginIgnoreEvents();
        if (value == null) {
            value = Activator.newInstance(targetClass);
        }
        if (value instanceof ValueObject) {
            inputValue = ((ValueObject) value).copy();
        } else {
            throw new Error(value.getClass().getName() + " is not implemented ValueObject");
        }
        endIgnoreEvents();
    }

    @Override
    public Object getInputValue() {
        Objects.requireNonNull(targetClass);
        return inputValue;
    }

    @Override
    public JComponent getComponent() {
        Objects.requireNonNull(targetClass);
        return editButton;
    }

    @Override
    public void setup(Annotation ann) {
        setup(((ObjectProperty) ann).value());
    }

    public void setup(Class<?> targetClass) {
        this.targetClass = targetClass;
    }

}
