package com.openclassrooms.savemytrip.database;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.openclassrooms.savemytrip.database.dao.ItemDao;
import com.openclassrooms.savemytrip.database.dao.UserDao;
import com.openclassrooms.savemytrip.models.Item;
import com.openclassrooms.savemytrip.models.User;
import java.util.concurrent.Executors;

/**
 * Created by Philippe on 27/02/2018.
 */
    //A class to link between both interfaces (ItemDao & UserDao) and configure our database

    // @Database to link between our classes Item and User
@Database(entities = {Item.class, User.class}, version = 1, exportSchema = false)
public abstract class SaveMyTripDatabase extends RoomDatabase {

    // --- SINGLETON ---
    // Containing a reference to our database
    private static volatile SaveMyTripDatabase INSTANCE;

    // --- DAO ---
    public abstract ItemDao itemDao();

    public abstract UserDao userDao();

    // --- INSTANCE ---
    // Creates a only one and unique RoomDatabase instance (a file containing our SQLite database)
    // if this method is being called twice, it'll only return the reference of our database previously created.
    public static SaveMyTripDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SaveMyTripDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SaveMyTripDatabase.class, "MyDatabase.db")
                            .addCallback(prepopulateDatabase())  // Adds to our builder a Callback returning test user
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback prepopulateDatabase() {
        return new Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                Executors.newSingleThreadExecutor().execute(() -> INSTANCE.userDao().createUser(
                        new User(1, "David",
                                "https://yt3.ggpht.com/ytc/AKedOLQQHbpndYbax4IGSxtVPYY5WLihYQtBYzyMU5Lbbvc=s176-c-k-c0x00ffffff-no-rj")));
            }
        };
    }
}


