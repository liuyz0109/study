package com.rabbitmqdispatcher.rabbitmqdispatcher.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author <a href="mailto:liuyaozong@gtmap.cn">liuyaozong</a>
 * @version 1.0, 2021/5/6
 * @description
 */
public class Dispatcher implements Serializable {
    private String dispatcherID;
    private String orderId;
    private String text;
    private String userId;
    private Date date;

    public String getDispatcherID() {
        return dispatcherID;
    }

    public void setDispatcherID(String dispatcherID) {
        this.dispatcherID = dispatcherID;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Dispatcher{" +
                "dispatcherID='" + dispatcherID + '\'' +
                ", orderId='" + orderId + '\'' +
                ", text='" + text + '\'' +
                ", userId='" + userId + '\'' +
                ", date=" + date +
                '}';
    }
}
