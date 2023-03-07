# Giải thích một số thành phần mã nguồn

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
