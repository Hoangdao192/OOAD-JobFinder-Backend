ý nghĩa của hệ thống này
phân tích usecase, đặc tả từng actor
Mô hình phân tích thiết kế, hệ thống (CHọn class diagram hoặc sequence diagram)
Tuân thủ quy tắc IBM
security, persistance như thế nào, mô hình
demo, kiểm thử

# Ứng dụng tìm kiếm việc làm

## Nội dung
1. [Công nghệ sử dụng](#tech)
2. [Yêu cầu môi trường](#environment)
3. [Hướng dẫn chạy](#run)
4. [Đặc tả API](#api)

<a name="tech"></a>
## Công nghệ sử dụng
* Front - end : [ReactJS](https://reactjs.org/)
* Back - end : [SpringBoot](https://spring.io/), [JDK11]()
* Database : [MySQL8](https://dev.mysql.com/downloads/installer/)

<a name="environment"></a>
## Yêu cầu môi trường
* [JDK11](https://www.oracle.com/vn/java/technologies/javase/jdk11-archive-downloads.html)
* [MySQL 8+](https://dev.mysql.com/downloads/installer/)
* [NodeJS](https://nodejs.org/en/)

<a name="run"></a>
## Hướng dẫn chạy ứng dụng
### Backend
1. Tạo một cơ sở dữ liệu mới (đặt tên tùy ý)
2. Tạo file `application-local.properties` trong đường dẫn `src/main/resources` với nội dung. Trong đó `server_upload_path` là đường dẫn đến thư mục để lưu file của server (Tạo một thư mục trống ở đâu đó và ghi đường dẫn vào)
```
spring.datasource.url=jdbc:mysql://localhost:3306/<database_name>
spring.datasource.username=<mysql_username>
spring.datasource.password=<mysql_password>
upload.path=<server_upload_path>
```
Ví dụ
````
spring.datasource.url=jdbc:mysql://localhost:3306/jobfinder
spring.datasource.username=root
spring.datasource.password=12345
````
3. Chạy class `App` `src/main/java/com.uet.jobfinder/App`

Server Backend sẽ chạy trên port `5000`

Một số mẫu về cách sử dụng api ở trong file `src/test/java/api.http`

### Front - end
* Từ thư mục gốc của project, `cd` đến mục `src/main/reactjs` 
(Nếu dùng vscode thì chỉ cần mở mỗi folder reactjs lên cho đỡ bị rối với back-end)
```bash
cd src/main/reactjs
```
* Chạy `npm i` để cài thư viện
* Chạy `npm start` để chạy server

Server front-end sẽ chạy trên port `3000`

<a name="api"></a>
## [Đặc tả API](API.md)

# Giải thích một số thành phần mã nguồn
## Phần giải thích này chỉ dành cho reactjs thôi.
## `src/main/reactjs`

Đây chỉ là một ít gơi ý nhỏ và vẫn còn không rõ ràng, nếu 
có bất cứ thắc mắc nào cứ liên hệ t vì t lúc nào cũng online.
Có thể tham khảo repository [ProductionMove](https://github.com/Hoangdao192/ProductionMove)

### Một trang web sẽ được chia thành hai phần là phần layout và phần nội dung
* Phần layout được lưu trong `src/components/layout`
* Phần nội dung được lưu trong `src/components/components`

### Route
* Mọi `url` của phần front-end sẽ được lưu vào file `routes/Routes.js`
* Khi vẫn đang test thì nên để trong `publicRoutes`

### AuthenticatedRoute
* Route này đại diện cho các Route cần phải xác thực trước khi truy cập

### `services/Authentication/Authentication.js`
* Class này xử lý việc login và logout 
* Cách login: `Authentication.login(username, password)`
