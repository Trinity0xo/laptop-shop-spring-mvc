# Laptop Shop Spring MVC

Laptop Shop Spring MVC là một dự án web bán laptop (shop online) đơn giản được xây dựng theo mô hình Spring MVC

## Cấu hình dự án

Để chạy được dự án thì cần phải cấu hình một số thành phần ở file `application.properties` trong `src/main/resources`

### 1. Cấu hình cơ sở dữ liệu (MySQL)

Tạo một database với tên bất kỳ và chỉnh sửa những dòng bên dưới:

```
spring.datasource.username=USERNAME
spring.datasource.password=MAT_KHAU
spring.datasource.url=jdbc:mysql://localhost:3306/TEN_DATABASE
```

Ví dụ: nếu tên database là `shoplap`, username là `root` và mật khẩu là `123456` thì sẽ như sau:

```
spring.datasource.username=root
spring.datasource.password=123456
spring.datasource.url=jdbc:mysql://localhost:3306/shoplap
```

### 2. Cấu hình lưu trữ hình ảnh

#### 2.1. Cấu hình đường dẫn gốc

Thư mục gốc lưu trữ file trên hệ thống:

```
files.path=C:/Users/Trinity/Documents/shoplap
```

Đường dẫn mapping tài nguyên tĩnh (sẽ dùng khi truy cập qua URL):

```
static.resources.mapping.folder=uploads
```

- `files.path`: đường dẫn vật lý trên máy tính, nơi lưu các file upload
- `static.resources.mapping.folder`: tên thư mục ảo (virtual path) để map tài nguyên ra ngoài khi người dùng truy cập

#### 2.2. Cấu hình các thư mục con

```
upload.product-images.folder=product_images
upload.category-images.folder=category_images
upload.avatars.folder=avatars
```

- `upload.product-images.folder`: thư mục con để lưu ảnh sản phẩm
- `upload.category-images.folder`: thư mục con để lưu ảnh danh mục
- `upload.avatars.folder`: thư mục con để lưu ảnh đại diện người dùng (avatar)

### 3. Cấu hình gửi email

```
spring.mail.username=EMAIL@GMAIL.COM
spring.mail.password=APP_PASSWORD
```

- `spring.mail.username`: địa chỉ Gmail dùng để gửi mail
- `spring.mail.password`: mật khẩu ứng dụng (App Password), không phải mật khẩu Gmail thông thường

### 4. Cấu hình đăng nhập với google Oauth02 (tùy chọn)

Để tích hợp đăng nhập bằng google thì cần phải khai báo `Client ID` và `Client Secret` do Google cung cấp vào bên dưới:

```
spring.security.oauth2.client.registration.google.client-id=CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=CLIENT_SECRET
```

- `Client ID`: ID của ứng dụng, lấy từ Google Cloud Console
- `client Secret`: Secret key dùng để xác thực ứng dụng

## Chạy dự án

Chạy dự án thông qua IDE và truy cập vào địa chỉ: http://localhost:8080
