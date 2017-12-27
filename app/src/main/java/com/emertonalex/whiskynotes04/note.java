package com.emertonalex.whiskynotes04;

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

    int getID(){
        return id;
    }

    int getRating(){
        return rating;
    }

    public String getName(){
        return name;
    }

    String getNose(){
        return nose;
    }

    String getPalate(){
        return palate;
    }

    String getFinish(){
        return finish;
    }

    String getExtra(){
        return extra;
    }
}
