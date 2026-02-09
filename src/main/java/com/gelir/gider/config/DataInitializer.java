package com.gelir.gider.config;

import com.gelir.gider.entity.Category;
import com.gelir.gider.entity.Role;
import com.gelir.gider.entity.User;
import com.gelir.gider.enums.CategoryType;
import com.gelir.gider.repository.CategoryRepository;
import com.gelir.gider.repository.RoleRepository;
import com.gelir.gider.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Override
    public void run(String... args) {
        log.info("=".repeat(60));
        log.info("🔧 Veritabanı Başlatma İşlemi Başladı");
        log.info("=".repeat(60));

        initializeRoles();
        initializeAdminUser();
        initializeDefaultCategories();

        log.info("=".repeat(60));
        log.info("✅ Veri başlatma tamamlandı");
        log.info("=".repeat(60));
    }

    private void initializeRoles() {
        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            Role userRole = Role.builder()
                    .name("ROLE_USER")
                    .description("Normal kullanıcı rolü")
                    .build();
            roleRepository.save(userRole);
            log.info("ROLE_USER oluşturuldu");
        }

        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            Role adminRole = Role.builder()
                    .name("ROLE_ADMIN")
                    .description("Yönetici rolü")
                    .build();
            roleRepository.save(adminRole);
            log.info("ROLE_ADMIN oluşturuldu");
        }
    }

    private void initializeAdminUser() {
        log.info("👤 Admin kullanıcısı kontrol ediliyor...");

        if (userRepository.findByUsernameAndDeletedFalse(adminUsername).isEmpty()) {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("ROLE_ADMIN bulunamadı"));

            User admin = User.builder()
                    .username(adminUsername)
                    .password(passwordEncoder.encode(adminPassword))
                    .email(adminEmail)
                    .fullName("Sistem Yöneticisi")
                    .enabled(true)
                    .accountNonLocked(true)
                    .build();

            admin.addRole(adminRole);
            userRepository.save(admin);
            log.info("✅ Admin kullanıcısı oluşturuldu:");
            log.info("   Username: {}", adminUsername);
            log.info("   Password: {}", adminPassword);
            log.info("   Email: {}", adminEmail);
        } else {
            User existingAdmin = userRepository.findByUsernameAndDeletedFalse(adminUsername).get();
            log.info("ℹ️  Admin kullanıcısı zaten mevcut:");
            log.info("   Username: {}", existingAdmin.getUsername());
            log.info("   Email: {}", existingAdmin.getEmail());
            log.info("   Enabled: {}", existingAdmin.getEnabled());
            log.info("   Account Non Locked: {}", existingAdmin.getAccountNonLocked());
        }
    }

    private void initializeDefaultCategories() {
        // Gelir kategorileri
        createCategoryIfNotExists("Maaş", CategoryType.INCOME, "Aylık maaş geliri");
        createCategoryIfNotExists("Freelance", CategoryType.INCOME, "Serbest çalışma geliri");
        createCategoryIfNotExists("Yatırım", CategoryType.INCOME, "Yatırım gelirleri");
        createCategoryIfNotExists("Kira", CategoryType.INCOME, "Kira geliri");
        createCategoryIfNotExists("Diğer Gelir", CategoryType.INCOME, "Diğer gelir kaynakları");

        // Gider kategorileri
        createCategoryIfNotExists("Kira", CategoryType.EXPENSE, "Ev kirası");
        createCategoryIfNotExists("Market", CategoryType.EXPENSE, "Market alışverişi");
        createCategoryIfNotExists("Faturalar", CategoryType.EXPENSE, "Elektrik, su, doğalgaz vb.");
        createCategoryIfNotExists("Ulaşım", CategoryType.EXPENSE, "Ulaşım giderleri");
        createCategoryIfNotExists("Sağlık", CategoryType.EXPENSE, "Sağlık giderleri");
        createCategoryIfNotExists("Eğlence", CategoryType.EXPENSE, "Eğlence ve hobi");
        createCategoryIfNotExists("Giyim", CategoryType.EXPENSE, "Giyim giderleri");
        createCategoryIfNotExists("Diğer Gider", CategoryType.EXPENSE, "Diğer giderler");

        log.info("Varsayılan kategoriler oluşturuldu");
    }

    private void createCategoryIfNotExists(String name, CategoryType type, String description) {
        if (categoryRepository.findByTypeAndUserIdIsNullAndDeletedFalse(type).stream()
                .noneMatch(c -> c.getName().equals(name))) {
            Category category = Category.builder()
                    .name(name)
                    .type(type)
                    .description(description)
                    .build();
            categoryRepository.save(category);
        }
    }
}
