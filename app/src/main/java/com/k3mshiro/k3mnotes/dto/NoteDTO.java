package com.k3mshiro.k3mnotes.dto;

import java.io.Serializable;

public class NoteDTO implements Serializable {
    private int id;
    private String title;
    private String date;
    private String content;
    private String color;
    private int priority;
    private String modifiedDate;
    private int favoriteValue;
    private long timeReminder;
    private int reminderId;
    private int isInTrash;

    public NoteDTO(int id, String title, String date, String content, String color, int priority,
                   String modifiedDate, int favoriteValue, long timeReminder, int reminderId, int isInTrash) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.content = content;
        this.color = color;
        this.priority = priority;
        this.modifiedDate = modifiedDate;
        this.favoriteValue = favoriteValue;
        this.timeReminder = timeReminder;
        this.reminderId = reminderId;
        this.isInTrash = isInTrash;
    }

    public NoteDTO(String title, String date, String content, String color, int priority,
                   String modifiedDate, int favoriteValue, long timeReminder, int reminderId, int isInTrash) {
        this.title = title;
        this.date = date;
        this.content = content;
        this.color = color;
        this.priority = priority;
        this.modifiedDate = modifiedDate;
        this.favoriteValue = favoriteValue;
        this.timeReminder = timeReminder;
        this.reminderId = reminderId;
        this.isInTrash = isInTrash;
    }

    public NoteDTO(String title, String date, String content, String color, int priority,
                   String modifiedDate, int favoriteValue, long timeReminder, int reminderId) {
        this.title = title;
        this.date = date;
        this.content = content;
        this.color = color;
        this.priority = priority;
        this.modifiedDate = modifiedDate;
        this.favoriteValue = favoriteValue;
        this.timeReminder = timeReminder;
        this.reminderId = reminderId;
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

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public int getFavoriteValue() {
        return favoriteValue;
    }

    public void setFavoriteValue(int favoriteValue) {
        this.favoriteValue = favoriteValue;
    }

    public long getTimeReminder() {
        return timeReminder;
    }

    public void setTimeReminder(long timeReminder) {
        this.timeReminder = timeReminder;
    }

    public int getReminderId() {
        return reminderId;
    }

    public void setReminderId(int reminderId) {
        this.reminderId = reminderId;
    }

    public int isInTrash() {
        return isInTrash;
    }

    public void setInTrash(int inTrash) {
        isInTrash = inTrash;
    }
}

