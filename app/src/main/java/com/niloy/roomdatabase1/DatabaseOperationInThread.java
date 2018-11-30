package com.niloy.roomdatabase1;

import android.os.AsyncTask;

import java.util.List;

public class DatabaseOperationInThread {

    private static volatile List<Info> userData;

    public static void addData(final Info info)   {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MainActivity.database.myDao().addInfos(info);
            }
        }).start();
    }

    public static void deleteData(final Info info)    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MainActivity.database.myDao().deleteItem(info);
            }
        }).start();
    }

    public static List<Info> readAllUsers() {
        GetUserData getUserData = new GetUserData();
        try {
            return getUserData.execute().get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static class GetUserData extends AsyncTask<Void,Void,List<Info>>    {
        @Override
        protected List<Info> doInBackground(Void... voids) {
            return MainActivity.database.myDao().readUsers();
        }
    }


    public static void updateData(final Info info) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MainActivity.database.myDao().updateItem(info);
            }
        }).start();
    }

}
