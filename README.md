# Ứng dụng tìm kiếm việc làm
### Công nghệ sử dụng
* Front-end : ReactJS
* Back-end : SpringBoot (JDK 11), MySQL

## Hướng dẫn chạy ứng dụng
### Backend
* Tạo một CSDL mới (đặt tên tùy ý)
* Tạo file application-local.properties trong đường dẫn src/main/resources với nội dung
````
spring.datasource.url=jdbc:mysql://localhost:3306/<database_name>
spring.datasource.username=<mysql_username>
spring.datasource.password=<mysql_password>
````
Ví dụ
````
spring.datasource.url=jdbc:mysql://localhost:3306/jobfinder
spring.datasource.username=root
spring.datasource.password=12345
````
* Chạy class App (src/main/java/com.uet.jobfinder/App)
* Server Backend sẽ chạy trên port 5000
* Một số mẫu về cách sử dụng api ở trong file src/test/java/api.http

### Front - end
* cd đến mục src/main/reactjs (Nếu dùng VsCode thì chỉ cần mở mỗi folder reactjs lên cho đỡ bị rối với back-end)
```bash
cd src/main/reactjs
```


# Giải thích một số thành phần mã nguồn
## Phần giải thích này chỉ dành cho reactjs thôi nhé
## src/main/reactjs

Đây chỉ là một ít gơi ý nhỏ và vẫn còn không rõ ràng, nếu 
có bất cứ thắc mắc nào cứ liên hệ t vì t lúc nào cũng online.
Có thể tham khảo repo ProductionMove
https://github.com/Hoangdao192/ProductionMove

### Một trang web sẽ được chia thành hai phần là phần layout và phần nội dung
* Phần layout được lưu trong src/components/layout
* Phần nội dung được lưu trong src/components/components

### Route
* Mọi url của phần front-end sẽ được lưu vào file routes/Routes.js
* Khi vẫn đang test thì nên để trong publicRoutes

### AuthenticatedRoute
* Route này đại diện cho các Route cần phải xác thực trước khi truy cập

### services/Authentication/Authentication.js
* Class này xử lý việc login và logout 
* Cách login: Authentication.login(username, password)
