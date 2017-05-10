package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/5/5.
 */

public class MyOpenHelper extends SQLiteOpenHelper {
    private static MyOpenHelper mMyOpenHelper;
    private final String CREATE_TABLE = "create table if not exists memo (id INTEGER PRIMARY KEY AUTOINCREMENT, title varchar(40), content text, createtime varchar(40), modifytime varchar(40), isdel int default 0)";

    private MyOpenHelper(Context context) {
        super(context, "memo", null, 1);
    }

    public static MyOpenHelper getInstance(Context context) {
        if (mMyOpenHelper == null) {
            mMyOpenHelper = new MyOpenHelper(context);
        }
        return mMyOpenHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
