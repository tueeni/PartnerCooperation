package com.example.partnercorporation;


import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebController {

    @Autowired
    private RuntimeService runtimeService;

    @GetMapping("/start-process")
    @ResponseBody
    public String startProcess() {
        runtimeService.startProcessInstanceByKey("helloWorldProcess");
        return "Process started!";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
