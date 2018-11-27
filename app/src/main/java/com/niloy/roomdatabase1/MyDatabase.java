package com.niloy.roomdatabase1;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Info.class} , version = 1)
public abstract class MyDatabase extends RoomDatabase {

    public abstract MyDao myDao();

}
