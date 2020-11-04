package net.cpollet.gallery;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

@Slf4j
public class Webapp {
    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("target");
        tomcat.setAddDefaultWebXmlToWebapp(false);
        tomcat.setConnector(tomcat.getConnector());
        tomcat.setPort(getPort());

        String webappDirLocation = ".";

        log.info("configuring app with basedir: {}", new File("./" + webappDirLocation).getAbsolutePath());

        tomcat.addWebapp(
                "",
                new File(webappDirLocation).getAbsolutePath()
        );

        tomcat.start();
        tomcat.getServer().await();
    }

    private static Integer getPort() {
        String webPort = System.getenv("TOMCAT_PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }
        return Integer.valueOf(webPort);
    }
}
