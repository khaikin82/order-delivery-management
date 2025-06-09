-- Tạo user (3 khách hàng, 3 nhân viên giao hàng, 1 admin)
INSERT INTO users (username, password, email, phone, full_name, role) VALUES
-- Customers
('customer', '$2a$10$7.pvhNxlffj884Xh7tUO3u53XaJHOedOvZkBEFGOrexOh3mItoDiu', 'customer@example.com', '0901000000',
'Nguyen Van Nam', 'CUSTOMER'),
('customer1', '$2a$10$7.pvhNxlffj884Xh7tUO3u53XaJHOedOvZkBEFGOrexOh3mItoDiu', 'cus1@example.com', '0901000001', 'Nguyen Van A', 'CUSTOMER'),
('customer2', '$2a$10$7.pvhNxlffj884Xh7tUO3u53XaJHOedOvZkBEFGOrexOh3mItoDiu', 'cus2@example.com', '0901000002', 'Tran Thi B', 'CUSTOMER'),
('customer3', '$2a$10$7.pvhNxlffj884Xh7tUO3u53XaJHOedOvZkBEFGOrexOh3mItoDiu', 'cus3@example.com', '0901000003', 'Le Hoang C', 'CUSTOMER'),

-- Delivery Staff
('staff', '$2a$10$6XSO38dCW8j3Ltogc781DedOTNaVyg8izOUielxGijXUt4hwb/TqS', 'staff@example.com', '0911000000',
 'Tran Anh Tuan', 'DELIVERY_STAFF'),
('staff1', '$2a$10$6XSO38dCW8j3Ltogc781DedOTNaVyg8izOUielxGijXUt4hwb/TqS', 'staff1@example.com', '0911000001', 'Pham Van D', 'DELIVERY_STAFF'),
('staff2', '$2a$10$6XSO38dCW8j3Ltogc781DedOTNaVyg8izOUielxGijXUt4hwb/TqS', 'staff2@example.com', '0911000002', 'Hoang Thi E', 'DELIVERY_STAFF'),
('staff3', '$2a$10$6XSO38dCW8j3Ltogc781DedOTNaVyg8izOUielxGijXUt4hwb/TqS', 'staff3@example.com', '0911000003', 'Do Quang F', 'DELIVERY_STAFF'),

-- Admin
('admin', '$2a$10$6xd0E0HNU2/E7xWtpdRONOpxRJTUksJGeboX7jaJct4x9eGBLARcW', 'admin@example.com', '0999999999', 'Hoang Dang Khai', 'ADMIN');


INSERT INTO orders (
    order_code, receiver_name, receiver_phone, receiver_address,
    description, weight, size, pickup_address, delivery_address, note,
    customer_id, staff_id, status, created_at, updated_at
) VALUES
-- Đơn hàng mới tạo, chưa có nhân viên giao
('ORD001', 'Nguyen A', '0901111111', '123 ABC St', 'Tài liệu A4', 0.5, 'A4', 'Warehouse A1', '123 ABC St', 'Giao buổi sáng', 1, NULL, 'CREATED', NOW() - INTERVAL 5 DAY, NOW() - INTERVAL 5 DAY),

-- Đơn hàng đã được giao nhân viên
('ORD002', 'Tran B', '0902222222', '456 DEF St', 'Hàng thời trang', 2.0, '30x20x10', 'Warehouse A2', '456 DEF St', 'Giao giờ hành chính', 1, 4, 'ASSIGNED', NOW() - INTERVAL 4 DAY, NOW() - INTERVAL 4 DAY),

-- Đơn hàng đã được lấy hàng bởi nhân viên
('ORD003', 'Le C', '0903333333', '789 GHI St', 'Thiết bị điện tử', 3.5, '40x30x20', 'Warehouse A3', '789 GHI St', '', 2, 4, 'PICKED_UP', NOW() - INTERVAL 3 DAY, NOW() - INTERVAL 3 DAY),

-- Đơn đang vận chuyển
('ORD004', 'Pham D', '0904444444', '159 JKL St', 'Tài liệu và hàng mẫu', 1.2, 'A4', 'Warehouse A4', '159 JKL St', 'Không được làm mất!', 2, 6, 'IN_TRANSIT', NOW() - INTERVAL 3 DAY, NOW() - INTERVAL 2 DAY),

-- Đơn đã giao tới người nhận
('ORD005', 'Hoang E', '0905555555', '753 MNO St', 'Thực phẩm khô', 4.1, '50x40x30', 'Warehouse A5', '753 MNO St', '', 3, 5, 'DELIVERED', NOW() - INTERVAL 4 DAY, NOW() - INTERVAL 1 DAY),

-- Đơn đã hoàn thành (đã giao, khách xác nhận)
('ORD006', 'Dang F', '0906666666', '321 PQR St', 'Vật dụng gia đình', 6.0, '60x50x40', 'Warehouse A6', '321 PQR St', 'Giao nhanh giúp', 3, 6, 'COMPLETED', NOW() - INTERVAL 7 DAY, NOW() - INTERVAL 1 DAY),

