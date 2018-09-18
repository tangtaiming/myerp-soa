package com.application.system.core.controller.vo;

import com.application.system.core.orm.ServiceResponseCode;

/**
 * @auther ttm
 * @date 2018/9/17
 */
public class ServiceResponseVo {

    private Double version = Double.valueOf(1.0D);
    private Integer code;
    private String description;
    private Object result;

    public ServiceResponseVo() {
        this.code = ServiceResponseCode.SUCCESS;
        this.description = "Success";
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getResult() {
        return this.result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Double getVersion() {
        return this.version;
    }

}
