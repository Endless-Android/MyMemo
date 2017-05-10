package bean;

/**
 * Created by Administrator on 2017/5/8.
 */

public class EventBusMessage {
    private String mMsg;
    public EventBusMessage(String msg) {
        mMsg = msg;
    }
    public String getMsg(){
        return mMsg;
    }
}
