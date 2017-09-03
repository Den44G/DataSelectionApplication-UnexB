
import java.sql.*;
import java.util.ArrayList;
public class ConnectToSql {
	
	//Обьявление переменных
	
	static final String DB_URL = "jdbc:sqlserver://srv-db\\ibank2ua:1433";
	static final String USER = "JavaUser";
	static final String PASS = "Java12345";
	
	private long edrpoy ;
	private String account;
	private Connection conn = null;
    private ResultSet rs;
    private long id;
    private String name;
    PreparedStatement preparedStatement = null;
    
    //Конструкторы
    
    ConnectToSql(){}; 
    
    ConnectToSql(long edr){
            	 edrpoy= edr; 
        try {   
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            if (conn != null) {
                String selectSQL="SELECT name_cln,addr_cln,okpo,client_id from ibank2ua.clients WHERE okpo = ?";
                PreparedStatement pS=conn.prepareStatement(selectSQL);
                pS.setLong(1, edrpoy);
                rs=pS.executeQuery();
                while (rs.next()){
                	this.edrpoy=rs.getLong("okpo");
                	this.id=rs.getLong("client_id");
                	this.name=rs.getString("name_cln");
                } 
                conn.close();
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
	    }
	
    ConnectToSql(String account){
    	this.account=account;
    	try{
    		conn = DriverManager.getConnection(DB_URL, USER, PASS);
        if (conn != null) {
        String selectSQL="SELECT a.account,c.name_cln,c.addr_cln,c.okpo,c.client_id from ibank2ua.accounts a,ibank2ua.clients c WHERE a.client_id=c.client_id AND account=?";
           PreparedStatement pS=conn.prepareStatement(selectSQL);
            pS.setString(1, account);
            rs=pS.executeQuery();
            while (rs.next()){
            	this.edrpoy=rs.getLong("okpo");
            	this.id=rs.getLong("client_id");
            	this.name=rs.getString("name_cln");
            	this.account=rs.getString("account");
            } 
            conn.close();
        }
    			
    	}catch(SQLException ex){
    		ex.printStackTrace();}
    }
    
	    public String getName(){
	    	return this.name;
	    }
	    
	    public Long getId(){
	    	return this.id;
	    }
	    
	    public Long getEdr(){
	    	return this.edrpoy;
	    }
	    
	    public String getAccount(){
	    	return this.account;
	    }
	   
	   public  ArrayList<String> accSel(){
		 ArrayList<String > arr = new ArrayList<String>();
		 try{
	    		conn = DriverManager.getConnection(DB_URL, USER, PASS);
	        if (conn != null) {
	        String selectSQL="SELECT account from ibank2ua.accounts";
	           PreparedStatement pS=conn.prepareStatement(selectSQL);
	            rs=pS.executeQuery(); 
	           while(rs.next()){
	        	   arr.add(rs.getString(account));
	           }    
	           conn.close();
	        }		
	    	}catch(SQLException ex){
	    		ex.printStackTrace();
	    		}
		return arr;
	   } 
 
	   


}