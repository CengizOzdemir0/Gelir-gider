-- Admin kullanıcısını kontrol et ve gerekirse düzelt

-- 1. Admin kullanıcısını kontrol et
SELECT id, username, email, password, enabled, account_non_locked, deleted 
FROM users 
WHERE username = 'admin';

-- 2. Admin kullanıcısının rollerini kontrol et
SELECT u.username, r.name as role_name
FROM users u
LEFT JOIN user_roles ur ON u.id = ur.user_id
LEFT JOIN roles r ON r.id = ur.role_id
WHERE u.username = 'admin';

-- 3. Eğer admin kullanıcısı yoksa veya şifresi yanlışsa, düzelt:
-- NOT: Aşağıdaki şifre "admin123" için BCrypt hash'idir

-- Admin kullanıcısını sil (eğer varsa)
DELETE FROM user_roles WHERE user_id IN (SELECT id FROM users WHERE username = 'admin');
DELETE FROM users WHERE username = 'admin';

-- Admin kullanıcısını yeniden oluştur
-- BCrypt hash for "admin123": $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
INSERT INTO users (username, email, password, full_name, enabled, account_non_locked, created_at, updated_at, deleted)
VALUES ('admin', 'admin@gelir-gider.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Sistem Yöneticisi', true, true, NOW(), NOW(), false);

-- Admin rolünü ekle
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN';

-- Kontrol et
SELECT u.username, u.email, u.enabled, r.name as role
FROM users u
LEFT JOIN user_roles ur ON u.id = ur.user_id
LEFT JOIN roles r ON r.id = ur.role_id
WHERE u.username = 'admin';
