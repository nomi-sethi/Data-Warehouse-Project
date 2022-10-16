package metro;

public class Mdata {
	String product_id;
	String product_name;
	String supplier_id;
	String supplier_name;
	double price;
	
	Mdata(){
		product_id = "";
		product_name = "";
		supplier_id = "";
		supplier_name = "";
		price = 0;
	}
	
void fill(String p_id, String p_name, String s_id, String s_name, double p) {
	this.product_id = p_id;
	this.product_name = p_name;
	this.supplier_id = s_id;
	this.supplier_name = s_name;
	this.price = p;
	}
}
