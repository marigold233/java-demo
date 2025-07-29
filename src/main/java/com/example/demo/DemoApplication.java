package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication
@RestController
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    /**
     * 私有辅助方法，用于读取文件内容。
     * 这等同于 Go 代码中的 readFile 函数。
     * @param fileName 要读取的文件名
     * @return 文件内容
     */
    private String readFileContent(String fileName) {
        // Go 代码中的路径是硬编码的，我们在这里也一样
        String filePath = "/etc/configmap_vol/" + fileName;
        try {
            // 使用 Java NIO 读取文件，这是现代 Java 的推荐方式
            return Files.readString(Path.of(filePath));
        } catch (IOException e) {
            // Go 代码中使用了 panic，这会使程序崩溃。
            // 在 Java 中，抛出一个 RuntimeException 是最接近的行为，
            // 它会中断当前请求的处理并返回一个 500 错误。
            throw new RuntimeException("Failed to read file: " + filePath, e);
        }
    }

    @GetMapping("/")
    public String hello() {
        // 1. 获取 Hostname (等同于 Go 的 os.Hostname())
        String hostName;
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            hostName = "unknown";
            e.printStackTrace();
        }

        // 2. 读取环境变量 (等同于 Go 的 os.Getenv("DB_URL"))
        String dbUrl = System.getenv("DB_URL");
        if (dbUrl == null) {
            dbUrl = ""; // 如果环境变量不存在，返回空字符串，以匹配Go的行为
        }

        // 3. 读取配置文件内容
        String appConfigContent = readFileContent("app-config.json");

        // 4. 格式化并返回最终的字符串 (等同于 Go 的 fmt.Sprintf())
        // 注意：我们使用了 Go 代码中的 v4 版本号
        return String.format(
            "[v4] Hello, Kubernetes! From host: %s\n" +
            "Get Database Connect URL: %s\n" +
            "app-config.json:%s",
            hostName,
            dbUrl,
            appConfigContent
        );
    }
}
