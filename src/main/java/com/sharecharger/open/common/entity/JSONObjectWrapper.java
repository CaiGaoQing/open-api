package com.sharecharger.open.common.entity;

import com.alibaba.fastjson.JSONObject;


/**
 * @Author 蔡高情
 * @Description
 * JSON.parseObject(sb.toString())映射过去。
 * 但是由于JSONObject实现了Map结果，
 * 所以Spring MVC的默认处理器MapMethodProcessor会先起作用，
 * 这样就不能正常的映射成JSONObject对象了。
 * 没有办法做了一个简单的JSONObject包装类，以使MapMethodProcessor不能对其进行处理。
 **/
public class JSONObjectWrapper {
    private JSONObject jsonObject;

    public JSONObjectWrapper(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public JSONObject getJSONObject() {
        return jsonObject;
    }
}