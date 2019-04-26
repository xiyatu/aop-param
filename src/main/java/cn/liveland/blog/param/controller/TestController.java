package cn.liveland.blog.param.controller;

import cn.liveland.blog.param.aspect.ClearBlank;
import cn.liveland.blog.param.controller.param.TestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author xiyatu
 * @date 2019/4/26 15:02
 * Description
 */
@RestController
@RequestMapping("/test")
public class TestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

    @GetMapping("/test1")
    public void test1(@ClearBlank @RequestParam("param1") String param1,
                      @ClearBlank(isAll = true) @RequestParam("param2") String param2) {
        LOGGER.info("===== Param1={} Param2={}", param1, param2);

    }

    @PostMapping("/test2")
    public void test1(@RequestBody @Valid TestParam param) {
        LOGGER.info("===== Param{}", param);

    }
}
