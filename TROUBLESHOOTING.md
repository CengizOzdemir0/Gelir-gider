# Authentication Sorun Giderme Kılavuzu

## Sorun
Kullanıcı doğru kullanıcı adı ve şifre girdiğinde `BadCredentialsException` hatası alınıyor.

## Yapılan Düzeltmeler

### 1. JWT Filter Güncellendi
- `shouldNotFilter()` metodu eklendi
- `/auth/**` endpoint'leri JWT kontrolünden muaf tutuldu
- Login ve register işlemleri artık JWT gerektirmiyor

### 2. DataInitializer Düzeltildi
- `findByUsername()` yerine `findByUsernameAndDeletedFalse()` kullanılıyor
- Detaylı loglama eklendi

### 3. Detaylı Loglama
- Admin kullanıcısı oluşturulduğunda/bulunduğunda bilgi loglanıyor
- Veritabanı başlatma süreci takip edilebiliyor

## Test Adımları

### 1. Uygulamayı Yeniden Başlatın
```bash
# Mevcut uygulamayı durdurun (Ctrl+C)
# Yeniden başlatın
mvn spring-boot:run
```

### 2. Logları Kontrol Edin
Uygulama başlarken şu logları görmeli siniz:

```
============================================================
🔧 Veritabanı Başlatma İşlemi Başladı
============================================================
👤 Admin kullanıcısı kontrol ediliyor...
✅ Admin kullanıcısı oluşturuldu:
   Username: admin
   Password: admin123
   Email: admin@gelir-gider.com
============================================================
✅ Veri başlatma tamamlandı
============================================================
```

### 3. Veritabanını Kontrol Edin
```sql
-- PostgreSQL'e bağlanın
psql -h 185.136.206.32 -U gelir_user -d gelir_gider_db

-- Kullanıcıları listeleyin
SELECT id, username, email, enabled, account_non_locked FROM users;

-- Admin kullanıcısının rollerini kontrol edin
SELECT u.username, r.name as role_name
FROM users u
JOIN user_roles ur ON u.id = ur.user_id
JOIN roles r ON r.id = ur.role_id
WHERE u.username = 'admin';
```

### 4. Login Testi
1. `http://localhost:1818/login` adresine gidin
2. Kullanıcı adı: `admin`
3. Şifre: `admin123`
4. Giriş Yap butonuna tıklayın

## Olası Sorunlar ve Çözümleri

### Sorun 1: Admin kullanıcısı oluşturulmamış
**Çözüm:** Veritabanındaki users tablosunu temizleyin ve uygulamayı yeniden başlatın
```sql
DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM roles;
```

### Sorun 2: Şifre hash'i yanlış
**Çözüm:** Admin kullanıcısını manuel olarak güncelleyin
```sql
-- BCrypt hash: admin123
UPDATE users 
SET password = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'
WHERE username = 'admin';
```

### Sorun 3: Kullanıcı disabled durumda
**Çözüm:**
```sql
UPDATE users 
SET enabled = true, account_non_locked = true
WHERE username = 'admin';
```

### Sorun 4: JWT Filter hala çalışıyor
**Çözüm:** Uygulamayı tamamen durdurup yeniden başlatın. Bazen hot-reload düzgün çalışmayabilir.

## Browser Console'da Hata Kontrolü
1. F12 ile Developer Tools'u açın
2. Network tab'ına gidin
3. Login butonuna tıklayın
4. `/auth/login` isteğine tıklayın
5. Response tab'ında hata mesajını kontrol edin

## Başarılı Login Response
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "admin",
  "fullName": "Sistem Yöneticisi",
  "roles": ["ROLE_ADMIN", "ROLE_USER"]
}
```

## İletişim
Sorun devam ederse:
1. Uygulama başlangıç loglarını paylaşın
2. `/auth/login` isteğinin Response'unu paylaşın
3. Veritabanı sorgu sonuçlarını paylaşın
