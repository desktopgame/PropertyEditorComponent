/*
 * PropertyEditorComponent
 *
 * Copyright (c) 2020 desktopgame
 *
 * This software is released under the MIT License.
 * http://opensource.org/licenses/mit-license.php
 */
package jp.desktopgame.pec;

/**
 * エディターで表示されるテキストを定義するクラスです. 実際にはロケールごとにこのクラスの変数を書き換える必要があります。
 *
 * @author desktopgame
 */
public class Messages {

    private Messages() {
    }

    public static String EDIT = "編集";
    public static String OK = "OK";
    public static String APPLY = "適用";
    public static String CLOSE = "閉じる";
    public static String CANCEL = "取り消し";
    public static String ADD = "追加";
    public static String DEL = "削除";
    public static String KEY = "キー";
    public static String VALUE = "値";
    public static String ADD_NEW_ITEM = "新しい項目の追加";
    public static String DUPLICATE_KEY = "キーが重複しています。";
    public static String MAP_EDIT_DIALOG = "辞書の編集";
    public static String LIST_EDIT_DIALOG = "リストの編集";
    public static String OBJECT_EDIT_DIALOG = "オブジェクトの編集";
    public static String PROPERTY_EDIT_DIALOG = "プロパティの編集";
}
