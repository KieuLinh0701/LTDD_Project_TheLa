CREATE DATABASE DBTheLa;
GO
USE DBTheLa;
GO
CREATE TABLE users (
    userId BIGINT PRIMARY KEY IDENTITY,
    name NVARCHAR(100),
    email VARCHAR(320) NOT NULL,
    password VARCHAR(128) NOT NULL,
    code VARCHAR(10),
    address NVARCHAR(255),
    phone VARCHAR(15),
    role VARCHAR(20),
	image VARCHAR(500),
	isActivate BIT DEFAULT 0
)
GO

CREATE TABLE categories (
    categoryId BIGINT PRIMARY KEY IDENTITY,
    name NVARCHAR(100) NOT NULL,
    image VARCHAR(500),
    isActive BIT DEFAULT 1,
    isDelete BIT DEFAULT 0
);
GO

CREATE TABLE products (
    productId BIGINT PRIMARY KEY IDENTITY,
    categoryId BIGINT NOT NULL, 
    name NVARCHAR(100) NOT NULL,
	price INT,
    image VARCHAR(500),
	description NVARCHAR(500),
	createDate DateTime,
	status BIT DEFAULT 1,
    isActive BIT DEFAULT 1,
    isDelete BIT DEFAULT 0,
    FOREIGN KEY (categoryId) REFERENCES categories(categoryId)
);
GO

CREATE TABLE orders (
    orderId BIGINT PRIMARY KEY IDENTITY,
	userId BIGINT NOT NULL,
	totalPrice FLOAT NOT NULL,
	note NVARCHAR(500),
	orderDate DateTime,
	deliveryAddress NVARCHAR(255) NOT NULL, 
    deliveryPhone NVARCHAR(15) NOT NULL,
	status NVARCHAR(50) DEFAULT 'Processing',
    FOREIGN KEY (userId) REFERENCES users(userId)
);
GO

CREATE TABLE order_details (
    orderDetailId BIGINT PRIMARY KEY IDENTITY, 
    orderId BIGINT NOT NULL, 
    productId BIGINT NOT NULL, 
    quantity INT NOT NULL, 
    price INT NOT NULL, 
    FOREIGN KEY (orderId) REFERENCES orders(orderId), 
    FOREIGN KEY (productId) REFERENCES products(productId)
);
GO

INSERT INTO users (name, email, password, code, role, isActivate)
VALUES 
(N'Kiều Linh', 'uynukieulinh@gmail.com', 'Khong&01', '123456', 'Customer', 1);


INSERT INTO categories (name, image, isActive, isDelete)
VALUES
(N'Cà phê', 'image_url_coffee', 1, 0),
(N'Trà', 'image_url_tea', 1, 0),
(N'Thức uống đặc biệt', 'image_url_special', 1, 0),
(N'Trà sữa', 'image_url_tea', 1, 0),
(N'Topping', 'image_url_tea', 1, 0);

INSERT INTO products (categoryId, name, price, image, description, createDate, status, isActive, isDelete)
VALUES
-- Cà phê
(1, N'Cà phê đen', 25000, 'image_url_black_coffee', N'Cà phê đen truyền thống Việt Nam', '2025-01-01 07:00:00', 1, 1, 0),
(1, N'Cà phê sữa', 30000, 'image_url_milk_coffee', N'Cà phê sữa đặc ngon ngọt', '2025-02-19 09:30:00', 1, 1, 0),
(1, N'Cà phê đá', 27000, 'image_url_iced_coffee', N'Cà phê đen đá mát lạnh', '2025-01-03 08:15:00', 1, 1, 0),
(1, N'Espresso', 35000, 'image_url_espresso', N'Espresso đậm đà', '2025-01-04 10:00:00', 1, 1, 0),
(1, N'Cappuccino', 45000, 'image_url_cappuccino', N'Cappuccino pha kiểu Ý', '2025-02-15 15:45:00', 1, 1, 0),

-- Trà
(2, N'Trà xanh', 25000, 'image_url_green_tea', N'Trà xanh tươi mát', '2025-01-06 08:30:00', 1, 1, 0),
(2, N'Trà đào', 30000, 'image_url_peach_tea', N'Trà đào thơm ngọt', '2025-01-07 09:15:00', 1, 1, 0),
(2, N'Trà lài', 27000, 'image_url_jasmine_tea', N'Trà hoa lài thơm dịu', '2025-01-09 11:30:00', 1, 1, 0),
(2, N'Trà matcha', 40000, 'image_url_matcha_tea', N'Trà xanh matcha Nhật Bản', '2025-01-10 14:20:00', 1, 1, 0),

(3, N'Cacao nóng', 40000, 'image_url_hot_cacao', N'Thức uống cacao ấm áp', '2025-01-11 16:00:00', 1, 1, 0),
(3, N'Sinh tố xoài', 50000, 'image_url_mango_smoothie', N'Sinh tố xoài tươi ngon', '2025-01-12 09:00:00', 1, 1, 0),
(3, N'Nước ép cam', 45000, 'image_url_orange_juice', N'Nước cam tươi nguyên chất', '2025-01-13 11:30:00', 1, 1, 0),
(3, N'Matcha latte', 48000, 'image_url_matcha_latte', N'Matcha latte thơm ngon', '2025-02-14 14:00:00', 1, 1, 0),
(3, N'Mocha', 50000, 'image_url_mocha', N'Cà phê mocha kết hợp chocolate', '2025-02-15 16:30:00', 1, 1, 0),

