package com.example.administrator.mymemo.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2017/5/5.
 */

public class MemoContentProvider extends ContentProvider {

    private MyOpenHelper mOpenHelper;
    private static final String authority = "com.endless.memo";
    private static final int QUERY_SUCCESS = 0;
    private static final int INSERT_SUCCESS = 1;
    private static final int UPDATE_SUCCESS = 2;
    private static final int DELETE_SUCCESS = 3;
    public static final Uri QUERY_URI = Uri.parse("content://com.endless.memo/query");
    public static final Uri INSERT_URI = Uri.parse("content://com.endless.memo/insert");
    public static final Uri DELETE_URI = Uri.parse("content://com.endless.memo/delete");
    public static final Uri UPDATE_URI = Uri.parse("content://com.endless.memo/update");


    static UriMatcher sMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sMatcher.addURI(authority, "query", QUERY_SUCCESS);
        sMatcher.addURI(authority, "insert", INSERT_SUCCESS);
        sMatcher.addURI(authority, "delete", DELETE_SUCCESS);
        sMatcher.addURI(authority, "update", UPDATE_SUCCESS);

    }

    @Override
    public boolean onCreate() {
        mOpenHelper = MyOpenHelper.getInstance(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (sMatcher.match(uri) == QUERY_SUCCESS) {
            SQLiteDatabase db = mOpenHelper.getReadableDatabase();
            Cursor cursor = db.query("memo", projection, selection, selectionArgs, null, null, sortOrder);
            return cursor;
        }
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (sMatcher.match(uri) == INSERT_SUCCESS) {
            SQLiteDatabase db = mOpenHelper.getReadableDatabase();
            long memo = db.insert("memo", null, values);
            db.close();
            Uri uri2 = Uri.parse("com.endless.insert/" + memo);
            return uri2;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (sMatcher.match(uri) == DELETE_SUCCESS) {
            SQLiteDatabase db = mOpenHelper.getReadableDatabase();
            int memp = db.delete("memo", selection, selectionArgs);
            db.close();
            return memp;
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (sMatcher.match(uri) == UPDATE_SUCCESS) {
            SQLiteDatabase db = mOpenHelper.getReadableDatabase();
            int memo = db.update("memo", values, selection, selectionArgs);
            db.close();
            return memo;
        }
        return 0;
    }
}
