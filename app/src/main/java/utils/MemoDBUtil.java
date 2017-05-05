package utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import bean.ContentBean;
import db.MemoContentProvider;

/**
 * Created by Administrator on 2017/5/5.
 */

public class MemoDBUtil {

    private static int ID = 0;
    /**
     * 添加新的memo
     */
    public static boolean addmemo(Context context, String title, String Content, String time) {

            ContentValues values = new ContentValues();
            values.put(TableUtil.COL_ID, ID);
            values.put(TableUtil.COL_TITLE, title);
            values.put(TableUtil.COL_CONTENT, Content);
            values.put(TableUtil.COL_CREATE_DATE, time);
            context.getContentResolver().insert(MemoContentProvider.INSERT_URI, values);
            ID+=1;
        return true;

    }


    public static ArrayList<ContentBean> getlist(Context context) {
        Cursor query = context.getContentResolver().query(MemoContentProvider.QUERY_URI, null, null, null, null);
        ArrayList<ContentBean> list = new ArrayList<>();
        for (query.moveToFirst(); !query.isAfterLast(); query.moveToNext()) {
            ContentBean memo = new ContentBean();
            memo.setId(query.getInt(0));
            memo.setTitle(query.getString(1));
            memo.setContent(query.getString(2));
            memo.setTime(query.getString(3));
            list.add(memo);
        }
        query.close();
        return list;
    }

    public static boolean deletememo(Context context, String condition) {
        context.getContentResolver().delete(MemoContentProvider.DELETE_URI, "title=?", new String[]{condition});
        return true;
    }

    public static boolean update(Context context,String title, String Content, String time,int id){
        ContentValues values = new ContentValues();
        values.put(TableUtil.COL_CONTENT,Content);
        values.put(TableUtil.COL_TITLE,title);
        values.put(TableUtil.COL_CREATE_DATE,time);
        int update = context.getContentResolver().update(MemoContentProvider.UPDATE_URI, values, "id=?", new String[]{String.valueOf(id)});
        return true;
    }


    public static boolean deletememoid(Context context, int id) {
        context.getContentResolver().delete(MemoContentProvider.DELETE_URI, "id=?", new String[]{String.valueOf(id)});
        return true;
    }

}