-- Trà sữa
(4, N'Trà sữa truyền thống', 30000, 'image_url_boba', N'Trà sữa đậm đà kết hợp trân châu đen dai ngon', '2025-02-16 08:00:00', 1, 1, 0),
(4, N'Trà sữa thái xanh', 30000, 'image_url_fruit_jelly', N'Hương vị trà thái xanh thơm mát với lớp thạch trái cây mềm mịn', '2025-01-17 09:00:00', 1, 1, 0),
(4, N'Trà sữa thái đỏ', 30000, 'image_url_cream', N'Trà thái đỏ độc đáo, kết hợp với kem sữa béo ngậy', '2025-01-18 11:15:00', 1, 1, 0),
(4, N'Trà sữa yến', 60000, 'image_url_chia_seeds', N'Trà sữa béo ngậy hòa quyện với yến mạch bổ dưỡng, mang lại hương vị thanh nhẹ', '2025-01-19 13:45:00', 1, 1, 0),
(4, N'Trà sữa yến mạch', 55000, 'image_url_white_boba', N'Trái nghiệm sự kết hợp hoàn hảo giữa trà sữa đậm đà và yến mạch giòn mềm tự nhiên', '2025-01-20 15:10:00', 1, 1, 0),

-- Topping
(5, N'Trân châu', 5000, 'image_url_boba', N'Trân châu đen dai ngon', '2025-01-16 10:00:00', 1, 1, 0),
(5, N'Thạch trái cây', 7000, 'image_url_fruit_jelly', N'Thạch trái cây ngọt ngào', '2025-01-17 11:15:00', 1, 1, 0),
(5, N'Kem sữa', 8000, 'image_url_cream', N'Lớp kem sữa béo ngậy', '2025-01-18 13:00:00', 1, 1, 0),
(5, N'Hạt chia', 6000, 'image_url_chia_seeds', N'Hạt chia bổ dưỡng', '2025-01-19 14:45:00', 1, 1, 0),
(5, N'Trân châu trắng', 5500, 'image_url_white_boba', N'Trân châu trắng giòn', '2025-01-20 15:30:00', 1, 1, 0);

INSERT INTO orders (userId, totalPrice, note, orderDate, deliveryAddress, deliveryPhone, status)
VALUES
(1, 175000, N'Khách hàng yêu cầu giao hàng nhanh', '2025-02-20 10:00:00', N'123 Đường A, Quận B, TP. HCM', '0909123456', N'Processing'),
(1, 225000, N'Không có yêu cầu đặc biệt', '2025-02-21 14:30:00', N'456 Đường C, Quận D, TP. HCM', '0909876543', N'Completed'),
(1, 95000, N'Yêu cầu giao buổi sáng', '2025-02-22 08:15:00', N'789 Đường E, Quận F, TP. HCM', '0909345678', N'Processing'),
(1, 300000, N'Khách hàng yêu cầu giao vào buổi chiều', '2025-02-23 16:45:00', N'111 Đường G, Quận H, TP. HCM', '0909567890', N'Completed'),
(1, 145000, N'Không có yêu cầu đặc biệt', '2025-02-24 12:00:00', N'222 Đường I, Quận J, TP. HCM', '0909765432', N'Cancelled');

INSERT INTO order_details (orderId, productId, quantity, price)
VALUES
-- Chi tiết cho order 1
(1, 1, 2, 25000),
(1, 6, 1, 25000),
(1, 10, 1, 40000),
-- Chi tiết cho order 2
(2, 2, 3, 30000),
(2, 7, 1, 30000),
-- Chi tiết cho order 3
(3, 11, 2, 50000),
(3, 16, 1, 30000),
-- Chi tiết cho order 4
(4, 3, 4, 27000),
(4, 12, 2, 45000),
-- Chi tiết cho order 5
(5, 4, 2, 35000),
(5, 18, 3, 55000);

INSERT INTO products (categoryId, name, price, image, description, createDate, status, isActive, isDelete)
VALUES
-- Sản phẩm mới trong vòng 7 ngày
(2, N'Trà táo quế', 35000, 'image_url_apple_cinnamon_tea', N'Trà táo quế thơm ngon ấm áp', '2025-02-20 09:00:00', 1, 1, 0),
(3, N'Chocolate đá xay', 55000, 'image_url_choco_frappe', N'Thức uống chocolate mát lạnh', '2025-02-21 14:30:00', 1, 1, 0),
(4, N'Trà sữa hạnh nhân', 40000, 'image_url_almond_boba', N'Trà sữa với hương vị hạnh nhân thơm ngọt', '2025-02-22 10:15:00', 1, 1, 0),
(5, N'Trân châu đường đen', 8000, 'image_url_brown_sugar_boba', N'Trân châu đường đen thơm lừng', '2025-02-23 15:45:00', 1, 1, 0),
(3, N'Latte bí ngô', 45000, 'image_url_pumpkin_spice_latte', N'Hương vị latte bí ngô đặc biệt', '2025-02-24 17:00:00', 1, 1, 0);

