package com.emertonalex.whiskynotes04;

/**
 * Created by Alex on 20-Sep-17.
 */

public class api {
    private static final String ROOT_URL = "http://192.168.0.26/wh_notes_04/api.php?apicall=";

    public static final String URL_CREATE_NOTE= ROOT_URL + "createnote";
    public static final String URL_READ_NOTES = ROOT_URL + "getnotes";
    public static final String URL_UPDATE_NOTE = ROOT_URL + "updatenote";
    public static final String URL_DELETE_NOTE = ROOT_URL + "deletenote&id=";
    public static final String URL_CATEGORIES_NAME = ROOT_URL + "getname";
    public static final String URL_CATEGORIES_NOSE = ROOT_URL + "getnose";
    public static final String URL_CATEGORIES_PALATE = ROOT_URL + "getpalate";
    public static final String URL_CATEGORIES_FINISH = ROOT_URL + "getfinish";
}
