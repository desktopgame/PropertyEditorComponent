/*
 * PropertyEditorComponent
 *
 * Copyright (c) 2020 desktopgame
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package jp.desktopgame.pec;

import java.awt.Component;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSeparator;

/**
 * リフレクションを用いて動的に編集画面を構築するクラスです.
 *
 * @author desktopgame
 */
public class BeanEditorPane<T> extends PropertyEditorPane {

    private Class<T> clazz;
    private List<Field> fields;
    private Map<String, PropertyEditor> editorMap;
    private Optional<T> target;
    private static final Map<Class<?>, Class<? extends PropertyEditor>> editorTypeMap;
    private static final Map<Class<? extends Annotation>, Class<? extends AnnotatedPropertyEditor>> annotatedEditorTypeMap;
    private boolean immediate;
    private boolean dirty;

    static {
        editorTypeMap = new HashMap<>();
        editorTypeMap.put(String.class, StringPropertyEditor.class);
        editorTypeMap.put(int.class, IntegerPropertyEditor.class);
        editorTypeMap.put(Integer.class, IntegerPropertyEditor.class);
        editorTypeMap.put(double.class, DoublePropertyEditor.class);
        editorTypeMap.put(Double.class, DoublePropertyEditor.class);
        editorTypeMap.put(boolean.class, BooleanPropertyEditor.class);
        editorTypeMap.put(Boolean.class, BooleanPropertyEditor.class);
        editorTypeMap.put(List.class, ListPropertyEditor.class);
        editorTypeMap.put(ArrayList.class, ListPropertyEditor.class);
        editorTypeMap.put(Object.class, ObjectPropertyEditor.class);
        editorTypeMap.put(Map.class, MapPropertyEditor.class);
        editorTypeMap.put(HashMap.class, MapPropertyEditor.class);
        editorTypeMap.put(TreeMap.class, MapPropertyEditor.class);
        annotatedEditorTypeMap = new HashMap<>();
        annotatedEditorTypeMap.put(ListProperty.class, ListPropertyEditor.class);
        annotatedEditorTypeMap.put(ObjectProperty.class, ObjectPropertyEditor.class);
        annotatedEditorTypeMap.put(MapProperty.class, MapPropertyEditor.class);
    }

    /**
     * プロパティのデータ型とそのデータを編集するためのエディター型を紐づけます.
     *
     * @param <T>
     * @param k
     * @param v
     */
    public static <T extends PropertyEditor> void registerPropertyEditor(Class<?> k, Class<T> v) {
        editorTypeMap.put(k, v);
    }

    /**
     * アノテーションとそのアノテーションから追加情報を受け取るためのエディター型を紐づけます.
     *
     * @param <T>
     * @param k
     * @param v
     */
    public static <T extends AnnotatedPropertyEditor> void registerAnnotatedPropertyEditor(Class<? extends Annotation> k, Class<T> v) {
        annotatedEditorTypeMap.put(k, v);
    }

    /**
     * 指定の型に紐づいたエディターを生成します.
     *
     * @param <T>
     * @param k
     * @return
     */
    public static <T extends PropertyEditor> T createPropertyEditor(Class<?> k) {
        if (!editorTypeMap.containsKey(k)) {
            return createPropertyEditor(Object.class);
        }
        T returnVar = null;
        // 空コンストラクタを呼び出す
        try {
            if (returnVar == null) {
                returnVar = (T) editorTypeMap.get(k).newInstance();
            }
        } catch (InstantiationException | IllegalAccessException | SecurityException | IllegalArgumentException ex) {
            throw new Error(ex);
        }
        return returnVar;
    }

    /**
     * フィールドのデータ型からエディターを生成します.
     * また、エディタがAnnotatedPropertyEditorを実装しているなら、フィールドに定義されたアノテーションの一覧を生成されたエディターに読み込ませます.
     *
     * @param <T>
     * @param f
     * @return
     */
    public static <T extends PropertyEditor> T createPropertyEditor(Field f) {
        Class<?> k = f.getType();
        T returnVar = createPropertyEditor(k);
        // アノテーションエディタのセットアップ
        final Object returnVarR = returnVar;
        annotatedEditorTypeMap.forEach((Class<? extends Annotation> annTy, Class<? extends AnnotatedPropertyEditor> editorTy) -> {
            Annotation ann = f.getAnnotation(annTy);
            if (ann == null) {
                return;
            }
            AnnotatedPropertyEditor c = editorTy.cast(returnVarR);
            c.setup(ann);
        });
        return returnVar;
    }

    public BeanEditorPane(Class<T> clazz) {
        this.clazz = clazz;
        this.fields = Arrays.asList(clazz.getDeclaredFields())
                .stream()
                .filter((e) -> e.getAnnotation(Property.class) != null)
                .collect(Collectors.toList());
        this.editorMap = new HashMap<>();
        this.target = Optional.empty();
        fields.forEach((f) -> {
            Property prop = f.getAnnotation(Property.class);
            String label = prop.value();
            if (label.equals("")) {
                label = f.getName();
            }
            Separator sep = f.getAnnotation(Separator.class);
            if (sep != null) {
                if (sep.value().equals("")) {
                    addLine(Box.createVerticalStrut(5));
                    addLine(new JSeparator());
                } else {
                    addLine(new JLabel(sep.value()));
                    addLine(new JSeparator());
                }
            }
            addLine(label, putComponent(f, createEditor(f)));
        });
        addFooter();
    }

