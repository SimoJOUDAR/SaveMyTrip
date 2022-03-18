package com.openclassrooms.savemytrip;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.test.platform.app.*;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.openclassrooms.savemytrip.database.SaveMyTripDatabase;
import com.openclassrooms.savemytrip.models.Item;
import com.openclassrooms.savemytrip.models.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Philippe on 09/03/2018.
 */
// A test launcher (on a AVD)
@RunWith(AndroidJUnit4.class)
public class ItemDaoTest {

    // FOR DATA
    private SaveMyTripDatabase database;

    // DATA SET FOR TEST
    private static final long USER_ID = 1;
    private static final User USER_DEMO = new User(USER_ID, "Philippe", "https://www.google.fr, "); // creates a demo user
    private static final Item NEW_ITEM_PLACE_TO_VISIT = new Item("Visite cet endroit de rêve !", 0, USER_ID);
    private static final Item NEW_ITEM_IDEA = new Item("On pourrait faire du chien de traîneau ?", 1, USER_ID);
    private static final Item NEW_ITEM_RESTAURANTS = new Item("Ce restaurant à l'air sympa", 2, USER_ID);

    @Rule // the rule below forces a synchronous execution of the test (so as to avoid deporting work to background threads)
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before  // Called before the testing starts, initDb() creates an instance of our database.
    public void initDb() throws Exception {
        // inMemoryDatabaseBuilder() creates in memory (instead of storage) the instance of our DB.
        this.database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getContext(),
                SaveMyTripDatabase.class)
                .allowMainThreadQueries()
                .build();
    }

    @After
    public void closeDb() throws Exception {
        database.close();
    }

    @Test
    public void insertAndGetUser() throws InterruptedException {
        // BEFORE : Adding a new user
        this.database.userDao().createUser(USER_DEMO);
        // TEST
        User user = LiveDataTestUtil.getValue(this.database.userDao().getUser(USER_ID));
        assertTrue(user.getUsername().equals(USER_DEMO.getUsername()) && user.getId() == USER_ID);
    }

    @Test
    public void getItemsWhenNoItemInserted() throws InterruptedException {
        // TEST
        List<Item> items = LiveDataTestUtil.getValue(this.database.itemDao().getItems(USER_ID));
        assertTrue(items.isEmpty());
    }

    @Test
    public void insertAndGetItems() throws InterruptedException {
        // BEFORE : Adding demo user & demo items

        this.database.userDao().createUser(USER_DEMO);
        this.database.itemDao().insertItem(NEW_ITEM_PLACE_TO_VISIT);
        this.database.itemDao().insertItem(NEW_ITEM_IDEA);
        this.database.itemDao().insertItem(NEW_ITEM_RESTAURANTS);

        // TEST
        List<Item> items = LiveDataTestUtil.getValue(this.database.itemDao().getItems(USER_ID));
        assertTrue(items.size() == 3);
    }

    @Test
    public void insertAndUpdateItem() throws InterruptedException {
        // BEFORE : Adding demo user & demo items. Next, update item added & re-save it
        this.database.userDao().createUser(USER_DEMO);
        this.database.itemDao().insertItem(NEW_ITEM_PLACE_TO_VISIT);
        Item itemAdded = LiveDataTestUtil.getValue(this.database.itemDao().getItems(USER_ID)).get(0);
        itemAdded.setSelected(true);
        this.database.itemDao().updateItem(itemAdded);

        //TEST
        List<Item> items = LiveDataTestUtil.getValue(this.database.itemDao().getItems(USER_ID));
        assertTrue(items.size() == 1 && items.get(0).getSelected());
    }

    @Test
    public void insertAndDeleteItem() throws InterruptedException {
        // BEFORE : Adding demo user & demo item. Next, get the item added & delete it.
        this.database.userDao().createUser(USER_DEMO);
        this.database.itemDao().insertItem(NEW_ITEM_PLACE_TO_VISIT);
        Item itemAdded = LiveDataTestUtil.getValue(this.database.itemDao().getItems(USER_ID)).get(0);
        this.database.itemDao().deleteItem(itemAdded.getId());

        //TEST
        List<Item> items = LiveDataTestUtil.getValue(this.database.itemDao().getItems(USER_ID));
        assertTrue(items.isEmpty());
    }
}
