/*
 * PropertyEditorComponent
 *
 * Copyright (c) 2020 desktopgame
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package jp.desktopgame.pec;

import java.lang.annotation.Annotation;

/**
 * エディターに対して追加で情報を与える必要がある場合、このインターフェイスを実装します. 加えて、{$link
 * jp.desktopgame.pec.BeanEditorPane.registerAnnotatedPropertyEditor}を使用して対応するアノテーション型を登録する必要があります。
 *
 * @author desktopgame
 */
public interface AnnotatedPropertyEditor {

    /**
     * プロパティに設定されているアノテーションから追加情報を読み込みます.
     *
     * @param ann
     */
    public void setup(Annotation ann);

}
