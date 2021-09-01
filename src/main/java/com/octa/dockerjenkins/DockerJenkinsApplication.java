package com.octa.dockerjenkins;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;

@SpringBootApplication
@RestController
public class DockerJenkinsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DockerJenkinsApplication.class, args);
    }

    @RequestMapping("")
    public String rest(String comand){
        try {
            ProcessBuilder builder = new ProcessBuilder();
//            builder.command("sh", "-c", "git add .", "git commit -m \"first commit\"", "git branch -M main", "git remote add origin https://github.com/Octaniel/docker-jenkins.git",
//                    "git push -u origin main");
            builder.command("sh", "-c", comand);
//            builder.directory(new File(System.getProperty("user.home")));
            Process process = builder.start();
            StreamGobbler streamGobbler =
                    new StreamGobbler(process.getInputStream(), System.out::println);
            Executors.newSingleThreadExecutor().submit(streamGobbler);
            int exitCode = process.waitFor();
            System.out.println(exitCode);
        }catch (Exception e){
            e.printStackTrace();
        }

        return "UUUU";
    }

}
