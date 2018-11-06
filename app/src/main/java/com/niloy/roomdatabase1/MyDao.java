package com.niloy.roomdatabase1;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface MyDao {

    @Insert
    public void addInfos(Info info);

    @Query("select * from infos")
    public List<Info> readUsers();

    @Delete
    public void deleteItem(Info info);

    @Update
    public void updateItem(Info info);

}
