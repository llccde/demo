package com.example.demo.HTML;

import com.alibaba.fastjson.JSON;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;

import java.util.ArrayList;

public class BuildImportParams {
    public ArrayList<ImportParamInBuild> params = new ArrayList<>();

    public BuildImportParams(Comment comment) {
        for (ImportParamInComment importParam : comment.importParams) {
            ImportParamInBuild paramInBuild = new ImportParamInBuild(importParam);
            params.add(paramInBuild);
        }
    }

    public Element getParamsTag(boolean isMainPage, int component_param_index) {
        Element commentTag = new Element(Tag.valueOf("comment"), "");
        commentTag.attr("id", "importParams");
        commentTag.attr("component_param_index", String.valueOf(component_param_index));
        if (isMainPage) {
            commentTag.attr("for_root", "true");
        }
        commentTag.attr("style", "visibility: hidden");

        String jsonContent = JSON.toJSONString(this, true);
        commentTag.text(jsonContent);
        return commentTag;
    }
}
