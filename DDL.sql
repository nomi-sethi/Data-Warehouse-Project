DROP DATABASE IF EXISTS metro;
CREATE DATABASE metro;
DROP TABLE IF EXISTS metro.customer_dim;
CREATE TABLE metro.customer_dim(
	customer_id varchar(4) PRIMARY KEY,
    customer_name varchar(30) NOT NULL
);

DROP TABLE IF EXISTS metro.store_dim;
CREATE TABLE metro.store_dim(
	store_id varchar(4) PRIMARY KEY,
    store_name varchar(20) NOT NULL
);

DROP TABLE IF EXISTS metro.supplier_dim;
CREATE TABLE metro.supplier_dim(
	supplier_id varchar(5) PRIMARY KEY,
    supplier_name varchar(30) NOT NULL
);

DROP TABLE IF EXISTS metro.product_dim;
CREATE TABLE metro.product_dim(
	product_id varchar(6) PRIMARY KEY,
    product_name varchar(30) NOT NULL,
    price decimal(5,2) not null
);

DROP TABLE IF EXISTS metro.date_dim;
CREATE TABLE metro.date_dim(
    t_date date Primary Key,
    t_year varchar(4) NOT NULL,
    t_quarter varchar(2) NOT NULL,
    t_month varchar(12) NOT NULL,
    t_dayname varchar(10) NOT NULL,
    t_day varchar (2) NOT NULL
);

DROP TABLE IF EXISTS metro.sales_fact;
CREATE TABLE metro.sales_fact(
	customer_id varchar(4) NOT NULL,
    store_id varchar(4) NOT NULL,
    supplier_id varchar(5) NOT NULL,
    product_id varchar(6) NOT NULL,
    t_date date NOT NULL,
    quantity smallint NOT NULL,
    sales decimal(10,2) NOT NULL,
    PRIMARY KEY(customer_id, store_id, supplier_id, product_id, t_date),
    FOREIGN KEY (customer_id) REFERENCES metro.customer_dim(customer_id),
    FOREIGN KEY (store_id) REFERENCES metro.store_dim(store_id),
    FOREIGN KEY (supplier_id) REFERENCES metro.supplier_dim(supplier_id),
    FOREIGN KEY (product_id) REFERENCES metro.product_dim(product_id),
    FOREIGN KEY (t_date) REFERENCES metro.date_dim(t_date)
);

DROP TABLE IF EXISTS metro.storeanalysis_mv;
CREATE TABLE metro.storeanalysis_mv(
	store_id varchar(4) NOT NULL,
    product_id varchar(6) NOT NULL,
    store_total decimal(15,2),
    PRIMARY KEY (store_id, product_id)
);