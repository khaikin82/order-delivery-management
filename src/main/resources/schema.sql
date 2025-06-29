-- Drop tables if they exist (in reverse order of dependencies)
DROP TABLE IF EXISTS order_tracking_history;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS users;

-- thỏa mãn 1NF - Mỗi cột chỉ chứa giá trị nguyên tử, ko có tập hợp hay list
-- thỏa mãn 2NF - khóa chính chỉ có 1 trường là id, nên ko có phụ thụộc 1 phần vào khóa chính
-- thỏa mãn 3NF - tất cả đều phụ thuộc trực tiếp vào khóa chính id, ko có phụ thuộc bắc cầu
-- thỏa mãn BCNF - Không tồn tại phụ thuộc hàm nào mà bên trái không phải là khóa chính hoặc khóa candidate (khóa siêu)

-- khóa siêu là cột thỏa mãn tính duy nhất (unique) và tính tối thiểu (không thể bỏ bớt bất kỳ thuộc tính nào trong khóa
-- candidate mà vẫn giữ được tính duy nhất.) (ví dụ username là khóa siêu trong users)

-- thỏa mãn 4NF - Không có bảng nào chứa các thuộc tính đa trị độc lập.

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255),
    phone VARCHAR(20),
    full_name VARCHAR(255),
    role ENUM('CUSTOMER', 'DELIVERY_STAFF', 'ADMIN') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_code VARCHAR(255) NOT NULL UNIQUE,
    receiver_name VARCHAR(255),
    receiver_phone VARCHAR(20),
    receiver_address VARCHAR(255),
    description TEXT,
    weight DOUBLE,
    size VARCHAR(100),
    pickup_address VARCHAR(255),
    delivery_address VARCHAR(255),
    note TEXT,
    customer_id BIGINT,
    staff_id BIGINT,
    status ENUM('CREATED', 'ASSIGNED', 'PICKED_UP', 'IN_TRANSIT', 'DELIVERED', 'COMPLETED', 'CANCELLED') NOT NULL,
    created_at DATETIME,
    updated_at DATETIME,

    CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES users(id),
    CONSTRAINT fk_staff FOREIGN KEY (staff_id) REFERENCES users(id)
);

CREATE TABLE order_tracking_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    status ENUM('CREATED', 'ASSIGNED', 'PICKED_UP', 'IN_TRANSIT', 'DELIVERED', 'COMPLETED', 'CANCELLED') NOT NULL,
    changed_at DATETIME NOT NULL,
    changed_by VARCHAR(255) NOT NULL,
    CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

-- Nếu một cột đã là UNIQUE hoặc PRIMARY KEY, thì MySQL đã tự tạo index cho nó rồi. Ghi thêm cho rõ :v

--CREATE INDEX idx_users_username ON users(username); -- đã UNIQUE, nên MySQL sẽ tạo sẵn index
CREATE INDEX idx_users_role ON users(role); -- giúp lọc user theo vai trò, admin có dashboard để quản lý customer, staff
CREATE INDEX idx_users_created_at ON users(created_at); -- hiển thị user theo thứ tự đc tạo gần đây nhất

--CREATE INDEX idx_orders_order_code ON orders(order_code); -- đã UNIQUE, nên MySQL sẽ tạo sẵn index
CREATE INDEX idx_orders_customer_id ON orders(customer_id); -- lọc order theo username của customer (get myOrder)
CREATE INDEX idx_orders_staff_id ON orders(staff_id); -- lọc order theo username của staff (get myStaffOrder)
CREATE INDEX idx_orders_status ON orders(status); -- lọc status
CREATE INDEX idx_orders_created_at ON orders(created_at); -- hiển thị order theo thứ tự đc tạo gần đây nhất

CREATE INDEX idx_tracking_order_id ON order_tracking_history(order_id); -- order_id: dùng để lấy toàn bộ lịch sử của 1 đơn hàng


---- users
----CREATE INDEX idx_users_username ON users(username); -- username UNIQUE, nên MySQL sẽ tạo sẵn index
--CREATE INDEX idx_users_role ON users(role);
--CREATE INDEX idx_users_created_at ON users(created_at);
--
---- orders
----CREATE INDEX idx_orders_order_code ON orders(order_code); -- order_code UNIQUE, nên MySQL sẽ tạo sẵn index
--CREATE INDEX idx_orders_customer_id ON orders(customer_id);
--CREATE INDEX idx_orders_staff_id ON orders(staff_id);
--CREATE INDEX idx_orders_status ON orders(status);
--CREATE INDEX idx_orders_created_at ON orders(created_at);
--
---- order_tracking_history
--CREATE INDEX idx_tracking_order_id ON order_tracking_history(order_id);
