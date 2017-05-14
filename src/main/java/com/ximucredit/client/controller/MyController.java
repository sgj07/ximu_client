package com.ximucredit.client.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by guanjie.sun on 2017/5/14.
 */

@Controller
public class MyController {

    private static final Logger logger = LoggerFactory.getLogger(MyController.class);

    @Value("${test.item}")
    private String item;

    @RequestMapping("/test")
    @ResponseBody
    public String test(){
        logger.info("into test");
        return item;
    }


}
