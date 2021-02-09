package com.example.myapplication;


import java.io.Serializable;

public class Data implements Serializable {
    private int id;
    private String title;
    private String content;
    private String date;
    private int titlecolor;
    private int bordercolor;
    private int contentcolor;
    private String typename;
    private long startdate;
    private boolean checked;
    private int typeposition;
    private String startdatestr;

    public Data(String title, String content, String date, int titlecolor, int bordercolor, int contentcolor){
        this.title=title;
        this.content=content;
        this.date=date;
        this.titlecolor = titlecolor;
        this.bordercolor = bordercolor;
        this.contentcolor = contentcolor;
    }
    public Data(int id,String title, String content, String date, int titlecolor, int bordercolor, int contentcolor){
        this.id=id;
        this.title=title;
        this.content=content;
        this.date=date;
        this.titlecolor = titlecolor;
        this.bordercolor = bordercolor;
        this.contentcolor = contentcolor;
    }

    public String getStartdatestr() {
        return startdatestr;
    }

    public void setStartdatestr(String startdatestr) {
        this.startdatestr = startdatestr;
    }

    public int getTypeposition() {
        return typeposition;
    }

    public long getStartdate() {
        return startdate;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setStartdate(long startdate) {
        this.startdate = startdate;
    }

    public void setTypeposition(int typeposition) {
        this.typeposition = typeposition;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getTitlecolor() {
        return titlecolor;
    }

    public int getBordercolor() {
        return bordercolor;
    }

    public void setContentcolor(int contentcolor) {
        this.contentcolor = contentcolor;
    }
    public void setTitlecolor(int titlecolor) {
        this.titlecolor = titlecolor;
    }

    public int getContentcolor() {
        return contentcolor;
    }

    public void setBordercolor(int bordercolor) {
        this.bordercolor = bordercolor;
    }
}
