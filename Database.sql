USE LTDD_TheLa

INSERT INTO categories (name, image, is_active, is_delete)
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

INSERT INTO products (category_id, name, description, create_date, status, is_active, is_delete)
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

INSERT INTO product_images (product_id, image, is_main)
VALUES
(1, 'img_prod_coffee_black_1', 1),
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

INSERT INTO product_sizes (product_id, size_id, price)
VALUES
-- Cà phê đen
(1, 1, 15000), -- Size S
(1, 2, 20000), -- Size M
(1, 3, 25000), -- Size L
-- Cà phê sữa
(2, 1, 20000), -- Size S
(2, 2, 25000), -- Size M
(2, 3, 30000), -- Size L
-- Trà đào
(3, 1, 25000), -- Size S
(3, 2, 30000), -- Size M
(3, 3, 35000), -- Size L
-- Trà lài
(4, 1, 20000), -- Size S
(4, 2, 25000), -- Size M
(4, 3, 30000), -- Size L
-- Sinh tố bơ
(5, 1, 40000), -- Size S
(5, 2, 45000), -- Size M
(5, 3, 50000), -- Size L
-- Sinh tố dâu
(6, 1, 35000), -- Size S
(6, 2, 40000), -- Size M
(6, 3, 45000), -- Size L
-- Trà sữa truyền thống
(7, 1, 30000), -- Size S
(7, 2, 35000), -- Size M
(7, 3, 40000), -- Size L
-- Trà sữa matcha
(8, 1, 35000), -- Size S
(8, 2, 40000), -- Size M
(8, 3, 45000), -- Size L
-- Trân châu đen
(9, 4, 10000), -- Tiêu chuẩn
-- Trân châu trắng
(10, 4, 12000); -- Tiêu chuẩn

INSERT INTO promotions (name, image, description, discount_percentage, start_date, end_date, quantity, quantity_used, is_active, is_delete, minimum_order_value)
VALUES 
-- Chương trình giảm giá
(N'Khuyến mãi mùa hè', 'img_pro_summer', N'Giảm giá 20% tất cả các sản phẩm trong tháng hè', 20.00, '2025-04-01 00:00:00', '2025-04-30 00:00:00', 100, 0, 1, 0, 0.0), 

-- Chương trình mua 1 tặng 1
(N'Mua sắm ưu đãi', 'img_uu_dai', N'Mua càng nhiều khuyến mãi các lớn', 25.00, '2025-04-01 00:00:00', '2025-04-15 00:00:00', 50, 0, 1, 0, 0.0);

INSERT INTO orders (user_id, total_price, note, create_date, delivery_address, delivery_phone, status)
VALUES
(1, 75000, N'Giao trước 10h sáng', '2025-04-01 08:30:00', N'123 Đường Nguyễn Trãi, Quận 1, TP.HCM', '0909123456', 'Delivered'),
(1, 95000, N'Không đá, ít đường', '2025-04-03 14:00:00', N'456 Đường Lê Văn Sỹ, Quận 3, TP.HCM', '0909876543', 'Processing');

INSERT INTO order_details (order_id, product_size_id, quantity, price)
VALUES
-- orderId = 1
(1, 3, 1, 25000), 
(1, 5, 2, 25000), 

-- orderId = 2
(2, 1, 1, 15000), 
(2, 2, 1, 20000), 
(2, 6, 2, 30000);

INSERT INTO reviews (product_id, user_id, rating, content, review_date, order_detail_id)
VALUES 
(1, 1, 5, N'Cà phê đen thơm ngon, đậm vị, rất đáng để thưởng thức.', '2025-04-05 10:30:00', 1),
(2, 1, 4, N'Hương vị tốt nhưng gói hàng bị trễ một chút.', '2025-04-05 10:30:00', 2),
(1, 1, 5, N'Cà phê ngon.', '2025-04-04 08:15:00', 3),
(1, 1, 5, N'Cà phê ngon.', '2025-04-04 08:15:00', 4),
(2, 1, 3, N'Hương vị ổn nhưng không quá đặc biệt, phù hợp để uống hàng ngày.', '2025-04-04 08:15:00', 5);

INSERT INTO review_images (review_id, image) 
VALUES 
    (1, 'img_review_cafe_den_1'),
	(1, 'img_review_cafe_den_2'),
    (2, 'img_review_cafe_sua_1'),
    (3, 'img_review_cafe_den_3'),
    (4, 'img_review_cafe_den_4'),
	(5, 'img_review_cafe_sua_2'),
    (5, 'img_review_cafe_sua_3');

