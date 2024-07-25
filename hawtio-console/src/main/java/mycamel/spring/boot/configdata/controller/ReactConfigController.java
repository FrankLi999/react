package mycamel.spring.boot.configdata.controller;

import lombok.RequiredArgsConstructor;
import mycamel.spring.boot.camel.dto.MyConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/react-config")
public class ReactConfigController {
    @GetMapping(path="", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public MyConfig myConfig() {
        Map config = new HashMap();
        config.put("a", "b");
        config.put("c", "d");
        return MyConfig.builder().config(config).build();
    }

}
