package com.example.demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

@Configuration
public class HTMLBuilder {
    private final ArrayList<Slot> slots = new ArrayList<>();
    private final ArrayList<Port> ports = new ArrayList<>();



    /**
     * 启动HTML构建过程
     * 已有参数：
     * html%s 参与组装的组件名
     * main 主要组件，即最后在页面上作为基底的组件
     * **/
    public String start(HashMap<String, String> args) {
        ArrayList<Document> htmlList = new ArrayList<>();
        ArrayList<String> names = new ArrayList<>();
        AtomicReference<String> mainDocument = new AtomicReference<>("");

        // 解析输入参数
        args.forEach((key, value) -> {
            if (key.startsWith("html")) {
                htmlList.add(getDocument(value));
                names.add(value);
            }
            if (key.equals("main")) {
                mainDocument.set(value);
            }
        });

        getSlots(htmlList);
        assemble();

        // 返回主文档的HTML内容
        for (int i = 0; i < htmlList.size(); i++) {
            if (names.get(i).equals(mainDocument.get()))
                return htmlList.get(i).html();
        }
        return "";
    }

    // 获取HTML文档
    private Document getDocument(String name) {
        File file = new File("E:\\java\\demo\\src\\main\\resources\\static\\component\\" + name);
        try {
            return Jsoup.parse(file, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Jsoup.parse("<html><p>null</p></html>");
    }

    // 获取所有的插槽和端口
    private void getSlots(ArrayList<Document> htmlList) {
        htmlList.forEach(document -> {
            Elements slotElements = document.select("[slot_type]");
            slotElements.forEach(element -> slots.add(new Slot(document, PortOrSlot.SLOT, element)));

            Elements portElements = document.select("[port_type]");
            portElements.forEach(element -> ports.add(new Port(document, PortOrSlot.PORT, element)));
        });
    }

    // 组装插槽和端口
    private void assemble() {
        slots.forEach(slot -> {
            if ("content".equals(slot.getValue("slot_type"))) {
                HashMap<String, String> arg = new HashMap<>();
                arg.put("port_type", "content");
                arg.put("content_type", slot.getValue("content_type_require"));
                Port port = getPort(arg);
                if (port != null) {
                    slot.connectTo(port);
                }
            }
        });
    }

    // 获取匹配的端口
    private Port getPort(HashMap<String, String> val) {
        return ports.stream()
                .filter(port -> val.entrySet().stream()
                        .allMatch(entry -> entry.getValue().equals(port.element.attr(entry.getKey()))))
                .findFirst()
                .orElse(null);
    }

    // 打印插槽和端口信息
    public void print() {
        System.out.println("SLOTS:\n\n\n");
        slots.forEach(slot -> System.out.println(slot.toString()));
        System.out.println("PORTS:\n\n\n");
        ports.forEach(port -> System.out.println(port.toString()));
    }

    public static void main(String[] args) {
        HTMLBuilder hb = new HTMLBuilder();
        HashMap<String, String> arg = new HashMap<>();
        arg.put("html1", "content_cpp.html");
        arg.put("html2", "smoothscroll_container_with_back_to_top_button.html");
        arg.put("main", "smoothscroll_container_with_back_to_top_button.html");
        String s = hb.start(arg);
        System.out.println(s);
        hb.print();
    }
}

enum PortOrSlot {
    PORT,
    SLOT
}

class SlotAndPortInfo {
    Document source;
    PortOrSlot type;
    HashMap<String, String> attrs;
    Element element;

    public SlotAndPortInfo(Document source, PortOrSlot type, Element element) {
        this.source = source;
        this.type = type;
        this.element = element;
        this.attrs = extractAttributes(element);
    }

    // 提取元素的属性
    private HashMap<String, String> extractAttributes(Element element) {
        HashMap<String, String> attributes = new HashMap<>();
        element.attributes().forEach(attr -> attributes.put(attr.getKey(), attr.getValue()));
        return attributes;
    }

    public String getValue(String key) {
        return attrs.get(key);
    }

    @Override
    public String toString() {
        return "SlotOrPortInfo{" +
                "type=" + type +
                ", attrs=" + attrs +
                ", element=" + element.tagName() + " " + element.attributes() +
                '}';
    }
}

class Port extends SlotAndPortInfo {
    public Port(Document source, PortOrSlot type, Element element) {
        super(source, type, element);
    }
}

class Slot extends SlotAndPortInfo {
    public Port connectedPort;

    public Slot(Document source, PortOrSlot type, Element element) {
        super(source, type, element);
    }

    // 连接到端口
    public void connectTo(Port port) {
        this.connectedPort = port;
        this.element.html(port.element.html());
    }
}