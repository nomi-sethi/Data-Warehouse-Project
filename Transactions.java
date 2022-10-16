package metro;
import java.sql.Date;

public class Transactions {
	int transaction_id;
	String product_id;
	String customer_id;
	String customer_name;
	String store_id;
	String store_name;
	Date t_date;
	int quantity;
	
	Transactions(){
		transaction_id = 0;
		product_id = "";
		customer_id = "";
		customer_name = "";
		store_id = "";
		store_name = "";
		t_date = null;
		quantity = 0;
	}
	
void fill( int t_id, String p_id, String c_id, String c_name, String s_id, String s_name, Date date, int q) {
	this.transaction_id = t_id;
	this.product_id = p_id;
	this.customer_id = c_id;
	this.customer_name = c_name;
	this.store_id = s_id;
	this.store_name = s_name;
	this.t_date = date;
	this.quantity = q;
	}

}
