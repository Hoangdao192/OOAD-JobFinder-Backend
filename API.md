# Đặc tả API

## Lỗi Server
Bất kì lỗi nào server trả về cho front-end đều có dạng `JSON` như sau

Ví dụ đây là một lỗi được trả về khi người dùng gửi request đăng nhập
với một email không tồn tại trong hệ thống
```json
{
  "errors": [
    {
      "code": "AUERR1",
      "message": "Email is not exists."
    }
  ]
}
```

## Xác thực
`Server` xác thực người dùng thông qua `JsonWebToken` nên đối với
các request yêu cầu xác thực thì cần kèm thêm trường `Authorization` vào
`Header`
```http request
Authorization: <token_type> <token>
# Example
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0IiwiaWF0IjoxNjc4Nzc5Njg0LCJleHAiOjE2NzkzODQ0ODR9.x7sXeuitQCsygr4LIdqZ8NCU4RUTzoIq7ZJ0mgKwtRAf3ONQ5EOcM3u9mKWJBgvrOpR-Yb_hdBdU9t9TsY3mow
```
<br> Ví dụ đây là một request yêu cầu update thông tin công ty

```http request
PUT http://localhost:5000/api/company
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0IiwiaWF0IjoxNjc4Nzc5Njg0LCJleHAiOjE2NzkzODQ0ODR9.x7sXeuitQCsygr4LIdqZ8NCU4RUTzoIq7ZJ0mgKwtRAf3ONQ5EOcM3u9mKWJBgvrOpR-Yb_hdBdU9t9TsY3mow
Content-Type: application/json

{
  "companyName" : "Misa",
  "companyLogo" : "...",
  "companyDescription" : "A good company",
  "numberOfEmployee" : "200+",
  "address" : {
    "province" : "...",
    "district" : "...",
    "ward" : "...",
    "detailAddress" : "...",
    "longitude" : 1.1223,
    "latitude" : 1.2334   
  }
}
```

## Các API
### 1. Đăng kí tài khoản
`Front-end` sẽ có 3 trang 
1. Trang nhập email, mật khẩu và chọn loại tài khoản
2. Trang nhập mã xác thực
3. Trang nhập thông tin chi tiết của người dùng

![img.png](readme/img.png)

<b>LƯU Ý: Chỉ có 2 loại tài khoản người dùng là Company và Employee</b>

<b>Bước 1. Nhập email, mật khẩu và chọn loại tài khoản</b>
```http request
POST http://localhost:5000/api/register
Content-Type: application/json

{
 "email" : "nguyendanghoangdao11a@gmail.com",
 "password" : "12345678",
 "role" : "Company"
}
```
```json
{
  "id": 4,
  "email": "20020390@vnu.edu.vn",
  "enabled": false,
  "locked": false,
  "roles": [
    "Company"
  ]
}
```

`Server` sẽ tạo tài khoản người dùng và gửi mã xác thực vào email của người dùng.

<b>Bước 2. Nhập mã xác thực được gửi về email</b>
<br>`Front-end` yêu cầu người dùng nhập mã xác thực và gửi request lên `Server`
```http request
POST http://localhost:5000/api/register/confirm
Content-Type: application/json

{
  "email" : "nguyendanghoangdao11a@gmail.com",
  "confirmationKey" : "626081"
}
```
<b>Lưu ý: `Front-end` phải lưu lại `token(tokenType, accessToken)` được trả về
để thực hiện các `request` tiếp theo</b>
```json
{
  "user": {
    "id": 4,
    "email": "20020390@vnu.edu.vn",
    "enabled": true,
    "locked": false,
    "roles": [
      "Company"
    ]
  },
  "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0IiwiaWF0IjoxNjc4ODA3NTc1LCJleHAiOjE2Nzk0MTIzNzV9.jx2fqHszwsgbDIqqnEgXwEw6gyamnLBGCq_6C7hAcSOO3HzAFGKZpT0Zsqb0mmwE2aU8p78ltvVpeSrtbEFV1g",
  "tokenType": "Bearer"
}
```
Mã xác thực sẽ hết hiệu lực trong vòng 2 phút nên để gửi lại mã
xác thực thì gửi request như sau
```http request
GET http://localhost:5000/api/register/confirm/resend?email=20020390@vnu.edu.vn
```
```json
{
  "success": true
}
```
<b>Bước 3. Nhập thông tin chi tiết của người dùng</b><br>
`Front-end` yêu cầu người dùng nhập thông tin chi tiết và
gửi lên `Server`.
Tùy vào loại tài khoản của người dùng là `Company` hay `Employee`
thì `request` sẽ khác nhau.
<br>
<b>Company</b>
```http request
PUT http://localhost:5000/api/company
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0IiwiaWF0IjoxNjc4Nzc5Njg0LCJleHAiOjE2NzkzODQ0ODR9.x7sXeuitQCsygr4LIdqZ8NCU4RUTzoIq7ZJ0mgKwtRAf3ONQ5EOcM3u9mKWJBgvrOpR-Yb_hdBdU9t9TsY3mow
Content-Type: application/json

{
  "companyName" : "Misa",
  "companyLogo" : "...",
  "companyDescription" : "A good company",
  "numberOfEmployee" : "200+",
  "address" : {
    "province" : "...",
    "district" : "...",
    "ward" : "...",
    "detailAddress" : "...",
    "longitude" : 1.1223,
    "latitude" : 1.2334   
  }
}
```
```json
{
  "userId": 4,
  "companyName": "Misa",
  "companyLogo": "tempLogo",
  "companyDescription": "A good company",
  "numberOfEmployee": "200+",
  "address": {
    "province": "Hà Nội",
    "district": "Nam Từ Liêm",
    "ward": "Phương Canh",
    "detailAddress": "Số 48 ngõ 80",
    "longitude": null,
    "latitude": null
  }
}
```

## 2. Đăng nhập
`Front-end` yêu cầu người dùng nhập email và mật khẩu rồi gửi `request` lên `Server`
```http request
POST http://localhost:5000/api/login
Content-Type: application/json

{
  "email" : "nguyendanghoangao11a@gmail.com",
  "password" : "12345678"
}
```
`Server` gửi lại token(lưu lại token cho các request sau này) và thông tin cơ bản của tài khoản
```json
{
  "tokenType": "Bearer",
  "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0IiwiaWF0IjoxNjc4ODA4NTc5LCJleHAiOjE2Nzk0MTMzNzl9.TsZMV2iZnA3Js1sGcirZYGBKy4Vq5s_t-EY3zQobULVS08FNNAcsZtqTZDIWRkzhnba4ILk_TdYivOeOA48K_w",
  "user": {
    "id": 4,
    "email": "20020390@vnu.edu.vn",
    "enabled": true,
    "locked": false,
    "roles": [
      "Company"
    ]
  }
}
```

## 3. Lấy thông tin một công ty
```http request
GET http://localhost:5000/api/company/{id}
# Example
GET http://localhost:5000/api/company/4
```
```json
{
  "userId": 4,
  "companyName": "Misa",
  "companyLogo": "tempLogo",
  "companyDescription": "A good company",
  "numberOfEmployee": "200+",
  "address": {
    "province": "Hà Nội",
    "district": "Nam Từ Liêm",
    "ward": "Phương Canh",
    "detailAddress": "Số 48 ngõ 80",
    "longitude": null,
    "latitude": null
  }
}
```

