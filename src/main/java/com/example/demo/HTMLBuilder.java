package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.example.demo.HTML.BuildImportParams;
import com.example.demo.HTML.Comment;
import com.example.demo.task.ComponentBuildTask;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;

@Configuration
public class HTMLBuilder {
    public  Document mainPage;
    public Comment mainPageComment;
    public BuildImportParams mainPageBuildImportParams;

    Element params;
    Element templates;
    Element javaScripts;

    public Queue<ComponentBuildTask> listToDo = new ArrayDeque<>();
    public HTMLBuilder(){
        try {
            mainPage = Jsoup.parse(new File("src/main/resources/static/mainPage.html"),"UTF-8");
            mainPageComment = JSON.parseObject(mainPage.select("comment[id=comment]").text(), Comment.class);
            mainPageBuildImportParams = new BuildImportParams(mainPageComment);
            params = mainPage.select("div[id=all-build-import-params]").first();
            templates = mainPage.select("div[id=all-template]").first();
            javaScripts = mainPage.select("div[id=all-js-for-component]").first();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void begin(){

    }
    public void addClass(){

    }
    public void addParams(){

    }
}