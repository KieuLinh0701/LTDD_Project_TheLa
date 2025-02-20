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

CREATE TABLE sizes (
    sizeId BIGINT PRIMARY KEY IDENTITY, 
    name NVARCHAR(20) NOT NULL, 
	description NVARCHAR(500) 
);

CREATE TABLE products_sizes (
    sizeId BIGINT NOT NULL,
    productId BIGINT NOT NULL,  
    price INT NOT NULL, 
	status BIT DEFAULT 1,
	isDelete BIT DEFAULT 0,
	PRIMARY KEY (sizeId, productId),
	FOREIGN KEY (sizeId) REFERENCES sizes(sizeId),
    FOREIGN KEY (productId) REFERENCES products(productId) 
);

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
    sizeId BIGINT NOT NULL, 
    quantity INT NOT NULL, 
    price INT NOT NULL, 
    FOREIGN KEY (orderId) REFERENCES orders(orderId), 
    FOREIGN KEY (productId) REFERENCES products(productId), 
    FOREIGN KEY (sizeId) REFERENCES sizes(sizeId) 
);
GO

INSERT INTO categories (name, image, isActive, isDelete)
VALUES
('Cà phê', 'image_url_coffee', 1, 0),
('Trà', 'image_url_tea', 1, 0),
('Thức uống đặc biệt', 'image_url_special', 1, 0),
('Trà sữa', 'image_url_tea', 1, 0),
('Topping', 'image_url_tea', 1, 0);

INSERT INTO products (categoryId, name, image, description, createDate, status, isActive, isDelete)
VALUES
-- Trà
(2, 'Trà xanh', 'image_url_green_tea', 'Trà xanh tươi mát', '2025-01-06 08:30:00', 1, 1, 0),
(2, 'Trà đào', 'image_url_peach_tea', 'Trà đào thơm ngọt', '2025-01-07 09:15:00', 1, 1, 0),
(2, 'Trà sữa', 'image_url_milk_tea', 'Trà sữa đậm đà', '2025-01-08 10:45:00', 1, 1, 0),
(2, 'Trà lài', 'image_url_jasmine_tea', 'Trà hoa lài thơm dịu', '2025-01-09 11:30:00', 1, 1, 0),
(2, 'Trà matcha', 'image_url_matcha_tea', 'Trà xanh matcha Nhật Bản', '2025-01-10 14:20:00', 1, 1, 0),

-- Trà sữa
(4, 'Trà sữa truyền thống', 'image_url_boba', 'Trà sữa đậm đà kết hợp trân châu đen dai ngon', '2025-02-16 08:00:00', 1, 1, 0),
(4, 'Trà sữa thái xanh', 'image_url_fruit_jelly', 'Hương vị trà thái xanh thơm mát với lớp thạch trái cây mềm mịn', '2025-01-17 09:00:00', 1, 1, 0),
(4, 'Trà sữa thái đỏ', 'image_url_cream', 'Trà thái đỏ độc đáo, kết hợp với kem sữa béo ngậy', '2025-01-18 11:15:00', 1, 1, 0),
(4, 'Trà sữa khoai môn', 'image_url_chia_seeds', 'Vị khoai môn ngọt bùi hòa quyện cùng trà sữa mịn màng', '2025-01-19 13:45:00', 1, 1, 0),
(4, 'Sữa tươi trân châu đường đen', 'image_url_white_boba', 'Sữa tươi thơm mát kết hợp trân châu đường đen ngọt lịm', '2025-01-20 15:10:00', 1, 1, 0);

INSERT INTO products (categoryId, name, price, image, description, createDate, status, isActive, isDelete)
VALUES
-- Cà phê
(1, 'Cà phê đen', 25000, 'image_url_black_coffee', 'Cà phê đen truyền thống Việt Nam', '2025-01-01 07:00:00', 1, 1, 0),
(1, 'Cà phê sữa', 30000, 'image_url_milk_coffee', 'Cà phê sữa đặc ngon ngọt', '2025-02-19 09:30:00', 1, 1, 0),
(1, 'Cà phê đá', 27000, 'image_url_iced_coffee', 'Cà phê đen đá mát lạnh', '2025-01-03 08:15:00', 1, 1, 0),
(1, 'Espresso', 35000, 'image_url_espresso', 'Espresso đậm đà', '2025-01-04 10:00:00', 1, 1, 0),
(1, 'Cappuccino', 45000, 'image_url_cappuccino', 'Cappuccino pha kiểu Ý', '2025-02-15 15:45:00', 1, 1, 0),

-- Thức uống đặc biệt
(3, 'Cacao nóng', 40000, 'image_url_hot_cacao', 'Thức uống cacao ấm áp', '2025-01-11 16:00:00', 1, 1, 0),
(3, 'Sinh tố xoài', 50000, 'image_url_mango_smoothie', 'Sinh tố xoài tươi ngon', '2025-01-12 09:00:00', 1, 1, 0),
(3, 'Nước ép cam', 45000, 'image_url_orange_juice', 'Nước cam tươi nguyên chất', '2025-01-13 11:30:00', 1, 1, 0),
(3, 'Matcha latte', 48000, 'image_url_matcha_latte', 'Matcha latte thơm ngon', '2025-02-14 14:00:00', 1, 1, 0),
(3, 'Mocha', 50000, 'image_url_mocha', 'Cà phê mocha kết hợp chocolate', '2025-02-15 16:30:00', 1, 1, 0),

