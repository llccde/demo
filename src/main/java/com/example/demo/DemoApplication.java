package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		// 启动Spring Boot应用程序
		SpringApplication.run(DemoApplication.class, args);
	}

}

@RestController
class WelcomeController {

	@GetMapping("/welcome")
	public String welcome() {
		return "欢迎使用Spring Boot!";
	}
}

// 定义一个控制器来处理HTML组合请求
@Controller
class HTMLController {

	@Autowired
	private HTMLBuilder htmlBuilder;
	//示例请求http://localhost:8089/combine?html1=content_java.html&html2=smoothscroll_container_with_back_to_top_button.html&main=smoothscroll_container_with_back_to_top_button.html


	@GetMapping("/combine")
	@ResponseBody
	public String combine(@RequestParam HashMap<String, String> params) {
		return htmlBuilder.start(params);
	}
}
