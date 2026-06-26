DESKTOP SUDOKU GAME

Một ứng dụng game Sudoku trên nền tảng máy tính được phát triển hoàn chỉnh với nhiều tính năng hỗ trợ người chơi, giao diện tương tác trực quan và hệ thống quản lý dữ liệu an toàn.

CÔNG NGHỆ SỬ DỤNG:

- Ngôn ngữ lập trình: Java

- Giao diện người dùng (GUI): Java Swing

- Cơ sở dữ liệu: SQLite

- Kết nối & Kiến trúc: JDBC, áp dụng mô hình DAO (Data Access Object)

- Cấu trúc dữ liệu & Thuật toán: Stack, Thuật toán quay lui (Backtracking Algorithm)

TÍNH NĂNG NỔI BẬT

- Giao diện tương tác cao: Xây dựng bằng Java Swing với các xử lý sự kiện giúp trải nghiệm chọn ô, điền số và tương tác mượt mà.

- Gợi ý & Tự động giải: Tích hợp thuật toán Backtracking mạnh mẽ để tạo ra các gợi ý nước đi chính xác và tự động giải quyết các bảng Sudoku từ dễ đến khó.

- Hoàn tác nước đi: Ứng dụng cấu trúc dữ liệu Stack để lưu trữ lịch sử các nước đi, cho phép người dùng quay lại thao tác trước đó một cách linh hoạt mà không bị lỗi logic.

- Hệ thống lưu trữ: Sử dụng SQLite và JDBC để lưu trạng thái game đang chơi, quản lý file save và duy trì bảng xếp hạng (Leaderboard) những người chơi có thời gian hoàn thành tốt nhất.

HƯỚNG DẪN CÀI ĐẶT VÀ CHẠY DỰ ÁN

1. Clone repository về máy: git clone <link-github-của-bạn>

2. Mở dự án: Mở thư mục dự án bằng IDE của bạn (khuyến nghị sử dụng IntelliJ IDEA).

3. Cấu hình thư viện: Đảm bảo bạn đã thêm file thư viện .jar của SQLite JDBC vào phần External Libraries của dự án.

4. Khởi chạy: Điều hướng đến file Main.java (hoặc Main), tìm hàm public static void main(String[] args) và chạy chương trình từ đây.
