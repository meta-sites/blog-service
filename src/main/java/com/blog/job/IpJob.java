package com.blog.job;

import com.blog.util.RestUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.json.Json;
import javax.json.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Component
public class IpJob {

    @Value("${server.frontend.url}")
    private String frontendServer;

    private String ip = "";

    public static void main(String[] args) throws InterruptedException, IOException {
        IpJob a = new IpJob();
        a.updateServerIp();
    }

    private String getIp() throws IOException, InterruptedException {
        String pythonScriptPath = "src/main/resources/open-source/ip.py";
        Process process = Runtime.getRuntime().exec("python " + pythonScriptPath);

        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        String ip = "";
        while ((line = reader.readLine()) != null) {
            ip += line;
        }

        return ip;
    }

//    @Scheduled(cron = "*/5 * * * * *")
    public void updateServerIp() throws IOException, InterruptedException {
        String newIP = getIp();
        if (!ip.equals(newIP)) {
            RestUtil.sendPostRest(frontendServer + "update-ip-server-for-dynamic-ip-change?ip=" + newIP, null, null);
        }
    }
}
