# Đặc tả API

## Danh sách các tài khoản được tạo sẵn
### 1. Tài khoản công ty
```json
[
  {
    "email": "20020390@vnu.edu.vn",
    "password": "12345678",
    "role": "Company"
  },
  {
    "email": "20020391@vnu.edu.vn",
    "password": "12345678",
    "role": "Company"
  },
  {
    "email": "20020392@vnu.edu.vn",
    "password": "12345678",
    "role": "Company"
  },
  {
    "email": "20020393@vnu.edu.vn",
    "password": "12345678",
    "role": "Company"
  },
  {
    "email": "20020394@vnu.edu.vn",
    "password": "12345678",
    "role": "Company"
  }
]
```
### 2. Tài khoản ứng viên
```json
[
  {
    "email": "nguyendanghoangdao2002@gmail.com",
    "password": "12345678",
    "role": "Candidate"
  },
  {
    "email": "nguyenthithanhhuyen2002@gmail.com",
    "password": "12345678",
    "role" : "Candidate"
  },
  {
    "email": "tranthikimbac2002@gmail.com",
    "password": "12345678",
    "role" : "Candidate"
  },
  {
    "email": "trandinhcuong2002@gmail.com",
    "password": "12345678",
    "role" : "Candidate"
  },
  {
    "email": "phanvantiendung2002@gmail.com",
    "password": "12345678",
    "role" : "Candidate"
  }
]
```

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
1. [Đăng kí tài khoản](#signup_api)
2. [Đăng nhập](#signin_api)
3. [API Công ty](#company_api)
<br>a. [Cập nhập thông tin công ty](#company_update)
<br>b. [Lấy danh sách công ty](#company_list)
<br>c. [Lấy thông tin công ty](#company_get)
4. [API Ứng viên](#candidate_api)
<br>a. [Cập nhập thông tin ứng viên](#candidate_update)
<br>b. [Lấy thông tin ứng viên](#candidate_get)
<br>c. [Lấy danh sách ứng viên](#candidate_list)
5. [API Công việc](#job_api)
<br>a. [Tạo công việc](#job_create)
<br>b. [Cập nhập công việc](#job_update)
<br>c. [Lấy danh sách công việc](#job_list)
<br>d. [Lấy thông tin một công việc](#job_get)
<br>e. [Xóa công việc](#job_delete)
<br>f. [Ứng viên lưu công việc](#job_save)
<br>g. [Ứng viên lấy danh sách công việc đã lưu](#job_save_list)
<br>h. [Ứng viên bỏ lưu công việc](#job_save_delete)
6. [API Ứng tuyển](#application_api)
<br>a. [Ứng viên ứng tuyển](#application_create)
<br>b. [Công ty chấp nhận đơn ứng tuyển](#application_accept)
<br>c. [Công ty từ chối đơn ứng tuyển](#application_reject)
<br>d. [Ứng viên hủy ứng tuyển](#application_delete)
<br>e. [Lấy danh sách đơn ứng tuyển](#application_list)
<br>f. [Lấy danh sách đơn ứng tuyển theo công ty](#application_list_company)
7. [API Tiện ích](#until_api)
<br>a. [Lấy danh sách chuyên ngành](#major_api)

<a name="signup_api"></a>
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
<br>`Front-end` sẽ gửi `request` với các trường như sau (Các trường này đều
không bắt buộc)
```
(Text) companyName: Tên công ty 
(Text) companyDescription: Mô tả công ty
(Text) numberOfEmployee: Số lượng nhân viên
(Text) address.province: Tỉnh
(Text) address.district: Huyện
(Text) address.ward: Xã/Phường
(Text) address.detailAddress: Địa chỉ
(Float) address.longitude: Kinh độ
(Float) address.latitude: Vĩ độ
(ImageFile) companyLogoFile: Ảnh logo công ty
```
<i>Form</i>
```html
<form action="http://localhost:5000/api/company" method="put" enctype="multipart/form-data">
    <input type="text" name="companyName">
    <input type="text" name="companyDescription">
    <input type="text" name="numberOfEmployee">
    <input type="text" name="address.province">
    <input type="text" name="address.district">
    <input type="text" name="address.ward">
    <input type="text" name="address.longitude">
    <input type="text" name="address.latitude">
    <input type="file" name="companyLogoFile">
</form>
```
```json
{
  "userId": 4,
  "companyName": "Misa",
  "companyLogo": "http://localhost:5000/file/image/7",
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
<br>
<b>Candidate</b>
<br>`Front-end` sẽ gửi `request` với các trường như sau (Các trường này đều
không bắt buộc)

```
(Text) fullName: Họ và tên - Bắt buộc
(Text) sex: Giới tính
(Text) dateOfBirth: Ngày sinh
(Text) contactEmail: Email dùng để liên hệ
(Text) phoneNumber: Số điện thoại
(Text) selfDescription: Mô tả bản thân
(Text) experience: Kinh nghiệm
(Text) education: Học vấn
(ImageFile) candidateAvatarFile: Avatar
```
<i>Form</i>
```html
<form action="http://localhost:5000/api/candidate" method="put" enctype="multipart/form-data">
    <input type="text" name="fullName">
    <input type="text" name="sex">
    <input type="text" name="dateOfBirth">
    <input type="text" name="contactEmail">
    <input type="text" name="phoneNumber">
    <input type="text" name="selfDescription">
    <input type="text" name="experience">
    <input type="text" name="education">
    <input type="file" name="candidateAvatarFile">
</form>
```
```json
{
  "fullName": "Hello",
  "sex": "Nam",
  "dateOfBirth": null,
  "contactEmail": null,
  "phoneNumber": null,
  "selfDescription": null,
  "experience": null,
  "education": null,
  "address": null,
  "avatar": "http://localhost:5000/api/file/image/6",
  "candidateAvatarFile": null
}
```

<a name="signin_api"></a>
## 2. Đăng nhập
`Front-end` yêu cầu người dùng nhập email và mật khẩu rồi gửi `request` lên `Server`
```http request
POST http://localhost:5000/api/login
Content-Type: application/json

{
  "email" : "nguyendanghoangdao11a@gmail.com",
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
<a name="company_api"></a>
## 3. API Công ty
<a name="company_update"></a>
### a. Cập nhập thông tin công ty
`Front-end` sẽ gửi `request` với các trường như sau (Các trường này đều
không bắt buộc)
```
(Text) companyName: Tên công ty 
(Text) companyDescription: Mô tả công ty
(Text) numberOfEmployee: Số lượng nhân viên
(Text) address.province: Tỉnh
(Text) address.district: Huyện
(Text) address.ward: Xã/Phường
(Text) address.detailAddress: Địa chỉ
(Float) address.longitude: Kinh độ
(Float) address.latitude: Vĩ độ
(ImageFile) companyLogoFile: Ảnh logo công ty
```
<i>Form</i>
```html
<form action="http://localhost:5000/api/company" method="put" enctype="multipart/form-data">
    <input type="text" name="companyName">
    <input type="text" name="companyDescription">
    <input type="text" name="numberOfEmployee">
    <input type="text" name="address.province">
    <input type="text" name="address.district">
    <input type="text" name="address.ward">
    <input type="text" name="address.longitude">
    <input type="text" name="address.latitude">
    <input type="file" name="companyLogoFile">
</form>
```
```json
{
  "userId": 4,
  "companyName": "Misa",
  "companyLogo": "http://localhost:5000/file/image/7",
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
<a name="company_list"></a>
### b. Lấy danh sách công ty
Mặc định `page = 0` và `pageSize = 0`
```http request
GET http://localhost:5000/api/company?page=0&pageSize=10
```
```json
{
  "page": {
    "currentPage": 0,
    "pageSize": 10,
    "totalPage": 1
  },
  "elements": [
    {
      "userId": 7,
      "companyName": null,
      "companyLogo": null,
      "companyLogoFile": null,
      "companyDescription": null,
      "numberOfEmployee": null,
      "address": null
    },
    {
      "userId": 9,
      "companyName": "Dũng ngu loz",
      "companyLogo": "http://localhost:5000/api/file/image/11",
      "companyLogoFile": null,
      "companyDescription": null,
      "numberOfEmployee": null,
      "address": null
    }
  ]
}
```
<a name="company_get"></a>
### c. Lấy thông tin một công ty
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
<a name="candidate_api"></a>
## 4. API Ứng viên
<a name="candidate_update"></a>
### a. Cập nhập thông tin ứng viên
<b>Candidate</b>
<br>`Front-end` sẽ gửi `request` với các trường như sau (Các trường này đều
không bắt buộc)

```
(Text) fullName: Họ và tên - Bắt buộc
(Text) sex: Giới tính
(Text) dateOfBirth: Ngày sinh
(Text) contactEmail: Email dùng để liên hệ
(Text) phoneNumber: Số điện thoại
(Text) selfDescription: Mô tả bản thân
(Text) experience: Kinh nghiệm
(Text) education: Học vấn
(ImageFile) candidateAvatarFile: Avatar
```
<i>Form</i>
```html
<form action="http://localhost:5000/api/candidate" method="put" enctype="multipart/form-data">
    <input type="text" name="fullName">
    <input type="text" name="sex">
    <input type="text" name="dateOfBirth">
    <input type="text" name="contactEmail">
    <input type="text" name="phoneNumber">
    <input type="text" name="selfDescription">
    <input type="text" name="experience">
    <input type="text" name="education">
    <input type="file" name="candidateAvatarFile">
</form>
```
```json
{
  "fullName": "Hello",
  "sex": "Nam",
  "dateOfBirth": null,
  "contactEmail": null,
  "phoneNumber": null,
  "selfDescription": null,
  "experience": null,
  "education": null,
  "address": null,
  "avatar": "http://localhost:5000/api/file/image/6",
  "candidateAvatarFile": null
}
```
<a name="candidate_get"></a>
### b. Lấy thông tin một ứng viên
```http request
GET http://localhost:5000/api/candidate/4
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI5IiwiaWF0IjoxNjc5MzI0MDA4LCJleHAiOjE2Nzk5Mjg4MDh9.903xQJcw98ITqHGluPqSJuqbz0J3xRA1msgoTE6zHLPgw91Nl7EF1mlhZJiDXoZSKV12H3lz8WZpNgUN9PbMhw
```
```json
{
  "userId": 4,
  "fullName": "Hello",
  "sex": "Nam",
  "dateOfBirth": null,
  "contactEmail": null,
  "phoneNumber": null,
  "selfDescription": null,
  "experience": null,
  "education": null,
  "address": null,
  "avatar": "http://localhost:5000/api/file/image/6",
  "candidateAvatarFile": null
}
```
<a name="candidate_list"></a>
### c. Lấy danh sách ứng viên
`page` và `pageSize` có thể có hoặc không.
<br>Mặc định `page = 0` và `pageSize = 10`
```http request
GET http://localhost:5000/api/candidate?page=0&pageSize=10
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0IiwiaWF0IjoxNjc5MzYxMjcyLCJleHAiOjE2Nzk5NjYwNzJ9.EE_Y6H1fYqZ1t9nkI-wpFY-eiE25JmdzjW6hpoKTpQXew8uodJ4o3B2n81oMPYMpmCvBkWHKb6nQD5eRSNsx0w
```
```json
{
  "page": {
    "currentPage": 0,
    "pageSize": 10,
    "totalPage": 1
  },
  "elements": [
    {
      "userId": 4,
      "fullName": "Dao",
      "sex": null,
      "dateOfBirth": null,
      "contactEmail": null,
      "phoneNumber": null,
      "selfDescription": null,
      "experience": null,
      "education": null,
      "address": {
        "id": 14,
        "province": "HaNoi",
        "district": null,
        "ward": null,
        "detailAddress": null,
        "longitude": null,
        "latitude": null
      },
      "avatar": "http://localhost:5000/api/file/image/6",
      "candidateAvatarFile": null
    }
  ]
}
```
<a name="job_api"></a>
## 5. API Công việc
<a name="job_create"></a>
### a. Tạo công việc
`requireExperience` nhận giá trị với các format sau
<br>
`1 year` : 1 năm <br>
`6 month` : 6 tháng <br>
`< 1 year` : ít hơn 1 năm <br>
`> 1 year` : trên 1 năm <br>
`1 - 2 year` : 1 đến 2 năm <br>
<br>
`salary` nhận giá trị với định dạng sau <br>
`5000000` : cố định 5 triệu đồng <br>
`> 5000000` : trên 5 triệu <br>
`1000000 - 5000000`: Từ 1 triệu đến 5 triệu <br>
`< 5000000` : cao nhất 5 triệu


```http request
POST http://localhost:5000/api/job
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI5IiwiaWF0IjoxNjc5MzI0MDA4LCJleHAiOjE2Nzk5Mjg4MDh9.903xQJcw98ITqHGluPqSJuqbz0J3xRA1msgoTE6zHLPgw91Nl7EF1mlhZJiDXoZSKV12H3lz8WZpNgUN9PbMhw

{
  "jobTitle" : "Lập trình viên Android",
  "jobDescription" : "Lập trình ứng dụng cho Android",
  "jobAddress" : {
    "province" : "Hà Nội",
    "district" : "Nam Từ Liêm",
    "ward" : "Xuân Thủy",
    "detailAddress" : "144"
  },
  "major" : "Information Technology",
  "salary" : "5 triệu",
  "numberOfHiring" : 10,
  "requireExperience" : "6 tháng",
  "sex" : "Male",
  "workingForm" : "Full-time",
  "closeDate" : "2023-04-28"
}
```
```json
{
  "id": 13,
  "userId": 9,
  "jobTitle": "Lập trình viên Android",
  "jobDescription": "Lập trình ứng dụng cho Android",
  "jobAddress" : {
    "id" : 1,
    "province" : "Hà Nội",
    "district" : "Nam Từ Liêm",
    "ward" : "Xuân Thủy",
    "detailAddress" : "144"
  },
  "major": "Information Technology",
  "salary": "5 triệu",
  "numberOfHiring": 10,
  "requireExperience": "6 tháng",
  "sex": "Nam",
  "workingForm": "Full-time"
}
```
<a name="job_update"></a>
### b. Cập nhập công việc
```http request
PUT http://localhost:5000/api/job
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI5IiwiaWF0IjoxNjc5MzI0MDA4LCJleHAiOjE2Nzk5Mjg4MDh9.903xQJcw98ITqHGluPqSJuqbz0J3xRA1msgoTE6zHLPgw91Nl7EF1mlhZJiDXoZSKV12H3lz8WZpNgUN9PbMhw

{
  "id" : 13,
  "jobTitle" : "Lập trình viên Android Fake",
  "jobDescription" : "Lập trình ứng dụng cho Android",
  "jobAddress" : {
    "province" : "Hà Nội",
    "district" : "Nam Từ Liêm",
    "ward" : "Xuân Thủy",
    "detailAddress" : "144"
  },
  "major" : "Information Technology",
  "salary" : "5 triệu",
  "numberOfHiring" : 10,
  "requireExperience" : "6 tháng",
  "sex" : "Nam",
  "workingForm" : "Full-time"
}
```
```json
{
  "id": 13,
  "userId": 9,
  "jobTitle": "Lập trình viên Android Fake",
  "jobDescription": "Lập trình ứng dụng cho Android",
  "jobAddress" : {
    "id" : 1,
    "province" : "Hà Nội",
    "district" : "Nam Từ Liêm",
    "ward" : "Xuân Thủy",
    "detailAddress" : "144"
  },
  "major": "Information Technology",
  "salary": "5 triệu",
  "numberOfHiring": 10,
  "requireExperience": "6 tháng",
  "sex": "Nam",
  "workingForm": "Full-time"
}
```
<a name="job_list"></a>
### c. Lấy danh sách công việc
API này cung cấp các param sau (đều không bắt buộc)<br>
`page` : Số thứ tự của trang<br>
`pageSize` : Số kết quả trả về<br>
`companyId` : Lọc công việc theo Company<br>
`jobTitle` : Lọc công việc theo tiêu đề<br>
`major` : Lọc theo chuyên ngành<br>
`workingForm` : Lọc theo hình thức làm việc<br>
`workingForm` chỉ trong 3 giá trị `Full-time` `Part-time` và `Remote`
```http request
GET http://localhost:5000/api/job?page=0&pageSize=10
### Default page = 0, pageSize = 10
GET http://localhost:5000/api/job
### Filter by company
GET http://localhost:5000/api/job?companyId=1
```
```json
{
  "page": {
    "currentPage": 0,
    "pageSize": 10,
    "totalPage": 1
  },
  "elements": [
    {
      "id": 13,
      "userId": 9,
      "jobTitle": "Lập trình viên Android Fake",
      "jobDescription": "Lập trình ứng dụng cho Android",
      "jobAddress" : {
        "id" : 1,
        "province" : "Hà Nội",
        "district" : "Nam Từ Liêm",
        "ward" : "Xuân Thủy",
        "detailAddress" : "144"
      },
      "major": "Information Technology",
      "salary": "5 triệu",
      "numberOfHiring": 10,
      "requireExperience": "6 tháng",
      "sex": "Nam",
      "workingForm": "Full-time"
    }
  ]
}
```
<a name="job_get"></a>
### d. Lấy thông tin một công việc
```http request
GET http://localhost:5000/api/job/13
```
```json
{
  "id": 13,
  "userId": 9,
  "jobTitle": "Lập trình viên Android Fake",
  "jobDescription": "Lập trình ứng dụng cho Android",
  "jobAddress" : {
    "id" : 1,
    "province" : "Hà Nội",
    "district" : "Nam Từ Liêm",
    "ward" : "Xuân Thủy",
    "detailAddress" : "144"
  },
  "major": "Information Technology",
  "salary": "5 triệu",
  "numberOfHiring": 10,
  "requireExperience": "6 tháng",
  "sex": "Nam",
  "workingForm": "Full-time"
}
```
<a name="job_delete"></a>
### e. Xóa công việc
```http request
DELETE http://localhost:5000/api/job/13
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI5IiwiaWF0IjoxNjc5MzI0MDA4LCJleHAiOjE2Nzk5Mjg4MDh9.903xQJcw98ITqHGluPqSJuqbz0J3xRA1msgoTE6zHLPgw91Nl7EF1mlhZJiDXoZSKV12H3lz8WZpNgUN9PbMhw
```
```json
{
  "success" : true
}
```
<a name="job_save"></a>
### f. Ứng viên lưu công việc
```http request
POST http://localhost:5000/api/job/save
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI2IiwiaWF0IjoxNjgwMDczODg1LCJleHAiOjE2ODA2Nzg2ODV9.sebTC9IVnDUg9H8MNk1EaPn5B7B79GU6usL9wNr5gvh3frF2zWocLXb_4Fug3fJl_glHE0GIp2CaT0OGEjQUhA
Content-Type: application/json

{
  "jobId" : 8,
  "candidateId" : 6
}
```
```json
{
  "id": 1,
  "jobId": null,
  "job": {
    "id": 1,
    "userId": 1,
    "jobTitle": "Lập trình viên Java",
    "jobDescription": "Tuyển lập trình viên Java",
    "jobAddress": {
      "id": 1,
      "province": "Hà Nội",
      "district": "Cầu Giấy",
      "ward": "Phố Duy Tân",
      "detailAddress": "Tầng 2, tòa nhà FPT",
      "longitude": null,
      "latitude": null
    },
    "major": "Công nghệ thông tin",
    "salary": "Thỏa thuận",
    "numberOfHiring": 20,
    "requireExperience": "> 1 year",
    "sex": "Male",
    "workingForm": "Fulltime",
    "status": "OPEN",
    "openDate": "2023-02-01",
    "closeDate": "2023-05-01"
  },
  "candidateId": 6
}
```
<a name="job_save_list"></a>
### g. Ứng viên lấy danh sách công việc đã lưu
```http request
GET http://localhost:5000/api/job/save?candidateId=6
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI2IiwiaWF0IjoxNjgwNjI2OTM4LCJleHAiOjE2ODEyMzE3Mzh9.lhraggy0HTXCqbDS4ZZ8vpmwyD5h6kWGMLE-_Nnyg3SqetEpRAAhxGiJpzU1MIUF2HdKH2xEWX9Au7LKmVzaYw
```
```json
{
  "page": {
    "currentPage": 0,
    "pageSize": 10,
    "totalPage": 1,
    "totalElement": 0
  },
  "elements": [
    {
      "id": 1,
      "jobId": null,
      "job": {
        "id": 1,
        "userId": 1,
        "jobTitle": "Lập trình viên Java",
        "jobDescription": "Tuyển lập trình viên Java",
        "jobAddress": {
          "id": 1,
          "province": "Hà Nội",
          "district": "Cầu Giấy",
          "ward": "Phố Duy Tân",
          "detailAddress": "Tầng 2, tòa nhà FPT",
          "longitude": null,
          "latitude": null
        },
        "major": "Công nghệ thông tin",
        "salary": "Thỏa thuận",
        "numberOfHiring": 20,
        "requireExperience": "> 1 year",
        "sex": "Male",
        "workingForm": "Fulltime",
        "status": "OPEN",
        "openDate": "2023-02-01",
        "closeDate": "2023-05-01"
      },
      "candidateId": 6
    }
  ]
}
```
<a name="job_save_delete"></a>
### h. Ứng viên bỏ lưu công việc
```http request
DELETE http://localhost:5000/api/job/save/1
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI2IiwiaWF0IjoxNjgwNjI2OTM4LCJleHAiOjE2ODEyMzE3Mzh9.lhraggy0HTXCqbDS4ZZ8vpmwyD5h6kWGMLE-_Nnyg3SqetEpRAAhxGiJpzU1MIUF2HdKH2xEWX9Au7LKmVzaYw
```
```json
{
  "success": true
}
```

<a name="application_api"></a>
## 5. API Ứng tuyển
<a name="application_create"></a>
### a. Ứng viên ứng tuyển
Các trường thông tin
```
(Long) candidateId: id ứng viên (Bắt buộc)
(Long) jobId: id công việc (Bắt buộc)
(Text) description: ghi chú, có thể là mô tả ngắn của ứng viên
(File pdf) cvFile: file cv
```
<i>Form</i>
```html
<form action="http://localhost:5000/api/job-application" method="put" enctype="multipart/form-data">
    <input type="text" name="candidateId">
    <input type="text" name="jobId">
    <input type="text" name="description">
    <input type="file" name="cvFile">
</form>
```
```json
{
  "id": 17,
  "candidateId": 4,
  "jobId": 15,
  "status": "Waiting",
  "description": null,
  "cvFile": null,
  "cv": null,
  "appliedDate": "2023-03-22T17:18:00.5217132",
  "updatedDate": null
}
```
<a name="application_accept"></a>
### b. Chấp nhận đơn ứng tuyển
```http request
GET http://localhost:5000/api/job-application/accept/17
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI5IiwiaWF0IjoxNjc5MzI0MDA4LCJleHAiOjE2Nzk5Mjg4MDh9.903xQJcw98ITqHGluPqSJuqbz0J3xRA1msgoTE6zHLPgw91Nl7EF1mlhZJiDXoZSKV12H3lz8WZpNgUN9PbMhw
```
```json
{
  "success" : true
}
```
<a name="application_reject"></a>
### c. Từ chối đơn ứng tuyển
```http request
GET http://localhost:5000/api/job-application/reject/17
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI5IiwiaWF0IjoxNjc5MzI0MDA4LCJleHAiOjE2Nzk5Mjg4MDh9.903xQJcw98ITqHGluPqSJuqbz0J3xRA1msgoTE6zHLPgw91Nl7EF1mlhZJiDXoZSKV12H3lz8WZpNgUN9PbMhw
```
```json
{
  "success" : true
}
```
<a name="application_delete"></a>
### d. Ứng viên hủy ứng tuyển
```http request
DELETE http://localhost:5000/api/job-application/17
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0IiwiaWF0IjoxNjc5MzYxMjcyLCJleHAiOjE2Nzk5NjYwNzJ9.EE_Y6H1fYqZ1t9nkI-wpFY-eiE25JmdzjW6hpoKTpQXew8uodJ4o3B2n81oMPYMpmCvBkWHKb6nQD5eRSNsx0w
```
```json
{
  "success" : true
}
```
<a name="application_list"></a>
### e. Lấy danh sách đơn ứng tuyển
#### Ứng viên lấy danh sách đơn ứng tuyển đã gửi
```http request
GET http://localhost:5000/api/job-application?candidateId=4
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0IiwiaWF0IjoxNjc5MzYxMjcyLCJleHAiOjE2Nzk5NjYwNzJ9.EE_Y6H1fYqZ1t9nkI-wpFY-eiE25JmdzjW6hpoKTpQXew8uodJ4o3B2n81oMPYMpmCvBkWHKb6nQD5eRSNsx0w
```
```json
{
  "page": {
    "currentPage": 0,
    "pageSize": 10,
    "totalPage": 1
  },
  "elements": [
    {
      "id": 18,
      "candidateId": 4,
      "jobId": 15,
      "status": "Waiting",
      "description": null,
      "cvFile": null,
      "cv": null,
      "appliedDate": "2023-03-22T17:25:17.015413",
      "updatedDate": null
    }
  ]
}
```
#### Công ty lấy danh sách đơn ứng tuyển cho một công việc nhất định
```http request
GET http://localhost:5000/api/job-application?jobId=15
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI5IiwiaWF0IjoxNjc5MzI0MDA4LCJleHAiOjE2Nzk5Mjg4MDh9.903xQJcw98ITqHGluPqSJuqbz0J3xRA1msgoTE6zHLPgw91Nl7EF1mlhZJiDXoZSKV12H3lz8WZpNgUN9PbMhw
```
```json
{
  "page": {
    "currentPage": 0,
    "pageSize": 10,
    "totalPage": 1
  },
  "elements": [
    {
      "id": 9,
      "candidateId": null,
      "candidate": {
        "userId": 6,
        "fullName": "Hoàng Đạo",
        "sex": "Male",
        "dateOfBirth": "2002-02-19",
        "contactEmail": "20020376@vnu.edu.vn",
        "phoneNumber": "0325135251",
        "selfDescription": "Sinh viên CNTT",
        "avatar": null,
        "candidateAvatarFile": null
      },
      "jobId": 8,
      "status": "Waiting",
      "description": null,
      "cvFile": null,
      "cv": null,
      "appliedDate": "2023-03-29T16:05:46.076455",
      "updatedDate": null
    }
  ]
}
```
<a name="application_list_company"></a>
### f. Lấy danh sách đơn ứng tuyển theo công ty
```http request
GET http://localhost:5000/api/job-application/listByCompany?companyId=1
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjgwMzY2OTE0LCJleHAiOjE2ODA5NzE3MTR9.ivU7xZb-Q3u3lnh5HaybOKRpdRI0ME3avU4lRqs1vqxFdQqG3ljL19gO8vSi1Ej5WyIU3IoecSq_1QUxDPmy_w
```
```json
{
  "page": {
    "currentPage": 0,
    "pageSize": 10,
    "totalPage": 1
  },
  "elements": [
    {
      "id": 1,
      "candidateId": null,
      "candidate": {
        "userId": 6,
        "fullName": "Nguyễn Đăng Hoàng Đạo",
        "sex": "Male",
        "dateOfBirth": "2002-02-19",
        "contactEmail": "nguyendanghoangdao2002@gmail.com",
        "phoneNumber": "0325135251",
        "selfDescription": "Sinh viên Trường đại học Công nghệ - Đại học Quốc Gia Hà Nội",
        "avatar": null,
        "candidateAvatarFile": null
      }
    }
  ]
}
```

<a name="until_api"></a>
## 7. API Tiện ích
<a name="major_api"></a>
### a. Lấy danh sách chuyên ngành
```http request
GET http://localhost:5000/api/major
```
```json
[
  {
    "id": 7,
    "name": "Công nghệ thông tin"
  },
  {
    "id": 8,
    "name": "Hệ thống thông tin"
  },
  {
    "id": 9,
    "name": "An toàn thông tin"
  },
  {
    "id": 10,
    "name": "Quản trị kinh doanh"
  },
  {
    "id": 11,
    "name": "Mạng máy tính và truyền thông dữ liệu"
  },
  {
    "id": 12,
    "name": "Logistics và Quản lý chuỗi cung ứng"
  },
  {
    "id": 13,
    "name": "Kỹ thuật máy tính"
  },
  {
    "id": 14,
    "name": "Công nghệ truyền thông"
  }
]
```

