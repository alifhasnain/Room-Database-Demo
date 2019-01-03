package com.niloy.roomdatabase1;


import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface MyDao {

    @Insert
    public void addInfos(Info info);

    /*@Query("select * from infos")
    public List<Info> readUsers();*/

    //This is for sorted read
    @Query("select * from infos order by name")
    public List<Info> readUsers();

    @Delete
    public void deleteItem(Info info);

    @Update
    public void updateItem(Info info);

}
