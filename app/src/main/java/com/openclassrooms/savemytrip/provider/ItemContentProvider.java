package com.openclassrooms.savemytrip.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.openclassrooms.savemytrip.database.SaveMyTripDatabase;
import com.openclassrooms.savemytrip.models.Item;

/**
 * Created by Philippe on 14/03/2018.
 */

public class ItemContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.openclassrooms.savemytrip.provider";
    public static final String TABLE_NAME = Item.class.getSimpleName();
    public static final Uri URI_ITEM = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

    @Override
    // Représente le point d'entrée du ContentProvider. Vous pourrez donc y initialiser
    // différentes variables qui vous serviront par la suite.
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    // Permet, à partir d'une URI renseignée, de récupérer les données (via un Cursor) depuis
    // la destination de votre choix (dans notre cas, notre base de données SQLite).
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        if (getContext() != null) {
            long userId = ContentUris.parseId(uri);
            final Cursor cursor = SaveMyTripDatabase.getInstance(getContext()).itemDao().getItemsWithCursor(userId);
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
            return cursor;
        }

        throw new IllegalArgumentException("Failed to query row for uri " + uri);
    }

    @Nullable
    @Override
    // Permet de retourner le type MIME associé à l'URI permettant d'identifier plus précisément
    // le type des données qui seront retournées.
    public String getType(@NonNull Uri uri) {
        return "vnd.android.cursor.item/" + AUTHORITY + "." + TABLE_NAME;
    }

    @Nullable
    @Override
    // Permet, à partir d'une URI renseignée, d'insérer des données au format ContentValues dans
    // la destination de notre choix (dans notre cas, notre base de données SQLite).
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        if (getContext() != null && contentValues != null) {
            final long id = SaveMyTripDatabase.getInstance(getContext()).itemDao().insertItem(Item.fromContentValues(contentValues));
            if (id != 0) {
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            }
        }

        throw new IllegalArgumentException("Failed to insert row into " + uri);
    }

    @Override
    // Permet, à partir d'une URI renseignée, de supprimer des données au format ContentValues de
    // la destination de notre choix (dans notre cas, notre base de données SQLite).
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        if (getContext() != null) {
            final int count = SaveMyTripDatabase.getInstance(getContext()).itemDao().deleteItem(ContentUris.parseId(uri));
            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        }
        throw new IllegalArgumentException("Failed to delete row into " + uri);
    }

    @Override
    // Permet, à partir d'une URI renseignée, de mettre à jour des données au format ContentValues
    // dans la destination de notre choix (dans notre cas, notre base de données SQLite).
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        if (getContext() != null && contentValues != null) {
            final int count = SaveMyTripDatabase.getInstance(getContext()).itemDao().updateItem(Item.fromContentValues(contentValues));
            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        }
        throw new IllegalArgumentException("Failed to update row into " + uri);
    }
}
