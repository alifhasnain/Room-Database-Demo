package com.niloy.roomdatabase1;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Info.class} , version = 1)
public abstract class MyDatabase extends RoomDatabase {

    public abstract MyDao myDao();

}
