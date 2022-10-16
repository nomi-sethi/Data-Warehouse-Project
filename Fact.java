package metro;

import java.sql.Date;

public class Fact {
	String product_id;
	String customer_id;
	String customer_name;
	String store_id;
	String store_name;
	Date t_date;
	String product_name;
	String supplier_id;
	String supplier_name;
	double price;
	int quantity;
	double sales;
	
	Fact(){
		product_id = "";
		product_name = "";
		customer_id = "";
		customer_name = "";
		store_id = "";
		store_name = "";
		supplier_id = "";
		supplier_name = "";
		t_date = null;
		price = 0;
		quantity = 0;
		sales = 0;
	}
	void fill(String pid, String pn, String cid, String cn, String stid, String stn, String spid, String spn, Date date, 
				double p, int q) {
		this.product_id = pid;
		this.product_name = pn;
		this.customer_id = cid;
		this.customer_name = cn;
		this.store_id = stid;
		this.store_name = stn;
		this.supplier_id = spid;
		this.supplier_name = spn;
		this.t_date = date;
		this.price = p;
		this.quantity = q;
		this.sales = this.price * this.quantity;
	}
	void calculateSale() {
		this.sales = this.quantity * this.price;
	}
}