    private PropertyEditor createEditor(Field f) {
        return createProxy(f, createPropertyEditor(f));
    }

    protected PropertyEditor createProxy(Field f, PropertyEditor editor) {
        PathString pathString = f.getAnnotation(PathString.class);
        if (pathString != null) {
            return new PathStringEditor(editor, pathString.value());
        }
        return editor;
    }

    private JComponent putComponent(Field f, PropertyEditor c) {
        String name = f.getName();
        editorMap.put(name, c);
        if (name.equals(name.toUpperCase())) {
            enableHierarchy(c.getComponent(), false);
        }
        c.addPropertyInputListener((e) -> {
            Object iv = e.getSource().getInputValue();
            target.ifPresent((target) -> {
                setDirty(true);
                if (isImmediate()) {
                    inspectValue(target, f, iv);
                }
            });
        });
        return c.getComponent();
    }

    private void enableHierarchy(JComponent c, boolean b) {
        c.setEnabled(b);
        for (int i = 0; i < c.getComponentCount(); i++) {
            Component child = c.getComponent(i);
            if (child instanceof JComponent) {
                enableHierarchy((JComponent) child, b);
            }
        }
    }

    /**
     * 現在エディターに入力されている値をリフレクションを使用してインスタンスに書き込みます.
     */
    public void saveToInstance() {
        editorMap.forEach((String k, PropertyEditor e) -> {
            target.ifPresent((target) -> {
                try {
                    inspectValue(target, clazz.getDeclaredField(k), e.getInputValue());
                } catch (NoSuchFieldException | SecurityException ex) {
                    throw new Error(ex);
                }
            });
        });
    }

    /**
     * 現在インスタンスに設定されている値をリフレクションを使用してコンポーネントに読み込みます.
     *
     * @param target
     */
    public void loadFromInstance(T target) {
        fields.forEach((f) -> {
            PropertyEditor editor = editorMap.get(f.getName());
            editor.update(inspectValue(target, f));
        });
    }

    /**
     * 指定のインスタンスから値を読み出します.
     *
     * @param instance
     * @param f
     * @return
     */
    protected Object inspectValue(T instance, Field f) {
        boolean a = f.isAccessible();
        try {
            if (!a) {
                f.setAccessible(true);
            }
            GetMethod gm = f.getAnnotation(GetMethod.class);
            if (gm != null) {
                Method method = clazz.getMethod(buildGetMethodName(f, gm));
                return method.invoke(instance);
            } else {
                return f.get(instance);
            }
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
            throw new Error(ex);
        } finally {
            f.setAccessible(a);
        }
    }

    /**
     * 指定のインスタンスに値を書き込みます.
     *
     * @param instance
     * @param f
     * @param newVal
     */
    protected void inspectValue(T instance, Field f, Object newVal) {
        boolean a = f.isAccessible();
        try {
            if (!a) {
                f.setAccessible(true);
            }
            SetMethod sm = f.getAnnotation(SetMethod.class);
            if (sm != null) {
                Method method = clazz.getMethod(buildSetMethodName(f, sm), f.getType());
                method.invoke(instance, newVal);
            } else {
                f.set(instance, newVal);
            }
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
            throw new Error(ex);
        } finally {
            f.setAccessible(a);
        }
    }

    private String buildSetMethodName(Field f, SetMethod sm) {
        if (!sm.value().equals("")) {
            return sm.value();
        }
        String name = f.getName();
        name = new String(new char[]{name.charAt(0)}).toUpperCase() + name.substring(1);
        return "set" + name;
    }

    private String buildGetMethodName(Field f, GetMethod gm) {
        if (!gm.value().equals("")) {
            return gm.value();
        }
        String name = f.getName();
        name = new String(new char[]{name.charAt(0)}).toUpperCase() + name.substring(1);
        if (name.startsWith("Is")) {
            return "is" + f.getName().substring(2);
        }
        return "get" + name;
    }

    /**
     * 値を編集済み、または保存済みとしてマークします.
     *
     * @param dirty
     */
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    /**
     * 値が編集済みであるならtrueを返します.
     *
     * @return
     */
    public boolean isDirty() {
        return dirty;
    }

    /**
     * リフレクションの実行対象となるインスタンスを設定します.
     *
     * @param target
     */
    public void setTarget(T target) {
        this.target = Optional.of(target);
        loadFromInstance(target);
    }

    /**
     * リフレクションの実行対象となるインスタンスを返します.
     *
     * @return
     */
    public Optional<T> getTarget() {
        return target;
    }

    /**
     * エディターに値が入力されるたびに即座にインスタンスに値を書き込む場合は true を設定します.
     *
     * @param immediate
     */
    public void setImmediate(boolean immediate) {
        this.immediate = immediate;
    }

    /**
     * エディターに値が入力されるたびに即座にインスタンスに値を書き込む場合はtrueを返します.
     *
     * @return
     */
    public boolean isImmediate() {
        return immediate;
    }

}
