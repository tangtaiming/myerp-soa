package com.application.system.core.controller;

import com.application.system.core.controller.vo.ServiceResponseVo;
import com.application.system.core.libraries.utils.Json;
import com.application.system.core.orm.ServiceResponseCode;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @auther ttm
 * @date 2018/9/17
 */
public class MyErpController {

    private String request = "";
    private Object result;
    private Integer responseCode;
    private String responseDescription;

    protected final String COLLECTION;
    protected final String COLLECTION_COUNT;
    protected final String RESULT;
    protected final String SUCCESS;

    public MyErpController() {
        this.responseCode = ServiceResponseCode.SUCCESS;
        this.responseDescription = "Success";
        this.COLLECTION = "collection";
        this.COLLECTION_COUNT = "collectionCount";
        this.RESULT = "result";
        this.SUCCESS = "success";
    }

    public void _execute() throws Exception {
    }

    public String getJsonData() {
        return Json.toJson(buildServiceResponse(responseCode, responseDescription, result));
    }

    public ServiceResponseVo getResponse() {
        return buildServiceResponse(responseCode, responseDescription, result);
    }

    public Object getResult() {
        return this.result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Integer getResponseCode() {
        return this.responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return this.responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public Map<String, Object> getRequest() {
        return null == this.request ? null : (Map) Json.fromJson(this.request, LinkedHashMap.class);
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public ServiceResponseVo buildServiceResponse(Integer responseCode, String responseDescription, Object result) {
        ServiceResponseVo responseVo = new ServiceResponseVo();
        this.responseCode = responseCode;
        this.responseDescription = responseDescription;
        this.result = result;
        return responseVo;
    }

}
