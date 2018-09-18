package com.application.system.core.orm;

/**
 * @auther ttm
 * @date 2018/9/17
 */
import java.io.Serializable;

public class Id implements Serializable {
    private String id;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
