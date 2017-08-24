package com.k3mshiro.k3mnotes.dto;

import java.io.Serializable;

public class NoteDTO implements Serializable {
    private int id;
    private String title;
    private String date;
    private String content;
    private String color;

    public NoteDTO(int id, String title, String date, String content, String color) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.content = content;
        this.color = color;
    }

    public NoteDTO(String title, String date, String content, String color) {
        this.title = title;
        this.date = date;
        this.content = content;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

