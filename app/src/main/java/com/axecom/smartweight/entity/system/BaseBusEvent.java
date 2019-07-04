package com.axecom.smartweight.entity.system;

import java.io.Serializable;

/**
 * author: luofaxin
 * date： 2018/10/17 0017.
 * email:424533553@qq.com
 * describe:事件总线
 */
public class BaseBusEvent {
    private String eventType;
    private Object other;
    private Serializable serializable;

    public Serializable getSerializable() {
        return serializable;
    }

    public void setSerializable(Serializable serializable) {
        this.serializable = serializable;
    }

    public BaseBusEvent() {
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Object getOther() {
        return other;
    }

    //可以是实体对象
    public void setOther(Object other) {
        this.other = other;
    }
}

