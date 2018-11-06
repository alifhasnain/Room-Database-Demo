package com.niloy.roomdatabase1;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "infos")
public class Info {

    private String name;

    //We must have one primary on entity
    private int age;
    @PrimaryKey
    @NonNull
    private String id;

    //We can customize Table Column name with this
    @ColumnInfo(name = "user_email")
    private String email;

    public Info() {

    }

    public Info(String name, int age, String id, String email ) {
        this.name = name;
        this.age = age;
        this.id = id;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
