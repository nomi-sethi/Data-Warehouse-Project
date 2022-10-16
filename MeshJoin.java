package metro;
import java.sql.*;
import java.util.*;

public class MeshJoin {
	public static void main(String[] args) throws Exception {
		Scanner s = new Scanner(System.in);
		System.out.println("Enter the username and the password for MySql local host server (root)\n");
		
		System.out.println("Username: ");
		String user = s.nextLine();
		
		System.out.println("Password: ");
		String pass = s.nextLine();
		
		Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/",user,pass);
		Statement st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Statement st2 = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Statement st3 = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		ResultSet rs;
		
		Queue <Integer> q = new LinkedList<>();
		Hashtable<Integer, Fact> hmap=new Hashtable<Integer, Fact>(50);
		int transOffset = 0;
		int MdataOffset = 0;
		Transactions t = new Transactions();
		Mdata m = new Mdata();
		
		for (transOffset = 0; transOffset < 10000; transOffset += 50) {
			rs = st.executeQuery("select * from project.transactions LIMIT "+transOffset+" ,50;");
			while (rs.next()) {
				t.transaction_id = rs.getInt("Transaction_id");
				//System.out.println(t.transaction_id);
				q.add(t.transaction_id);
			}
			for(rs.first();;rs.next()) {
				t.fill(rs.getInt("transaction_id"), 
						rs.getString("product_id"), 
						rs.getString("customer_id"),
						rs.getString("customer_name"), 
						rs.getString("store_id"), 
						rs.getString("store_name"), 
						rs.getDate("t_date"),
						rs.getInt("quantity"));
				boolean found = false;
				for (MdataOffset=0; MdataOffset < 100; MdataOffset += 25) {
					ResultSet rs2 = st2.executeQuery("select * from project.masterdata LIMIT "+MdataOffset+" , 25;");
					while (rs2.next()) {
						m.fill(rs2.getString("product_id"), 
								rs2.getString("product_name"), 
								rs2.getString("supplier_id"), 
								rs2.getString("supplier_name"), 
								rs2.getDouble("price"));
						//System.out.println(t.transaction_id +" "+ t.product_id +" "+ m.product_id);
						if(t.product_id.equals(m.product_id)) {
							found = true;
							Fact f = new Fact();
							f.fill(t.product_id,
									m.product_name,
									t.customer_id, 
									t.customer_name, 
									t.store_id, 
									t.store_name, 
									m.supplier_id, 
									m.supplier_name, 
									t.t_date, 
									m.price,
									t.quantity);
							hmap.put(t.transaction_id%50, f);
							//System.out.println(hmap.keySet());
							//System.out.println(q.peek());
							q.remove();
							break;
						}
					}
					if(found) break;
				}
				if(rs.isLast()) break;
			}
			//creating enumeration
			Enumeration <Fact> e = hmap.elements();
			while (e.hasMoreElements()) { 
				Fact f = e.nextElement();
				
				////	Loading Customer dimension table	////
				String Query = "INSERT IGNORE into metro.customer_dim values(\""+f.customer_id+
								"\", \""+f.customer_name+"\");" ;
				int r = st3.executeUpdate(Query);
				//System.out.println(r);
				
				////	Loading Supplier dimension table	////
				Query = "INSERT IGNORE into metro.supplier_dim values(\""+f.supplier_id+
						"\", \""+f.supplier_name+"\");" ;
				r = st3.executeUpdate(Query);
				//System.out.println(r);
				
				////	Loading Store dimension table	////			
				Query = "INSERT IGNORE into metro.store_dim values(\""+f.store_id+
						"\", \""+f.store_name+"\");" ;
				r = st3.executeUpdate(Query);
				//System.out.println(r);
				
				////	Loading Product dimension table	////
				Query = "INSERT IGNORE into metro.product_dim values(\""+f.product_id+
						"\", \""+f.product_name+"\", \""+f.price+"\");" ;
				r = st3.executeUpdate(Query);
				//System.out.println(r);
				
				////	Loading Date dimension table	////				
				Query = "INSERT IGNORE into metro.date_dim values("
						+"\""+ f.t_date + "\", "
						+ "year(\"" +f.t_date+ "\"), "
						+ "quarter(\"" +f.t_date+ "\"), "
						+ "monthname(\"" +f.t_date+ "\"), "
						+ "dayname(\"" +f.t_date+ "\"), "
						+ "day(\"" +f.t_date+ "\") "
						+ ");" ;
				r = st3.executeUpdate(Query);
				//System.out.println(r);
				
				////	Loading Fact table	////
				Query = "INSERT IGNORE into metro.sales_fact values("
						+ "\""+f.customer_id+"\", "
						+ "\""+f.store_id+"\", "
						+ "\""+f.supplier_id+"\", "
						+ "\""+f.product_id+"\", "
						+ "\""+f.t_date+"\", "
						+ "\""+f.quantity+"\", "
						+ "\""+f.sales+"\");" ;
				r = st3.executeUpdate(Query);
				//System.out.println(r);
	        }
			
			System.out.println("Transaction Chunk Number: " +((transOffset/50)+1) + " loaded");
			hmap.clear();
		}
		int r = st3.executeUpdate("insert into metro.storeanalysis_mv (store_id, product_id, store_total)\r\n" + 
				"	(select s.store_id, p.product_id, sum(sf.sales)\r\n" + 
				"    from metro.store_dim s join metro.product_dim p join metro.sales_fact sf\r\n" + 
				"    on (s.store_id = sf.store_id AND p.product_id = sf.product_id)\r\n" + 
				"    group by s.store_id, p.product_id);");
		System.out.println(r);
		s.close();
	}
}
