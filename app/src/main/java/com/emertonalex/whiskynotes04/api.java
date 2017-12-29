package com.emertonalex.whiskynotes04;


class api {
    private static final String ROOT_URL = "https://thanedd-isle-187914.appspot.com/";

    static final String URL_CREATE_NOTE= ROOT_URL + "notes_list/";

    static final String URL_UPDATE_NOTE = ROOT_URL + "updatenote";
    static final String URL_DELETE_NOTE = ROOT_URL + "deletenote&id=";

    static final String URL_READ_NOTES = ROOT_URL + "notes_list?format=json";
    static final String URL_CATEGORIES_NAME = ROOT_URL + "name_list?format=json";
    static final String URL_CATEGORIES_NOSE = ROOT_URL + "nose_list?format=json";
    static final String URL_CATEGORIES_PALATE = ROOT_URL + "palate_list?format=json";
    static final String URL_CATEGORIES_FINISH = ROOT_URL + "finish_list?format=json";
}
