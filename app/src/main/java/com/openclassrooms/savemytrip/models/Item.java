package com.openclassrooms.savemytrip.models;

import android.content.ContentValues;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Created by Philippe on 08/03/2018.
 */

// A Data Model class holding data about Items + the setters and getters

// To declare primary key foreign key relationship
@Entity(foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "id",
        childColumns = "userId"))
public class Item {
    @PrimaryKey(autoGenerate = true)  // to allow Room to automatically generate a unique Id for every saved Item.
    private long id;
    private String text;
    private int category;
    private Boolean isSelected;
    private long userId;

    @Ignore
    public Item() {
    }

    public Item(String text, int category, long userId) {
        this.text = text;
        this.category = category;
        this.userId = userId;
        this.isSelected = false;
    }

    // --- GETTER ---
    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public int getCategory() {
        return category;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public long getUserId() {
        return userId;
    }

    // --- SETTER ---
    public void setId(long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    // --- UTILS ---

    // Permet de transformer un objet de type ContentValues en un objet Item.
    // Cette methode est un type de dictionnaire assez basique qui nous permet de récupérer une valeur à partir d'une clé…
    // Nous placerons ensuite cette valeur dans la propriété correspondante de l'objet Item
    public static Item fromContentValues(ContentValues values) {
        final Item item = new Item();
        if (values.containsKey("text")) item.setText(values.getAsString("text"));
        if (values.containsKey("category")) item.setCategory(values.getAsInteger("category"));
        if (values.containsKey("isSelected")) item.setSelected(values.getAsBoolean("isSelected"));
        if (values.containsKey("userId")) item.setUserId(values.getAsLong("userId"));
        return item;
    }
}