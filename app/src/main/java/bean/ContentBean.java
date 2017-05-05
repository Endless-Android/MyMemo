package bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/3.
 */

public class ContentBean implements Serializable {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private String title; //便签标题
    private String Time;    //便签创建时间
    private String Content;     //便签的内容

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

}
