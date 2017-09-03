import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.mongodb.*;


public class DataSelectionFromMongoDb {

	
	static final String DB_ADDRESS ="173.1.1.197";
	static final int DB_PORT= 27017;
	static final String DB_NAME ="ibank-log";
	static final String DB_COLLECTION ="log";
	private MongoClient mongo;
	private Calendar startDate=Calendar.getInstance();
	private Calendar expirationDate=Calendar.getInstance();
	private long client_id;
	private int event;
	private int ip_address;
	private int id_key;
	private int event_time;
	private DBCursor cursor;
	private MongoCredential credential;
	private MongoClient client;
	
	public DBCursor getCursor(){
		return cursor;
	}
	
	//Конструкторы класса
	
	public DataSelectionFromMongoDb(){};
	
	// в параметры конструктора передается 1 или 0 в зависимости от нажатых кнопок
	public DataSelectionFromMongoDb(long client_id,Calendar startDate,Calendar expirationDate,int event,int ip_address,int id_key ){
		this.client_id=client_id;
		this.startDate=startDate;
		this.expirationDate=expirationDate;
		this.event=event;
		this.ip_address=ip_address;
		this.id_key=id_key;
		
		int startDateYear=startDate.get(Calendar.YEAR);
		int startDateMonth=startDate.get(Calendar.MONTH)+1;
		int startDateDay=startDate.get(Calendar.DAY_OF_MONTH);
	    int expirationDateYear=expirationDate.get(Calendar.YEAR);
		int expirationDateMonth=expirationDate.get(Calendar.MONTH)+1;
		int expirationDateDay=expirationDate.get(Calendar.DAY_OF_MONTH)+1;
		
		credential=MongoCredential.createCredential("test", "ibank-log", "Test123".toCharArray());
		mongo= new MongoClient(new ServerAddress(DB_ADDRESS,DB_PORT),Arrays.asList(credential));
		DB db= mongo.getDB(DB_NAME);
		
		DBCollection table= db.getCollection(DB_COLLECTION);
		BasicDBObject dateRange= new BasicDBObject("$gt",new DateTime(startDateYear,startDateMonth,startDateDay,0,0,DateTimeZone.UTC).toDate());dateRange.put("$lte", new DateTime(expirationDateYear,expirationDateMonth,expirationDateDay,0,0,DateTimeZone.UTC ).toDate());
		BasicDBObject query= new BasicDBObject("event_time", dateRange).append("client_id",this.client_id);
		BasicDBObject fields= new BasicDBObject();
		fields.put("_id",0 );
		if(this.event==1){
		fields.put("event", this.event);
		}
		if(this.ip_address==1){
		fields.put("ip_address", this.ip_address);
		}
		if(this.id_key==1){
		fields.put("id_key", this.id_key);
		}
		fields.put("event_time", 1);
		cursor= table.find(query,fields);
		
	}
	
	public DataSelectionFromMongoDb(long client_id,int event,int ip_address,int id_key ){
		this.client_id=client_id;
		this.event=event;
		this.ip_address=ip_address;
		this.id_key=id_key;
	                    mongo= new MongoClient(DB_ADDRESS,DB_PORT);
		DB db= mongo.getDB(DB_NAME);
		DBCollection table= db.getCollection(DB_COLLECTION);
		BasicDBObject query= new BasicDBObject("client_id",this.client_id);
		BasicDBObject fields= new BasicDBObject();
		fields.put("_id",0 );
		if(this.event==1){
		fields.put("event", this.event);
		}
		if(this.ip_address==1){
		fields.put("ip_address", this.ip_address);
		}
		if(this.id_key==1){
		fields.put("id_key", this.id_key);
		}
		fields.put("event_time", 1);
		cursor= table.find(query,fields);
	}
	
	//Метод ,который закрывает соединение
	public void mongoConnectionClose(){
		this.mongo.close();
	}
		
	public void clearCursor(){
		this.cursor=null;
	}
	public void closeCursor(){
		this.cursor.close();
		//this.cursor.maxTime(1, TimeUnit.SECONDS);
	}
}
