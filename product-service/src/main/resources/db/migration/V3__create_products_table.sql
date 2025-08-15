CREATE TABLE products (
    product_id SERIAL PRIMARY KEY,
    category_id INT,
    product_title VARCHAR(255),
    image_url VARCHAR(255),
    sku VARCHAR(255),
    price_unit NUMERIC,
    quantity INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);