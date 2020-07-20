package com.miss.common.timer.util.file;

import com.alibaba.fastjson.JSON;
import com.miss.common.timer.po.JobPojo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

@Slf4j
public class JsonUtils {
    public static List<JobPojo> readJobsFromClassPath(String path) {
        try {
            ClassPathResource resource = new ClassPathResource(path);
            BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            StringBuilder json = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                json.append(line);
            }
            String defaultString = json.toString();
            String result = defaultString.replace("\r\n", "");
            return JSON.parseArray(result, JobPojo.class);
        } catch (Exception e) {
            log.error("读取任务信息", "定时器", e, "读取任务信息异常");
        }
        return Collections.emptyList();
    }
}
