USE jobfinder;
Set names utf8mb4;

ALTER TABLE job ADD FULLTEXT (job_title);
ALTER TABLE company ADD FULLTEXT (company_name);

/*Roles*/
INSERT INTO role (id, name) values
    (1, 'Admin'), (2, 'Company'), (3, 'Candidate');

/*Admin*/
INSERT INTO user (id, email, enabled, locked, password) VALUES
    (99, 'admin@jobfinder.com', true, false, '$2a$10$sW0xlxPWnY6m993ofp.S8eu3KnNx6QoDw.sq3vItwIbbeYFemYB7y');
INSERT INTO user_role (user_id, role_id) VALUES (99, 1);

/*App file*/
INSERT INTO file (id, file_name, file_path, file_type) VALUES
    (1, 'fpt_logo.png', 'C:\\Users\\Administrator\\.jobfinder-server\\fpt_logo', 'application/jpeg'),
    (2, 'amela_logo.png', 'C:\\Users\\Administrator\\.jobfinder-server\\amela_logo', 'application/jpeg'),
    (3, 'cmc_logo.png', 'C:\\Users\\Administrator\\.jobfinder-server\\cmc_logo', 'application/jpeg'),
    (4, 'vng_logo.png', 'C:\\Users\\Administrator\\.jobfinder-server\\vng_logo', 'application/jpeg'),
    (5, 'vccorp_logo.png', 'C:\\Users\\Administrator\\.jobfinder-server\\vccorp_logo', 'application/jpeg'),
    (6, 'cv_1.pdf', 'C:\\Users\\Administrator\\.jobfinder-server\\cv_1', 'application/pdf'),
    (7, 'cv_2.pdf', 'C:\\Users\\Administrator\\.jobfinder-server\\cv_2', 'application/pdf'),
    (8, 'cv_3.pdf', 'C:\\Users\\Administrator\\.jobfinder-server\\cv_3', 'application/pdf'),
    (9, 'cv_4.pdf', 'C:\\Users\\Administrator\\.jobfinder-server\\cv_4', 'application/pdf'),
    (10, 'cv_5.pdf', 'C:\\Users\\Administrator\\.jobfinder-server\\cv_5', 'application/pdf'),
    (11, 'av_1.png', 'C:\\Users\\Administrator\\.jobfinder-server\\av_1', 'application/jpeg'),
    (12, 'av_2.png', 'C:\\Users\\Administrator\\.jobfinder-server\\av_2', 'application/jpeg'),
    (13, 'av_3.png', 'C:\\Users\\Administrator\\.jobfinder-server\\av_3', 'application/jpeg'),
    (14, 'av_4.png', 'C:\\Users\\Administrator\\.jobfinder-server\\av_4', 'application/jpeg'),
    (15, 'av_5.png', 'C:\\Users\\Administrator\\.jobfinder-server\\av_5', 'application/jpeg');

/*Company*/
INSERT INTO user (id, email, enabled, locked, password, create_date) VALUES
    (1, '20020390@vnu.edu.vn', true, false, '$2a$10$sW0xlxPWnY6m993ofp.S8eu3KnNx6QoDw.sq3vItwIbbeYFemYB7y', '2023-02-01'),
    (2, '20020391@vnu.edu.vn', true, false, '$2a$10$sW0xlxPWnY6m993ofp.S8eu3KnNx6QoDw.sq3vItwIbbeYFemYB7y', '2023-02-10'),
    (3, '20020392@vnu.edu.vn', true, false, '$2a$10$sW0xlxPWnY6m993ofp.S8eu3KnNx6QoDw.sq3vItwIbbeYFemYB7y', '2023-02-15'),
    (4, '20020393@vnu.edu.vn', true, false, '$2a$10$sW0xlxPWnY6m993ofp.S8eu3KnNx6QoDw.sq3vItwIbbeYFemYB7y', '2023-02-20'),
    (5, '20020394@vnu.edu.vn', true, false, '$2a$10$sW0xlxPWnY6m993ofp.S8eu3KnNx6QoDw.sq3vItwIbbeYFemYB7y', '2023-02-28');
