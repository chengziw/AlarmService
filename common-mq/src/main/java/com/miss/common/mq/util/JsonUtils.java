package com.miss.common.mq.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class JsonUtils {
    public static JSONObject readQueuesFromClassPath(String path) throws IOException {

        ClassPathResource resource = new ClassPathResource(path);
        BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        StringBuffer json = new StringBuffer();
        String line = null;
        while ((line = br.readLine()) != null) {
            json.append(line);
        }
        String defaultString = json.toString();
        String result = defaultString.replace("\r\n", "");
        return JSON.parseObject(result);
    }
}
