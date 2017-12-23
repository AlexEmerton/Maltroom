package com.emertonalex.whiskynotes04;

/**
 * Created by Alex on 20-Sep-17.
 */

import java.io.Serializable;

public class note {
    private int id;
    private String name, nose, palate, finish, extra;
    private int rating;

    public note(int id, int rating, String name, String nose, String palate,
                String finish, String extra){
        this.id = id;
        this.rating = rating;
        this.name = name;
        this.nose = nose;
        this.palate = palate;
        this.finish = finish;
        this.extra = extra;
    }

    public int getID(){
        return id;
    }

    public int getRating(){
        return rating;
    }

    public String getName(){
        return name;
    }

    public String getNose(){
        return nose;
    }

    public String getPalate(){
        return palate;
    }

    public String getFinish(){
        return finish;
    }

    public String getExtra(){
        return extra;
    }
}
