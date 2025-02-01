package com.example.demo.task;

import com.example.demo.HTML.BuildImportParams;
import com.example.demo.HTML.Comment;
import com.example.demo.HTML.Component;
import com.example.demo.HTMLBuilder;

public class ComponentBuildTask {

    ComponentBuildTask creator = null;
    Component component = null;
    Comment commentForMainPage;
    BuildImportParams paramsForMainPage;
    HTMLBuilder builder;
    public ComponentBuildTask(ComponentBuildTask creator,String templateName,HTMLBuilder builder){

    }

}
