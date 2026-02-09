# Gelir-Gider Takip Uygulaması

Modern ve güvenli bir gelir-gider takip uygulaması. Java 21, Spring Boot 3.x, PostgreSQL ve Redis teknolojileri kullanılarak geliştirilmiştir.

## 🚀 Özellikler

- ✅ JWT tabanlı güvenli kimlik doğrulama
- ✅ Redis ile token yönetimi
- ✅ Rol tabanlı yetkilendirme (USER, ADMIN)
- ✅ Aylık gelir-gider takibi
- ✅ Kategori yönetimi
- ✅ Otomatik aylık özet hesaplama
- ✅ Modern ve responsive web arayüzü
- ✅ RESTful API
- ✅ Soft delete desteği

## 📋 Gereksinimler

- Java 21
- Maven 3.6+
- PostgreSQL 12+
- Redis 6+

## ⚙️ Kurulum

### 1. Veritabanı Yapılandırması

PostgreSQL'de bir veritabanı oluşturun:

```sql
CREATE DATABASE gelir_gider_db;
```

### 2. Uygulama Yapılandırması

`src/main/resources/application.yml` dosyasını düzenleyin:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/gelir_gider_db
    username: your_username
    password: your_password
  
  data:
    redis:
      host: localhost
      port: 6379
      password: your_redis_password  # Eğer varsa
```

### 3. Uygulamayı Çalıştırma

```bash
# Projeyi derleyin
mvn clean install

# Uygulamayı başlatın
mvn spring-boot:run
```

Uygulama `http://localhost:8080` adresinde çalışacaktır.

## 👤 Varsayılan Kullanıcı

Uygulama ilk çalıştırıldığında otomatik olarak bir admin kullanıcısı oluşturulur:

- **Kullanıcı Adı:** admin
- **Şifre:** admin123

## 📚 API Endpoints

### Kimlik Doğrulama
- `POST /auth/login` - Giriş yap
- `POST /auth/logout` - Çıkış yap

### Gelir Yönetimi
- `POST /api/income` - Yeni gelir ekle
- `GET /api/income/month/{year}/{month}` - Aylık gelirleri listele
- `PUT /api/income/{id}` - Gelir güncelle
- `DELETE /api/income/{id}` - Gelir sil

### Gider Yönetimi
- `POST /api/expense` - Yeni gider ekle
- `GET /api/expense/month/{year}/{month}` - Aylık giderleri listele
- `PUT /api/expense/{id}` - Gider güncelle
- `DELETE /api/expense/{id}` - Gider sil

### Özet
- `GET /api/summary/month/{year}/{month}` - Aylık özet

### Kategori
- `GET /api/categories/income` - Gelir kategorileri
- `GET /api/categories/expense` - Gider kategorileri

### Admin (Sadece ADMIN rolü)
- `GET /api/admin/users` - Tüm kullanıcıları listele
- `POST /api/admin/users` - Yeni kullanıcı oluştur
- `PUT /api/admin/users/{id}` - Kullanıcı güncelle
- `DELETE /api/admin/users/{id}` - Kullanıcı sil
- `GET /api/admin/categories` - Tüm kategorileri listele
- `POST /api/admin/categories` - Yeni kategori oluştur
- `PUT /api/admin/categories/{id}` - Kategori güncelle
- `DELETE /api/admin/categories/{id}` - Kategori sil

## 🎨 Kullanıcı Arayüzü

### Login Sayfası
`http://localhost:8080/login`

### Kullanıcı Paneli
`http://localhost:8080/user/dashboard`
- Ay seçimi
- Gelir/gider ekleme
- Aylık özet görüntüleme

### Admin Paneli
`http://localhost:8080/admin/dashboard`
- Kullanıcı yönetimi
- Kategori yönetimi

## 🔒 Güvenlik

- JWT token süresi: 1 saat
- Token'lar Redis'te saklanır
- Logout işlemi token'ı Redis'ten siler
- Şifreler BCrypt ile hashlenir
- Rol tabanlı endpoint koruması

## 🗄️ Veritabanı Yapısı

- **users** - Kullanıcı bilgileri
- **roles** - Roller
- **user_roles** - Kullanıcı-rol ilişkisi
- **categories** - Gelir/gider kategorileri
- **income** - Gelir kayıtları
- **expense** - Gider kayıtları

## 📝 Varsayılan Kategoriler

### Gelir Kategorileri
- Maaş
- Freelance
- Yatırım
- Kira
- Diğer Gelir

### Gider Kategorileri
- Kira
- Market
- Faturalar
- Ulaşım
- Sağlık
- Eğlence
- Giyim
- Diğer Gider

## 🛠️ Teknolojiler

- **Backend:** Java 21, Spring Boot 3.2.2
- **Security:** Spring Security, JWT
- **Database:** PostgreSQL
- **Cache:** Redis
- **ORM:** JPA/Hibernate
- **UI:** Thymeleaf, HTML, CSS, JavaScript
- **Build Tool:** Maven

## 📄 Lisans

Bu proje MIT lisansı altında lisanslanmıştır.
