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
    isDelete BIT DEFAULT 0,
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
    image VARCHAR(500),
	description NVARCHAR(500),
	createDate Date,
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

