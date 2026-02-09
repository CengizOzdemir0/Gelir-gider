-- Admin kullanıcısının şifresini güncelle
-- Bu script admin kullanıcısının şifresini "admin123" olarak ayarlar

UPDATE users 
SET password = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    enabled = true,
    account_non_locked = true
WHERE username = 'admin';

-- Kontrol et
SELECT username, email, enabled, account_non_locked, 
       CASE WHEN password = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy' 
            THEN 'Şifre doğru (admin123)' 
            ELSE 'Şifre farklı' 
       END as password_status
FROM users 
WHERE username = 'admin';
