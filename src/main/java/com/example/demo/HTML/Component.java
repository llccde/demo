package com.example.demo.HTML;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;

public class Component {
    //requirement in json which include name and type etc. ,value in JSON
    public HashMap<String,String> importParams;
    //comment about
    public HashMap<String,String> exportParams;
    //slotId,Component object
    public HashMap<String,Component> SlotAndChildComponent;
    public Component fromHTML(){

    }
}
