/*
 * PropertyEditorComponent
 *
 * Copyright (c) 2020 desktopgame
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package jp.desktopgame.pec;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * List, ArrayListとして定義されているフィールドの要素型をマークします.
 * {@link jp.desktopgame.pec.ListPropertyEditor}が新規要素を追加するためのエディターをルックアップするために使用します。
 *
 * @author desktopgame
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ListProperty {

    Class<?> value() default Object.class;
}
