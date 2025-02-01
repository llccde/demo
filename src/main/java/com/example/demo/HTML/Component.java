package com.example.demo.HTML;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Component {
    // 用来保证 index 不会重复
    public static int componentCount = 0;
    // 组件名，即 template 的 id
    public String templateName;
    // 构建参数
    public BuildImportParams buildImportParams;
    // 参数索引,该组件在生成的html文件中的参数索引

    public int component_param_index;

    // 存储 ComponentOrigin 的静态哈希表
    public static Map<String, ComponentOrigin> componentOriginMap = new HashMap<>();

    static {
        // 自动加载 component 目录下的所有组件
        loadComponents("src/main/resources/static/component");
    }

    public static void loadComponents(String directoryPath) {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".html"));
        if (files != null) {
            for (File file : files) {
                ComponentOrigin origin = ComponentOrigin.fromHTML(file.getPath());
                componentOriginMap.put(origin.templateName, origin);
            }
        }
    }

    public static class ComponentOrigin {

        //public JSONObject importParamsJustLike;
        public Comment comment;
        public String templateName;

        public Element templateTag;

        public Element srcTag;

        public static ComponentOrigin fromHTML(String path) {
            ComponentOrigin origin = new ComponentOrigin();
            try {
                Document doc = Jsoup.parse(new File(path), "UTF-8");
                //origin.importParamsJustLike = JSON.parseObject(doc.select("comment[id=importParams]").first().text());
                origin.comment = JSON.parseObject(doc.select("comment[id=comment]").text(), Comment.class);
                origin.templateName = doc.select("template[main=true]").attr("id");
                origin.srcTag = doc.select("script[id=src]").first();
                origin.templateTag = doc.select("template[main=true]").first();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return origin;
        }
    }

    public void print() {
        System.out.println(this.toString());
    }

    public static Component getComponent(String templateName){
        Component component = new Component();
        component.templateName = templateName;

        if(componentOriginMap.get(templateName)==null){
            return null;
        }

        component.buildImportParams = new BuildImportParams(componentOriginMap.get(component.templateName).comment);
        component.component_param_index = componentCount++;
        return component;
    }

    public static void main(String[] args) {
        Component component = new Component();
        component.templateName = "containerTopToBottom";
        component.buildImportParams = new BuildImportParams(componentOriginMap.get(component.templateName).comment);
        component.component_param_index = componentCount++;
        System.out.println(JSON.toJSONString(component, SerializerFeature.PrettyFormat));
    }

}
