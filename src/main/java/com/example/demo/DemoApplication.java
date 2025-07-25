package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@RestController
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @GetMapping("/")
    public String hello() {
        String hostName;
        try {
            // 获取本地主机的名称，等同于 Go 的 os.Hostname()
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            // 如果获取失败，则返回一个默认值
            hostName = "unknown";
            e.printStackTrace();
        }
        // 使用 String.format 格式化字符串，等同于 Go 的 fmt.Sprintf()
        return String.format("[v3] Hello, Kubernetes!, this is ingress test, host:%s\n", hostName);
    }
}