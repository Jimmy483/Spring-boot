package com.gmi.learn.model;

public class Theme {

    private String colours;

    public Theme(String colours) {
        this.colours = colours;
    }

    public Theme(){

    }

    public void setColours(String colours){
        this.colours=colours;
    }

    public String getColours(){
        return colours;
    }
}
