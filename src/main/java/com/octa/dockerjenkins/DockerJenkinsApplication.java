package com.octa.dockerjenkins;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;

@SpringBootApplication
@RestController
public class DockerJenkinsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DockerJenkinsApplication.class, args);
    }


    public int rest(String comand){
        int exitCode = 1;
        try {
            ProcessBuilder builder = new ProcessBuilder();
//            builder.command("sh", "-c", "git add .", "git commit -m \"first commit\"", "git branch -M main", "git remote add origin https://github.com/Octaniel/docker-jenkins.git",
//                    "git push -u origin main");
            builder.command("sh", "-c",comand);
//            builder.directory(new File(System.getProperty("user.home")));
            Process process = builder.start();
            StreamGobbler streamGobbler =
                    new StreamGobbler(process.getInputStream(), System.out::println);
            Executors.newSingleThreadExecutor().submit(streamGobbler);
            exitCode = process.waitFor();
            System.out.println(exitCode);
        }catch (Exception e){
            e.printStackTrace();
        }

        return exitCode;
    }
    @RequestMapping("")
    public String restw(){
        return "QQQQQQQQQQQQQQQQ";
    }

    @RequestMapping("rr")
    public void listFolderStructure(String version) throws Exception {

        Session session = null;
        ChannelExec channel = null;
        int rest = rest("echo OtanielJose3@|sudo -S docker build -t octajs .");
        if(rest==1) return;
        int rest1 = rest("echo OtanielJose3@|sudo -S docker tag octajs otaniel/octajs:"+version);
        if(rest1==1) return;
        int rest2 = rest("echo OtanielJose3@|sudo -S docker push otaniel/octajs:"+version);
        if(rest2==1) return;

        deploy(session, channel, "echo batepa1953@ssh|sudo -S sudo docker rm octajs -f");
        deploy(session, channel, "echo batepa1953@ssh|sudo -S sudo docker run --name octajs -d -p 8082:8081 otaniel/octajs:"+version+" --restart");
        System.out.println("Concluido");

    }

    private void deploy(Session session, ChannelExec channel, String command) throws JSchException, InterruptedException {
        try {
            session = new JSch().getSession("bsoftware", "webapp.b-software.st");
            session.setPassword("batepa1953@ssh");
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand("echo batepa1953@ssh|sudo -S sudo docker run --name octajs -d -p 8082:8081 otaniel/octajs:1.3 --restart");
            ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
            channel.setOutputStream(responseStream);
            channel.connect();

            while (channel.isConnected()) {
                Thread.sleep(100);
            }

            String responseString = responseStream.toString();
            System.out.println(responseString);
        } finally {
            if (session != null) {
                session.disconnect();
            }
            if (channel != null) {
                channel.disconnect();
            }
        }
    }


}
