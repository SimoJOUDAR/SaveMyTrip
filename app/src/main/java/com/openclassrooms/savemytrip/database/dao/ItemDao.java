package com.openclassrooms.savemytrip.database.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.savemytrip.models.Item;

import java.util.List;

/**
 * Created by Philippe on 27/02/2018.
 */
    // This class is a Items table

@Dao   // To indicate that this is an DAO class
public interface ItemDao {

    // @Query to declare getItems() as an SQL request
    // It returns all the Items in the table where userId = userId
    // Used in UI updaters
    @Query("SELECT * FROM Item WHERE userId = :userId")
    LiveData<List<Item>> getItems(long userId);

    // Pour le ContentProvider. Retourne un objet de type Cursor, plus facilement manipulable par le ContentProvider.
    // Used to share data with other apps
    @Query("SELECT * FROM Item WHERE userId = :userId")
    Cursor getItemsWithCursor(long userId);

    @Insert
    long insertItem(Item item);

    @Update
    int updateItem(Item item);

    // there's also the possibility to use @Delete while passing an item to it
    @Query("DELETE FROM Item WHERE id = :itemId")
    int deleteItem(long itemId);
}
