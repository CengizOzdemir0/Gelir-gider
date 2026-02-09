# Gelir-Gider Takip Uygulaması - Veritabanı Yapılandırması

## ✅ Yapılandırma Tamamlandı

`application.yml` dosyası Docker container bilgilerinizle güncellendi.

### PostgreSQL Bağlantısı
- **Host**: gelir-gider-db (Docker container ismi)
- **Port**: 5432
- **Database**: gelir_gider_db
- **Username**: gelir_user
- **Password**: gelir_sifre_123

### Redis Bağlantısı
- **Host**: gelir-gider-redis (Docker container ismi)
- **Port**: 6379
- **Password**: (yok)

### Hibernate Ayarları
- **ddl-auto**: update (Tabloları otomatik oluşturur/günceller)
- **show-sql**: true (SQL sorgularını console'da gösterir)

## 🚀 Uygulamayı Çalıştırma

### 1. Maven ile Build
```bash
mvn clean install
```

### 2. Uygulamayı Başlat
```bash
mvn spring-boot:run
```

### 3. İlk Çalıştırmada Otomatik Oluşturulacaklar

Hibernate aşağıdaki tabloları otomatik oluşturacak:
- ✅ `users` - Kullanıcı bilgileri
- ✅ `roles` - Roller (ROLE_USER, ROLE_ADMIN)
- ✅ `user_roles` - Kullanıcı-rol ilişkisi
- ✅ `categories` - Gelir/gider kategorileri
- ✅ `income` - Gelir kayıtları (index: user_id, year, month)
- ✅ `expense` - Gider kayıtları (index: user_id, year, month)

DataInitializer aşağıdaki verileri otomatik ekleyecek:
- ✅ ROLE_USER ve ROLE_ADMIN rolleri
- ✅ Admin kullanıcısı (admin/admin123)
- ✅ Varsayılan kategoriler (Maaş, Market, Faturalar, vb.)

## 📝 Önemli Notlar

> **Docker Network**
> Java uygulamanız Docker container içinde çalışıyorsa, `gelir-gider-db` ve `gelir-gider-redis` host isimleri çalışacaktır.
> 
> Eğer Java uygulamanız Docker dışında (local) çalışıyorsa, host isimlerini şu şekilde değiştirmeniz gerekir:
> - PostgreSQL: `185.136.206.32:5432`
> - Redis: `185.136.206.32:6379`

## 🔍 Veritabanı Kontrolü

Tabloların oluşup oluşmadığını kontrol etmek için:

```sql
-- PostgreSQL'e bağlan
psql -h 185.136.206.32 -U gelir_user -d gelir_gider_db

-- Tabloları listele
\dt

-- Örnek sorgu
SELECT * FROM users;
SELECT * FROM roles;
SELECT * FROM categories;
```

## 🌐 Uygulama Erişimi

Uygulama başarıyla başladıktan sonra:
- **URL**: http://localhost:8080
- **Login**: admin / admin123

## ⚠️ Sorun Giderme

### Bağlantı Hatası Alırsanız:

1. **PostgreSQL container çalışıyor mu?**
   ```bash
   docker ps | grep gelir_gider_postgres
   ```

2. **Redis container çalışıyor mu?**
   ```bash
   docker ps | grep gelir_gider_redis
   ```

3. **Network bağlantısı var mı?**
   ```bash
   docker network ls
   docker network inspect <network-name>
   ```

4. **Java uygulaması Docker içinde mi?**
   - Evet ise: `gelir-gider-db` ve `gelir-gider-redis` kullanın
   - Hayır ise: `185.136.206.32` kullanın
