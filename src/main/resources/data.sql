-- Tạo user (3 khách hàng, 3 nhân viên giao hàng, 1 admin)
INSERT INTO users (username, password, email, phone, role) VALUES
('customer1', 'pass1', 'cus1@example.com', '0901000001', 'CUSTOMER'),
('customer2', 'pass2', 'cus2@example.com', '0901000002', 'CUSTOMER'),
('customer3', 'pass3', 'cus3@example.com', '0901000003', 'CUSTOMER'),

('staff1', 'staffpass1', 'staff1@example.com', '0911000001', 'DELIVERY_STAFF'),
('staff2', 'staffpass2', 'staff2@example.com', '0911000002', 'DELIVERY_STAFF'),
('staff3', 'staffpass3', 'staff3@example.com', '0911000003', 'DELIVERY_STAFF'),

('admin', 'adminpass', 'admin@example.com', '0999999999', 'ADMIN');

-- Tạo đơn hàng ở nhiều trạng thái, nhiều khách hàng và nhân viên khác nhau
INSERT INTO orders (
    order_code, receiver_name, receiver_phone, receiver_address,
    description, weight, size, pickup_address, delivery_address, note,
    customer_id, staff_id, status, created_at, updated_at
) VALUES
-- Đơn hàng mới tạo
('ORD001', 'Nguyen A', '0901111111', '123 ABC St', 'Tài liệu A4', 0.5, 'A4', 'A1 Street', 'B1 Street', 'Giao buổi sáng', 1, 1, 'CREATED', NOW(), NOW()),
-- Đơn hàng đã được giao nhân viên
('ORD002', 'Tran B', '0902222222', '456 DEF St', 'Hàng thời trang', 2.0, '30x20x10', 'A2 Street', 'B2 Street', 'Giao giờ hành chính', 1, 2, 'ASSIGNED', NOW(), NOW()),
-- Đơn hàng đã được lấy hàng
('ORD003', 'Le C', '0903333333', '789 GHI St', 'Thiết bị điện tử', 3.5, '40x30x20', 'A3 Street', 'B3 Street', '', 2, 1, 'PICKED_UP', NOW(), NOW()),
-- Đơn đang vận chuyển
('ORD004', 'Pham D', '0904444444', '159 JKL St', 'Tài liệu và hàng mẫu', 1.2, 'A4', 'A4 Street', 'B4 Street', 'Không được làm mất!', 2, 3, 'IN_TRANSIT', NOW(), NOW()),
-- Đơn đã giao tới người nhận
('ORD005', 'Hoang E', '0905555555', '753 MNO St', 'Thực phẩm khô', 4.1, '50x40x30', 'A5 Street', 'B5 Street', '', 3, 2, 'DELIVERED', NOW(), NOW()),
-- Đơn đã hoàn thành
('ORD006', 'Dang F', '0906666666', '321 PQR St', 'Vật dụng gia đình', 6.0, '60x50x40', 'A6 Street', 'B6 Street', 'Giao nhanh giúp', 3, 3, 'COMPLETED', NOW(), NOW()),
-- Đơn đã hủy
('ORD007', 'Nguyen G', '0907777777', '147 STU St', 'Hàng dễ vỡ', 1.0, 'Fragile', 'A7 Street', 'B7 Street', 'Khách huỷ', 1, NULL, 'CANCELLED', NOW(), NOW()),

-- Một số đơn hàng khác để đa dạng dữ liệu
('ORD008', 'Pham H', '0908888888', '258 VWX St', 'Sách giáo khoa', 2.3, 'A4', 'A8 Street', 'B8 Street', '', 2, 1, 'ASSIGNED', NOW(), NOW()),
('ORD009', 'Le I', '0909999999', '369 YZ St', 'Đồ chơi trẻ em', 3.0, '50x50x30', 'A9 Street', 'B9 Street', '', 3, 2, 'IN_TRANSIT', NOW(), NOW()),
('ORD010', 'Tran J', '0910000000', '147 ABC St', 'Thức ăn cho thú cưng', 2.0, '25x25x25', 'A10 Street', 'B10 Street', 'Giao sớm nhất có thể', 2, 3, 'PICKED_UP', NOW(), NOW());


-- Insert lịch sử trạng thái cho đơn hàng 1
INSERT INTO order_status_history (order_id, status, updated_at, updated_by) VALUES
(1, 'CREATED',     '2025-06-05 08:00:00', 'customer01@example.com'),
(1, 'ASSIGNED',    '2025-06-05 08:10:00', 'admin@example.com'),
(1, 'PICKED_UP',   '2025-06-05 08:30:00', 'shipper01@example.com'),
(1, 'IN_TRANSIT',  '2025-06-05 08:50:00', 'shipper01@example.com'),
(1, 'DELIVERED',   '2025-06-05 09:15:00', 'shipper01@example.com'),
(1, 'COMPLETED',   '2025-06-05 09:30:00', 'customer01@example.com');

-- Insert lịch sử trạng thái cho đơn hàng 2 (bị huỷ)
INSERT INTO order_status_history (order_id, status, updated_at, updated_by) VALUES
(2, 'CREATED',   '2025-06-05 10:00:00', 'customer02@example.com'),
(2, 'CANCELLED', '2025-06-05 10:20:00', 'customer02@example.com');