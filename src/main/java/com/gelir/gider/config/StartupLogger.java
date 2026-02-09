package com.gelir.gider.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StartupLogger {

    private final Environment environment;

    public StartupLogger(Environment environment) {
        this.environment = environment;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void logApplicationStartup() {
        String port = environment.getProperty("server.port", "8080");
        String contextPath = environment.getProperty("server.servlet.context-path", "");

        log.info("=".repeat(60));
        log.info("💰 Gelir-Gider Takip Uygulaması Başarıyla Başlatıldı!");
        log.info("=".repeat(60));
        log.info("🌐 Uygulama Adresi: http://localhost:{}{}", port, contextPath);
        log.info("📝 Login Sayfası:   http://localhost:{}{}/login", port, contextPath);
        log.info("✨ Kayıt Sayfası:   http://localhost:{}{}/register", port, contextPath);
        log.info("=".repeat(60));
    }
}