INSERT INTO user_role (user_id, role_id) VALUES
    (1, 2), (2, 2), (3, 2), (4, 2), (5, 2);

INSERT INTO address (id, detail_address, district, province, ward) VALUES
    (1, 'Tầng 2, tòa nhà FPT', 'Cầu Giấy', 'Hà Nội', 'Phố Duy Tân');
INSERT INTO company (user_id, company_description, company_name, number_of_employee, address_id, company_logo_id) VALUES
    (1, 'Là thành viên thuộc Tập đoàn công nghệ hàng đầu Việt Nam FPT, Công ty Cổ phần Viễn thông FPT (tên gọi tắt là FPT Telecom) hiện là một trong những nhà cung cấp dịch vụ viễn thông và Internet có uy tín và được khách hàng yêu mến tại Việt Nam và khu vực.
Thành lập ngày 31/01/1997, khởi nguồn từ Trung tâm Dịch vụ Trực tuyến do 4 thành viên sáng lập cùng sản phẩm mạng Intranet đầu tiên của Việt Nam mang tên “Trí tuệ Việt Nam – TTVN”, sản phẩm được coi là đặt nền móng cho sự phát triển của Internet tại Việt Nam. Sau 20 năm hoạt động, FPT Telecom đã lớn mạnh vượt bậc với hơn 7,000 nhân viên chính thức, gần 200 văn phòng điểm giao dịch thuộc hơn 80 chi nhánh tại 59 tỉnh thành trên toàn quốc. Bên cạnh đó, Công ty đã và đang đặt dấu ấn trên trường quốc tế bằng 8 chi nhánh trải dài khắp Campuchia, cũng như việc được cấp giấy phép kinh doanh dịch vụ tại Myanmar.
Với sứ mệnh tiên phong đưa Internet đến với người dân Việt Nam và mong muốn mỗi gia đình Việt Nam đều sử dụng ít nhất một dịch vụ của FPT Telecom, đồng hành cùng phương châm “Khách hàng là trọng tâm”, chúng tôi không ngừng nỗ lực đầu tư hạ tầng, nâng cấp chất lượng sản phẩm – dịch vụ, tăng cường ứng dụng công nghệ mới để mang đến cho khách hàng những trải nghiệm sản phẩm dịch vụ vượt trội.
', 'Công ty Cổ phần viễn thông FPT', '5000+', 1, 1);

INSERT INTO address (id, detail_address, district, province, ward) VALUES
    (2, 'Tầng 11, Tòa nhà CMC, 11 đường Duy Tân', 'Cầu Giấy', 'Hà Nội', 'Phố Duy Tân');
INSERT INTO company (user_id, company_description, company_name, number_of_employee, address_id, company_logo_id) VALUES
    (2, 'CMC Telecom là một trong 8 công ty thành viên của Tập đoàn Công nghệ CMC. Với gần 9 năm xây dựng, phát triển, CMC Telecom là công ty hạ tầng viễn thông nằm trong TOP 4 các công ty viễn thông hàng đầu Việt Nam. CMC Telecom là nhà cung cấp đầu tiên áp dụng công nghệ GPON và sở hữu hạ tầng truyền dẫn 100% cáp quang.
CMC Telecom được biết tới là nhà cung cấp dịch vụ toàn diện với các sản phẩm truyền số liệu chuyên biệt cho doanh nghiệp như Internet Leased Line, dịch vụ Wan nội hạt; liên tỉnh; quốc tế, Internet cáp quang cho doanh nghiệp và đặc biệt là sản phẩm Internet trên mạng Truyền hình cáp Việt Nam (VTVnet) cho gia đình. Không chỉ cung cấp những dịch vụ viễn thông cơ bản, CMC Telecom còn cung cấp cả các dịch vụ gia tăng cho khối khách hàng doanh nghiệp như Video Conference, Bảo mật Hệ thống và dịch vụ Managed Service…
Hai (02) Data Center trung lập tiêu chuẩn Tier3 của CMC Telecom tại Hà Nội và TP Hồ Chí Minh (thuộc Liên minh Dữ liệu Á Châu ADCA) đã nhận được chứng nhận về bảo mật, an toàn thông tin theo tiêu chuẩn quốc tế IEC/ISO 27001.
* VÌ SAO BẠN LẠI LỰA CHỌN CMC Telecom LÀ NƠI PHÁT TRIỂN SỰ NGHIỆP:
Ngay từ những ngày đầu thành lập, chúng tôi luôn mong muốn tập hợp được đội ngũ nhân sự có trình độ chuyên môn cao, có tư cách đạo đức tốt để cùng hợp tác, phát triển và chung tay xây dựng công ty ngày càng lớn mạnh và phát triển không ngừng.
Đối với chúng tôi, nhân lực là tài sản quý giá nhất. Sự thành công của CMC Telecom ngày hôm nay có được là nhờ sự đóng góp to lớn của những thế hệ đã và đang làm việc từ khi mới thành lập cho đến hôm nay. Chúng tôi đã, đang và mãi mãi ý thức như vậy.
Chúng tôi cam kết sẽ tạo điều kiện cho các ứng cử viên trúng tuyển được làm việc trong một môi trường chuyên nghiệp, thân thiện và cạnh tranh lành mạnh. Chúng tôi cam kết sẽ tạo ra những cơ hội để tất cả cán bộ nhân viên trong công ty có thể phát huy tốt nhất khả năng, chuyên môn, nhiệt huyết làm việc để cống hiến cho công ty, cho xã hội, và được hưởng các quyền lợi xứng đáng với công sức của mình. Chúng tôi cam kết đảm bảo thu nhập ổn định ở mức cao, đáp ứng đầy đủ các quyền lợi của người lao động được quy định tại luật lao động.., và hơn thế nữa.
Hãy đến với chúng tôi để xây dựng CMC Telecom ngày càng phát triển và vững mạnh.
* Địa Điểm:
- Trụ sở chính: Tầng 11 tòa nhà CMC, Đường Duy Tân, Q. Cầu Giấy, TP. Hà Nội
- Chi nhánh Hà Nội: Tầng 12 tòa nhà CMC, Đường Duy Tân, Q. Cầu Giấy, TP. Hà Nội
- Chi nhánh Hải Phòng:104 Văn Cao, P. Đằng Giang, Q. Ngô Quyền, TP. Hải Phòng
- Chi nhánh Đà Nẵng: 383 Nguyễn Văn Linh – P.Thạc Gián – Q.Thanh Khê – Tp.Đà Nẵng
- Chi nhánh HCM: Tầng 4, tòa nhà Paxsky3, số 225 Bis Đường Nam Kỳ Khởi Nghĩa, Phường 7, Quận 3, TP Hồ Chí Minh.',
     'Công ty cổ phần Hạ tầng Viễn thông CMC Telecom', '1000', 2, 3);

INSERT INTO address (id, detail_address, district, province, ward) VALUES
    (3, 'Tầng 5, tháp A, tòa nhà Keangnam', 'Nam Từ Liêm', 'Hà Nội', 'Mễ trì');
INSERT INTO company (user_id, company_description, company_name, number_of_employee, address_id, company_logo_id) VALUES
    (3, 'Giới thiệu: AMELA là doanh nghiệp cung cấp các dịch vụ, giải pháp phần mềm và CNTT, được thành lập từ năm 2019. Chúng tôi có trụ sở chính tại Hà Nội và văn phòng quốc tế tại Tokyo, Nhật Bản. Tuy là một doanh nghiệp trẻ, nhưng với tốc độ tăng trưởng hàng đầu trong ngành IT Việt Nam năm 2020, AMELA nắm giữ đầy đủ nguồn lực và kinh nghiệm để sẵn sàng phát triển cùng tất cả doanh nghiệp ở mọi ngành nghề, cũng như tham gia đóng góp vào phát triển kinh tế xã hội.
1.Triết lý và Tầm nhìn
Triết lý:
Ứng dụng tối đa tiềm năng của công nghệ để giải quyết các vấn đề của khách hàng và cả xã hội như: tình trạng thiếu nguồn nhân lực, giải pháp tự động hóa và tận dụng phương tiện số.
Tầm nhìn chiến lược
Công ty công nghệ hàng đầu Việt Nam, góp phần vào số hoá xã hội.
2. Giá trị cốt lõi
Uy tín với khách hàng và con người AMELA
3. 10 triết lý hoạt động
01 Lợi ích khách hàng được đặt lên trên hết
02 Luôn đặt chữ Đức lên trên chữ Tài
03 Coi trọng sự hoàn hảo hoàn chỉnh của sản phẩm hơn là quy mô
04 Là một công ty có tính chuyên môn hóa cao, AMELA luôn cố gắng tìm kiếm những con người tốt nhất cho từng vị trí.
05 Ở AMELA, mọi cơ hội phát triển được chia đều và công bằng cho tất cả mọi người.
06 AMELA muốn đi xa trên con đường mình đã chọ hơn là đi nhanh, và vì thế AMELA coi trọng teamwork hơn là cái tôi của từng cá nhân.
07 Không ngừng sáng tạo cho mỗi sản phẩm mình tạo ra.
08 Sếp không phải là người ra lệnh cho nhân viên, mà sếp là người tạo điều kiện tốt nhất cho nhân viên phát huy được tối đa năng lực của mình.
09 Tài sản lớn nhất của AMELA là uy tín với khách hàng và con người của AMELA.
10 Đối với doanh nghiệp, lợi nhuận là vô cùng quan trọng, tuy nhiên AMELA sẵn',
     'CÔNG TY CỔ PHẦN CÔNG NGHỆ AMELA VIỆT NAM', '500', 3, 2);

INSERT INTO address (id, detail_address, district, province, ward) VALUES
    (4, 'Hapulico Complex, Center Building, Số 1 Nguyễn Huy Tưởng', 'Thanh Xuân', 'Hà Nội', 'Thanh Xuân Trung');
INSERT INTO company (user_id, company_description, company_name, number_of_employee, address_id, company_logo_id)
    VALUES (4, 'Được thành lập vào năm 2006, Công ty CP VCCorp (VCCorp) là công ty tiên phong trong lĩnh vực công nghệ và nội dung số. Với 15 năm hình thành và phát triển, VCCorp đã xây dựng được một hệ sinh thái Internet rộng lớn với rất nhiều sản phẩm sáng tạo, hữu ích trong nhiều lĩnh vực (quảng cáo trực tuyến, thương mại điện tử, trò chơi trực tuyến...) phủ sóng trên 90% người sử dụng Internet và mobile, có giá trị đóng góp lớn vào sự phát triển của Internet Việt Nam trong hơn một thập kỷ qua.
Để có được hàng loạt sản phẩm công nghệ mang tính đột phá và ưu việt ngày hôm nay, VCCorp đã xây dựng một đội ngũ nhân sự hùng hậu với trên 1800 nhân viên làm việc và hoạt động chuyên nghiệp tại Hà Nội, thành phố Hồ Chí Minh và các văn phòng đại diện, chi nhánh khác tại 06 thành phố phát triển nhất Việt Nam. Với tinh thần “Innovation-NonStop – Sáng tạo cho cộng đồng”, VCCorp là mái nhà chung dành cho những người trẻ yêu công nghệ, đam mê sáng tạo.
Admicro thuộc VCCorp hiện là đơn vị nắm giữ thị phần lớn nhất trên thị trường quảng cáo trực tuyến Việt Nam với 20 sites nội bộ, 5 báo độc quyền và 151 sites đối tác. Đã tiếp cận được 33 trên 36 triệu người dùng internet tại Việt Nam; độ phủ lên đến 97.6% cộng đồng mạng Việt Nam trên PC và 95% trên trên thiết bị di động. Đồng hành cùng hơn 1,000 thương hiệu lớn trong nhiều lĩnh vực và triển khai thành công hơn 12,000 chiến dịch truyền thông mỗi năm.',
            'Công ty Cổ phần VCCorp', '1000', 4, 5);

INSERT INTO address (id, detail_address, district, province, ward) VALUES
    (5, '165 Phố Thái Hà', 'Đống Đa', 'Hà Nội', 'Láng Hạ');
INSERT INTO company (user_id, company_description, company_name, number_of_employee, address_id, company_logo_id) VALUES
    (5, 'VNG là một trong những doanh nghiệp đầu tiên của Việt Nam đi tiên phong trong ngành công nghiệp Internet. Trẻ trung, năng động, nhiệt huyết, không ngừng phát triển và học tập những công nghệ mới, chúng tôi đã đạt nhiều thành tựu về công nghệ số cho các nhu cầu khác nhau với 3 loại hình:
- Nội dung số (Zing.vn..)
- Giải trí trực tuyến, liên kết cộng đồng (Zalo, Zingme)
- Thương mại điện tử (123pay, 123phim)
cùng các sản phẩm Game nổi bật: Kiếm Thế, Võ Lâm Truyền Kỳ....', 'Công ty Cổ phần VNG', '1000', 5, 4);

/*Candidate*/
INSERT INTO user (id, email, enabled, locked, password, create_date) VALUES
    (6, 'nguyendanghoangdao2002@gmail.com', true, false, '$2a$10$sW0xlxPWnY6m993ofp.S8eu3KnNx6QoDw.sq3vItwIbbeYFemYB7y', '2023-03-28'),
    (7, 'nguyenthithanhhuyen2002@gmail.com', true, false, '$2a$10$sW0xlxPWnY6m993ofp.S8eu3KnNx6QoDw.sq3vItwIbbeYFemYB7y', '2023-03-21'),
    (8, 'tranthikimbac2002@gmail.com', true, false, '$2a$10$sW0xlxPWnY6m993ofp.S8eu3KnNx6QoDw.sq3vItwIbbeYFemYB7y', '2023-03-17'),
    (9, 'trandinhcuong2002@gmail.com', true, false, '$2a$10$sW0xlxPWnY6m993ofp.S8eu3KnNx6QoDw.sq3vItwIbbeYFemYB7y', '2023-03-12'),
    (10, 'phanvantiendung2002@gmail.com', true, false, '$2a$10$sW0xlxPWnY6m993ofp.S8eu3KnNx6QoDw.sq3vItwIbbeYFemYB7y', '2023-03-01');
INSERT INTO user_role (user_id, role_id) VALUES
    (6, 3), (7, 3), (8, 3), (9, 3), (10, 3);

INSERT INTO candidate (user_id, avatar_id, contact_email, date_of_birth, full_name, phone_number, self_description, sex) VALUES
    (6, 11, 'nguyendanghoangdao2002@gmail.com', '2002-02-19', 'Nguyễn Đăng Hoàng Đạo', '0325135251',
     'Sinh viên Trường đại học Công nghệ - Đại học Quốc Gia Hà Nội', 'Male');
INSERT INTO candidate (user_id, avatar_id, contact_email, date_of_birth, full_name, phone_number, self_description, sex) VALUES
    (7, 12, 'nguyenthithanhhuyen2002@gmail.com', '2002-02-20', 'Nguyễn Thị Thanh Huyền', '0325135251',
     'Sinh viên Trường đại học Công nghệ - Đại học Quốc Gia Hà Nội', 'Female');
INSERT INTO candidate (user_id, avatar_id, contact_email, date_of_birth, full_name, phone_number, self_description, sex) VALUES
    (8, 13, 'tranthikimbac2002@gmail.com', '2002-02-21', 'Trần Thị Kim Bắc', '0325135251',
     'Sinh viên Trường đại học Công nghệ - Đại học Quốc Gia Hà Nội', 'Female');
INSERT INTO candidate (user_id, avatar_id, contact_email, date_of_birth, full_name, phone_number, self_description, sex) VALUES
    (9, 14, 'trandinhcuong2002@gmail.com', '2002-02-22', 'Trần Đình Cường', '0325135251',
     'Sinh viên Trường đại học Công nghệ - Đại học Quốc Gia Hà Nội', 'Male');
INSERT INTO candidate (user_id, avatar_id, contact_email, date_of_birth, full_name, phone_number, self_description, sex) VALUES
    (10, 15, 'phanvantiendung2002@gmail.com', '2002-02-23', 'Phan Văn Tiến Dũng', '0325135251',
     'Sinh viên Trường đại học Công nghệ - Đại học Quốc Gia Hà Nội', 'Male');

/*Job*/
INSERT INTO job (id, close_date, job_description, job_title, major,
     number_of_hiring, open_date, require_experience, salary, sex, status, working_form, company_user_id, address_id)
VALUES (1, '2023-05-01', 'Tuyển lập trình viên Java', 'Lập trình viên Java', 'Công nghệ thông tin',
        20, '2023-02-01', '> 1 year', 'Thỏa thuận', 'Male', 'OPEN', 'Fulltime', 1, 1);
INSERT INTO job (id, close_date, job_description, job_title, major,
                 number_of_hiring, open_date, require_experience, salary, sex, status, working_form, company_user_id, address_id)
VALUES (2, '2023-05-01', 'Tuyển lập trình viên Android', 'Lập trình viên Android', 'Công nghệ thông tin',
        20, '2023-02-01', '> 1 year', 'Thỏa thuận', 'Male', 'OPEN', 'Fulltime', 1, 1);

INSERT INTO job (id, close_date, job_description, job_title, major,
                 number_of_hiring, open_date, require_experience, salary, sex, status, working_form, company_user_id, address_id)
VALUES (3, '2023-05-01', 'Tuyển lập trình viên Java', 'Lập trình viên Java', 'Công nghệ thông tin',
        20, '2023-02-01', '> 1 year', 'Thỏa thuận', 'Male', 'OPEN', 'Fulltime', 2, 2);
INSERT INTO job (id, close_date, job_description, job_title, major,
                 number_of_hiring, open_date, require_experience, salary, sex, status, working_form, company_user_id, address_id)
VALUES (4, '2023-05-01', 'Tuyển lập trình viên Android', 'Lập trình viên Android', 'Công nghệ thông tin',
        20, '2023-02-01', '> 1 year', 'Thỏa thuận', 'Male', 'OPEN', 'Fulltime', 2, 2);

INSERT INTO job (id, close_date, job_description, job_title, major,
                 number_of_hiring, open_date, require_experience, salary, sex, status, working_form, company_user_id, address_id)
VALUES (5, '2023-05-01', 'Tuyển lập trình viên Java', 'Lập trình viên Java', 'Công nghệ thông tin',
        20, '2023-02-01', '> 1 year', 'Thỏa thuận', 'Male', 'OPEN', 'Fulltime', 3, 3);
INSERT INTO job (id, close_date, job_description, job_title, major,
                 number_of_hiring, open_date, require_experience, salary, sex, status, working_form, company_user_id, address_id)
VALUES (6, '2023-05-01', 'Tuyển lập trình viên Android', 'Lập trình viên Android', 'Công nghệ thông tin',
        20, '2023-02-01', '> 1 year', 'Thỏa thuận', 'Male', 'OPEN', 'Fulltime', 3, 3);

INSERT INTO job (id, close_date, job_description, job_title, major,
                 number_of_hiring, open_date, require_experience, salary, sex, status, working_form, company_user_id, address_id)
VALUES (7, '2023-05-01', 'Tuyển lập trình viên Java', 'Lập trình viên Java', 'Công nghệ thông tin',
        20, '2023-02-01', '> 1 year', 'Thỏa thuận', 'Male', 'OPEN', 'Fulltime', 4, 4);
INSERT INTO job (id, close_date, job_description, job_title, major,
                 number_of_hiring, open_date, require_experience, salary, sex, status, working_form, company_user_id, address_id)
VALUES (8, '2023-05-01', 'Tuyển lập trình viên Android', 'Lập trình viên Android', 'Công nghệ thông tin',
        20, '2023-02-01', '> 1 year', 'Thỏa thuận', 'Male', 'OPEN', 'Fulltime', 4, 4);

INSERT INTO job (id, close_date, job_description, job_title, major,
                 number_of_hiring, open_date, require_experience, salary, sex, status, working_form, company_user_id, address_id)
VALUES (9, '2023-05-01', 'Tuyển lập trình viên Java', 'Lập trình viên Java', 'Công nghệ thông tin',
        20, '2023-02-01', '> 1 year', 'Thỏa thuận', 'Male', 'OPEN', 'Fulltime', 5, 5);
INSERT INTO job (id, close_date, job_description, job_title, major,
                 number_of_hiring, open_date, require_experience, salary, sex, status, working_form, company_user_id, address_id)
VALUES (10, '2023-05-01', 'Tuyển lập trình viên Android', 'Lập trình viên Android', 'Công nghệ thông tin',
        20, '2023-02-01', '> 1 year', 'Thỏa thuận', 'Male', 'OPEN', 'Fulltime', 5, 5);

/*Job application*/
INSERT INTO job_application (id, applied_date, status, candidate_user_id, job_id, cv_file_id)
VALUES (1, '2023-02-03', 'Waiting', 6, 1, 6),
       (2, '2023-02-03', 'Waiting', 7, 1, 7),
       (3, '2023-02-03', 'Waiting', 8, 1, 8),
       (4, '2023-02-03', 'Waiting', 9, 1, 9),
       (5, '2023-02-03', 'Waiting', 7, 2, 7),
       (6, '2023-02-03', 'Waiting', 8, 2, 8),
       (7, '2023-02-03', 'Waiting', 9, 2, 9),
       (8, '2023-02-03', 'Waiting', 10, 2, 10),
       (9, '2023-02-03', 'Waiting', 8, 3, 8),
       (10, '2023-02-03', 'Waiting', 9, 3, 9),
       (11, '2023-02-03', 'Waiting', 10, 3, 10),
       (12, '2023-02-03', 'Waiting', 6, 3, 6),
       (13, '2023-02-03', 'Waiting', 9, 4, 9),
       (14, '2023-02-03', 'Waiting', 10, 4, 10),
       (15, '2023-02-03', 'Waiting', 6, 4, 6),
       (16, '2023-02-03', 'Waiting', 7, 4, 7),
       (17, '2023-02-03', 'Waiting', 10, 5, 10),
       (18, '2023-02-03', 'Waiting', 6, 5, 6),
       (19, '2023-02-03', 'Waiting', 7, 5, 7),
       (20, '2023-02-03', 'Waiting', 8, 5, 8),
       (21, '2023-02-03', 'Waiting', 6, 6, 6),
       (22, '2023-02-03', 'Waiting', 7, 6, 7),
       (23, '2023-02-03', 'Waiting', 8, 6, 8),
       (24, '2023-02-03', 'Waiting', 9, 6, 9),
       (25, '2023-02-03', 'Waiting', 10, 7, 10);

/*Major*/
INSERT INTO major (id, name) VALUES
     (1, 'Công nghệ thông tin'),
     (2, 'Hệ thống thông tin'),
     (3, 'An toàn thông tin'),
     (4, 'Quản trị kinh doanh'),
     (5, 'Mạng máy tính và truyền thông dữ liệu'),
     (6, 'Logistic và quản lý chuỗi cung ứng'),
     (7, 'Kỹ thuật máy tính'),
     (8, 'Công nghệ truyền thông')
