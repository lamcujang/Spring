CREATE TABLE categories (
	category_id SERIAL PRIMARY KEY,
	parent_category_id INT,
	category_title VARCHAR(255),
	image_url VARCHAR(255),
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);