-- Topping
(5, 'Trân châu', 5000, 'image_url_boba', 'Trân châu đen dai ngon', '2025-01-16 10:00:00', 1, 1, 0),
(5, 'Thạch trái cây', 7000, 'image_url_fruit_jelly', 'Thạch trái cây ngọt ngào', '2025-01-17 11:15:00', 1, 1, 0),
(5, 'Kem sữa', 8000, 'image_url_cream', 'Lớp kem sữa béo ngậy', '2025-01-18 13:00:00', 1, 1, 0),
(5, 'Hạt chia', 6000, 'image_url_chia_seeds', 'Hạt chia bổ dưỡng', '2025-01-19 14:45:00', 1, 1, 0),
(5, 'Trân châu trắng', 5500, 'image_url_white_boba', 'Trân châu trắng giòn', '2025-01-20 15:30:00', 1, 1, 0);

INSERT INTO sizes (name, description)
VALUES
('S', 'Kích thước nhỏ (500ml)'),
('M', 'Kích thước vừa (700ml)'),
('L', 'Kích thước lớn (1000ml)');

INSERT INTO products_sizes (sizeId, productId, price, status, isDelete)
VALUES
-- Trà xanh
(1, 1, 20000, 1, 0), -- Size nhỏ
(2, 1, 25000, 1, 0), -- Size vừa
(3, 1, 30000, 1, 0), -- Size lớn

-- Trà đào
(1, 2, 22000, 1, 0), -- Size nhỏ
(2, 2, 27000, 1, 0), -- Size vừa
(3, 2, 32000, 1, 0), -- Size lớn

-- Trà sữa
(1, 3, 25000, 1, 0), -- Size nhỏ
(2, 3, 30000, 1, 0), -- Size vừa
(3, 3, 35000, 1, 0), -- Size lớn

-- Trà lài
(1, 4, 22000, 1, 0), -- Size nhỏ
(2, 4, 27000, 1, 0), -- Size vừa
(3, 4, 32000, 1, 0), -- Size lớn

-- Trà matcha
(1, 5, 30000, 1, 0), -- Size nhỏ
(2, 5, 35000, 1, 0), -- Size vừa
(3, 5, 40000, 1, 0), -- Size lớn

-- Trà sữa truyền thống
(1, 6, 30000, 1, 0), -- Size nhỏ
(2, 6, 35000, 1, 0), -- Size vừa
(3, 6, 40000, 1, 0), -- Size lớn

-- Trà sữa thái xanh
(1, 7, 32000, 1, 0), -- Size nhỏ
(2, 7, 37000, 1, 0), -- Size vừa
(3, 7, 42000, 1, 0), -- Size lớn

-- Trà sữa thái đỏ
(1, 8, 32000, 1, 0), -- Size nhỏ
(2, 8, 37000, 1, 0), -- Size vừa
(3, 8, 42000, 1, 0), -- Size lớn

-- Trà sữa khoai môn
(1, 9, 35000, 1, 0), -- Size nhỏ
(2, 9, 40000, 1, 0), -- Size vừa
(3, 9, 45000, 1, 0), -- Size lớn

-- Sữa tươi trân châu đường đen
(1, 10, 40000, 1, 0), -- Size nhỏ
(2, 10, 45000, 1, 0), -- Size vừa
(3, 10, 50000, 1, 0); -- Size lớn

USE DBTheLa;
GO
INSERT INTO users (name, email, password, code, address, phone, role, isActivate)
VALUES
('Nguyen Van A', 'nguyenvana@example.com', 'password123', 'A001', '123 ABC Street', '0901234567', 'Customer', 1),
('Tran Thi B', 'tranthib@example.com', 'password456', 'B002', '456 DEF Street', '0902345678', 'Customer', 1),
('Le Van C', 'levanc@example.com', 'password789', 'C003', '789 GHI Street', '0903456789', 'Customer', 1);

INSERT INTO orders (userId, totalPrice, note, orderDate, deliveryAddress, deliveryPhone, status)
VALUES
(1, 120000, 'Không cho đá', '2025-02-19 10:30:00', '123 ABC Street', '0902345678', 'Completed'),
(11, 75000, 'Giao hàng sau 3 giờ chiều', '2025-02-19 14:00:00', '456 DEF Street', '0903456789', 'Completed'),
(27, 155000, NULL, '2025-02-19 16:30:00', '123 ABC Street', '0902345678', 'Completed');

INSERT INTO order_details (orderId, productId, sizeId, quantity, price)
VALUES
-- Order 1
(1, 6, 2, 2, 35000), -- Trà sữa truyền thống, Size vừa, 2 ly
(1, 3, 1, 1, 25000), -- Trà sữa, Size nhỏ, 1 ly

-- Order 2
(2, 9, 3, 1, 45000), -- Trà sữa khoai môn, Size lớn, 1 ly
(2, 1, 2, 1, 25000), -- Trà xanh, Size vừa, 1 ly

-- Order 3
(3, 10, 3, 2, 50000), -- Sữa tươi trân châu đường đen, Size lớn, 2 ly
(3, 4, 1, 1, 22000);  -- Trà lài, Size nhỏ, 1 ly


SELECT * FROM categories WHERE isActive = 1 AND isDelete = 0