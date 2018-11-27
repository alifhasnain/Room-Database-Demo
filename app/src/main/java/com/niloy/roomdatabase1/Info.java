package com.niloy.roomdatabase1;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

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

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    @NonNull
    private byte[] image;

    public Info() {

    }

    public Info(String name, int age, @NonNull String id, String email, byte[] image) {
        this.name = name;
        this.age = age;
        this.id = id;
        this.email = email;
        this.image = image;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
