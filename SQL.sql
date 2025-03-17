CREATE DATABASE DBTheLa;
GO
USE DBTheLa;
GO
CREATE TABLE users (
    userId BIGINT PRIMARY KEY IDENTITY,
    name NVARCHAR(100),
    email VARCHAR(320) NOT NULL,
    password VARCHAR(128) NOT NULL,
    code VARCHAR(50),
    address NVARCHAR(500),
    phone VARCHAR(50),
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
	description NVARCHAR(500),
	createDate DateTime,
	status BIT DEFAULT 1,
    isActive BIT DEFAULT 1,
    isDelete BIT DEFAULT 0,
    FOREIGN KEY (categoryId) REFERENCES categories(categoryId)
);
GO

CREATE TABLE productImages (
    imageId BIGINT IDENTITY(1,1) PRIMARY KEY,
    productId BIGINT NOT NULL,
    image NVARCHAR(255) NOT NULL,
    isMain BIT NOT NULL DEFAULT 0, -- 1: Ảnh chính, 0: Ảnh phụ
    FOREIGN KEY (productId) REFERENCES products(productId)
);

CREATE TABLE sizes (
    sizeId BIGINT IDENTITY(1,1) PRIMARY KEY,
    name NVARCHAR(50) NOT NULL,
	description NVARCHAR(100)
);

CREATE TABLE productSizes (
	productSizeId BIGINT IDENTITY(1,1) PRIMARY KEY,
    sizeId BIGINT NOT NULL,
    productId BIGINT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (productId) REFERENCES products(productId),
	FOREIGN KEY (sizeId) REFERENCES sizes(sizeId)
);

CREATE TABLE orders (
    orderId BIGINT PRIMARY KEY IDENTITY,
	userId BIGINT NOT NULL,
	totalPrice DECIMAL(10, 2) NOT NULL,
	note NVARCHAR(500),
	createDate DateTime,
	deliveryAddress NVARCHAR(255) NOT NULL, 
    deliveryPhone NVARCHAR(15) NOT NULL,
	status NVARCHAR(50) DEFAULT 'Processing',
    FOREIGN KEY (userId) REFERENCES users(userId)
);
GO

CREATE TABLE order_details (
    orderDetailId BIGINT PRIMARY KEY IDENTITY, 
    orderId BIGINT NOT NULL, 
    productSizeId BIGINT NOT NULL, 
    quantity INT NOT NULL, 
    price DECIMAL(10, 2) NOT NULL, 
    FOREIGN KEY (orderId) REFERENCES orders(orderId), 
    FOREIGN KEY (productSizeId) REFERENCES productSizes(productSizeId)
);
GO


INSERT INTO categories (name, image, isActive, isDelete)
VALUES
(N'Cà phê', 'img_cate_coffee', 1, 0),
(N'Trà', 'img_cate_tea', 1, 0),
(N'Thức uống đặc biệt', 'img_cate_special_drink', 1, 0),
(N'Trà sữa', 'img_cate_milk_tea', 1, 0),
(N'Topping', 'img_cate_topping', 1, 0);

INSERT INTO sizes (name, description) VALUES 
('S', '500ml'),
('M', '700ml'),
('L', '1000ml'),
(N'Tiêu chuẩn', '');

INSERT INTO products (categoryId, name, description, createDate, status, isActive, isDelete)
VALUES
(1, N'Cà phê đen', N'Cà phê nguyên chất đậm đà, không đường', DATEADD(DAY, -8, GETDATE()), 1, 1, 0),
(1, N'Cà phê sữa', N'Cà phê pha cùng sữa đặc, vị ngọt ngào', DATEADD(DAY, -3, GETDATE()), 1, 1, 0),
(2, N'Trà đào', N'Trái đào tươi ngon kết hợp cùng trà thanh mát', DATEADD(DAY, -2, GETDATE()), 1, 1, 0),
(2, N'Trà lài', N'Trá hoa lài thanh khiết, giúp thư giãn', DATEADD(DAY, -6, GETDATE()), 1, 1, 0),
(3, N'Sinh tố bơ', N'Sinh tố bơ tươi, nguyên chất, giàu dinh dưỡng', DATEADD(DAY, -4, GETDATE()), 1, 1, 0),
(3, N'Sinh tố dâu', N'Sinh tố dâu ngọt ngào từ những trái dâu chín mọng', DATEADD(DAY, -5, GETDATE()), 1, 1, 0),
(4, N'Trà sữa truyền thống', N'Trà sữa đậm vị truyền thống, phù hợp với mọi khẩu vị', DATEADD(DAY, -1, GETDATE()), 1, 1, 0),
(4, N'Trà sữa matcha', N'Hương vị matcha Nhật Bản thơm ngon và bổ dưỡng', DATEADD(DAY, -2, GETDATE()), 1, 1, 0),
(5, N'Trân châu đen', N'Trân châu đen dẻo dai, ngọt nhẹ', DATEADD(DAY, -10, GETDATE()), 1, 1, 0),
(5, N'Trân châu trắng', N'Trân châu trắng giòn sật, thơm ngon', DATEADD(DAY, -4, GETDATE()), 1, 1, 0);

INSERT INTO productImages (productId, image, isMain)
VALUES
(1, 'img_prod_coffee_black', 1),
(2, 'img_prod_coffee_milk', 1),
(3, 'img_prod_tea_peach', 1),
(4, 'img_prod_tea_jasmine', 1),
(5, 'img_prod_smoothie_avocado', 1),
(6, 'img_prod_smoothie_strawberry', 1),
(7, 'img_prod_milk_tea_original', 1),
(8, 'img_prod_milk_tea_matcha', 1),
(9, 'img_prod_topping_black_pearl', 1),
(10, 'img_prod_topping_white_pearl', 1);
