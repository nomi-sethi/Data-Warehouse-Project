#### Query1 ####
# Quarter Wise
select s.supplier_name as Supplier, sum(sf.sales) as Total_Sales, d.t_quarter as Quarter 
from metro.sales_fact sf join metro.supplier_dim s join metro.date_dim d
on (s.supplier_id = sf.supplier_id AND d.t_date = sf.t_date)
group by Supplier, Quarter
order by Supplier, Quarter;

# Month Wise
select s.supplier_name as Supplier, sum(sf.sales) as Total_Sales, d.t_month as Month
from metro.sales_fact sf join metro.supplier_dim s join metro.date_dim d
on (s.supplier_id = sf.supplier_id AND d.t_date = sf.t_date)
group by Supplier, Month
order by Supplier, Month;

# Both Quarter and Month Wise
select s.supplier_name as Supplier, sum(sf.sales) as Total_Sales,d.t_quarter as Quarter, d.t_month as Month
from metro.sales_fact sf join metro.supplier_dim s join metro.date_dim d
on (s.supplier_id = sf.supplier_id AND d.t_date = sf.t_date)
group by Supplier, Month, Quarter
order by supplier, Quarter;

#### Query 2 ####
select * from metro.storeanalysis_mv;

#### Query 3 ####
select p.product_name as product, count(p.product_name) as units_sold 
		from metro.product_dim p join metro.sales_fact sf join metro.date_dim d
		on(p.product_id = sf.product_id AND d.t_date = sf.t_date)
		where (d.t_dayname = "Saturday" OR d.t_dayname = "Sunday") 
		group by p.product_name
        order by units_sold DESC Limit 5;

#### Query 4 ####
select p.product_name, sf.sales, d.t_quarter 
	from metro.product_dim p join metro.sales_fact sf join metro.date_dim d
    on (p.product_id = sf.product_id AND d.t_date = sf.t_date)
    group by p.product_name
    order by d.t_quarter;

#### Query 5 ####
select p.product_name, h1.first_half_sales, h2.second_half_sales, y.year_sales from
	(select p.product_name product, sum(sf.sales) first_half_sales from 
		metro.sales_fact sf join metro.product_dim p join metro.date_dim d
		on (p.product_id = sf.product_id AND d.t_date = sf.t_date)
		where d.t_quarter = "1" OR d.t_quarter = "2"
		group by p.product_name
		order by p.product_name) h1 join
	(select p.product_name product, sum(sf.sales) second_half_sales from 
		metro.sales_fact sf join metro.product_dim p join metro.date_dim d
		on (p.product_id = sf.product_id AND d.t_date = sf.t_date)
		where d.t_quarter = "3" OR d.t_quarter = "4"
		group by p.product_name
		order by p.product_name) h2 join
	(select p.product_name product, sum(sf.sales) year_sales from 
		metro.sales_fact sf join metro.product_dim p join metro.date_dim d
		on (p.product_id = sf.product_id AND d.t_date = sf.t_date)
		group by p.product_name
		order by p.product_name) y join
	(select product_name from metro.product_dim) p
    on (p.product_name = h1.product AND p.product_name = h2.product AND p.product_name = y.product)
	group by p.product_name
    order by p.product_name;

#### Query 6 ####
select count(*) from metro.sales_fact;
# Produced results show that the total number of rows in sales fact table are less than total transactions; 
# It might be due to repetition

#### Query 7 ####
#	Created in DDL.sql file and Popullated in MeshJoin.java file
#	same materialized view table was used in Query#2 (Q2), written above,
#	to retrieve data

### Query Code
# Creation
DROP TABLE IF EXISTS metro.storeanalysis_mv;
CREATE TABLE metro.storeanalysis_mv(
	store_id varchar(4) NOT NULL,
    product_id varchar(6) NOT NULL,
    store_total decimal(15,2),
    PRIMARY KEY (store_id, product_id)
);
# Popullation
insert ignore into metro.storeanalysis_mv (store_id, product_id, store_total)
	(select s.store_id, p.product_id, sum(sf.sales)
    from metro.store_dim s join metro.product_dim p join metro.sales_fact sf
    on (s.store_id = sf.store_id AND p.product_id = sf.product_id)
    group by s.store_id, p.product_id);
    