-- Đơn đã hủy (khách hủy, chưa giao)
('ORD007', 'Nguyen G', '0907777777', '147 STU St', 'Hàng dễ vỡ', 1.0, 'Fragile', 'Warehouse A7', '147 STU St', 'Khách hủy', 1, NULL, 'CANCELLED', NOW() - INTERVAL 6 DAY, NOW() - INTERVAL 6 DAY),

-- Một số đơn hàng khác để đa dạng dữ liệu
('ORD008', 'Pham H', '0908888888', '258 VWX St', 'Sách giáo khoa', 2.3, 'A4', 'Warehouse A8', '258 VWX St', '', 2, 4, 'ASSIGNED', NOW() - INTERVAL 2 DAY, NOW() - INTERVAL 2 DAY),

('ORD009', 'Le I', '0909999999', '369 YZ St', 'Đồ chơi trẻ em', 3.0, '50x50x30', 'Warehouse A9', '369 YZ St', '', 3, 5, 'IN_TRANSIT', NOW() - INTERVAL 2 DAY, NOW() - INTERVAL 1 DAY),

('ORD010', 'Tran J', '0910000000', '147 ABC St', 'Thức ăn cho thú cưng', 2.0, '25x25x25', 'Warehouse A10', '147 ABC St', 'Giao sớm nhất có thể', 2, 6, 'PICKED_UP', NOW() - INTERVAL 1 DAY, NOW());


INSERT INTO order_tracking_history (order_id, status, changed_at, changed_by) VALUES
-- ORD001 (CREATED)
(1, 'CREATED', NOW() - INTERVAL 5 DAY, 'customer1'),

-- ORD002 (CREATED → ASSIGNED)
(2, 'CREATED', NOW() - INTERVAL 6 DAY, 'customer1'),
(2, 'ASSIGNED', NOW() - INTERVAL 4 DAY, 'admin1'),

-- ORD003 (CREATED → ASSIGNED → PICKED_UP)
(3, 'CREATED', NOW() - INTERVAL 7 DAY, 'customer2'),
(3, 'ASSIGNED', NOW() - INTERVAL 5 DAY, 'admin1'),
(3, 'PICKED_UP', NOW() - INTERVAL 3 DAY, 'staff1'),

-- ORD004 (CREATED → ASSIGNED → PICKED_UP → IN_TRANSIT)
(4, 'CREATED', NOW() - INTERVAL 7 DAY, 'customer2'),
(4, 'ASSIGNED', NOW() - INTERVAL 6 DAY, 'admin2'),
(4, 'PICKED_UP', NOW() - INTERVAL 4 DAY, 'staff3'),
(4, 'IN_TRANSIT', NOW() - INTERVAL 2 DAY, 'staff3'),

-- ORD005 (CREATED → ASSIGNED → PICKED_UP → IN_TRANSIT → DELIVERED)
(5, 'CREATED', NOW() - INTERVAL 9 DAY, 'customer3'),
(5, 'ASSIGNED', NOW() - INTERVAL 7 DAY, 'admin2'),
(5, 'PICKED_UP', NOW() - INTERVAL 5 DAY, 'staff2'),
(5, 'IN_TRANSIT', NOW() - INTERVAL 3 DAY, 'staff2'),
(5, 'DELIVERED', NOW() - INTERVAL 1 DAY, 'staff2'),

-- ORD006 (CREATED → ASSIGNED → PICKED_UP → IN_TRANSIT → DELIVERED → COMPLETED)
(6, 'CREATED', NOW() - INTERVAL 10 DAY, 'customer3'),
(6, 'ASSIGNED', NOW() - INTERVAL 8 DAY, 'admin2'),
(6, 'PICKED_UP', NOW() - INTERVAL 6 DAY, 'staff3'),
(6, 'IN_TRANSIT', NOW() - INTERVAL 4 DAY, 'staff3'),
(6, 'DELIVERED', NOW() - INTERVAL 2 DAY, 'staff3'),
(6, 'COMPLETED', NOW() - INTERVAL 1 DAY, 'customer3'),

-- ORD007 (CREATED → CANCELLED)
(7, 'CREATED', NOW() - INTERVAL 7 DAY, 'customer1'),
(7, 'CANCELLED', NOW() - INTERVAL 6 DAY, 'customer1'),

-- ORD008 (CREATED → ASSIGNED)
(8, 'CREATED', NOW() - INTERVAL 3 DAY, 'customer2'),
(8, 'ASSIGNED', NOW() - INTERVAL 2 DAY, 'admin1'),

-- ORD009 (CREATED → ASSIGNED → PICKED_UP → IN_TRANSIT)
(9, 'CREATED', NOW() - INTERVAL 5 DAY, 'customer3'),
(9, 'ASSIGNED', NOW() - INTERVAL 4 DAY, 'admin1'),
(9, 'PICKED_UP', NOW() - INTERVAL 3 DAY, 'staff2'),
(9, 'IN_TRANSIT', NOW() - INTERVAL 1 DAY, 'staff2'),

-- ORD010 (CREATED → ASSIGNED → PICKED_UP)
(10, 'CREATED', NOW() - INTERVAL 4 DAY, 'customer2'),
(10, 'ASSIGNED', NOW() - INTERVAL 3 DAY, 'admin2'),
(10, 'PICKED_UP', NOW() - INTERVAL 1 DAY, 'staff3');
