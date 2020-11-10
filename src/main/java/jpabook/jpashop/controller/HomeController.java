package jpabook.jpashop.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class HomeController {

    //lombok에서 @Slf4j로 지원 해준다.
    //Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping("/")
    public String home() {
        log.info("home confroller");
        return "home";
    }
}
