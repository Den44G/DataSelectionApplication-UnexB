import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.junit.internal.matchers.IsCollectionContaining;

import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.awt.event.ActionEvent;

import com.mongodb.DBObject;
import com.mongodb.MongoException;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;
import javax.swing.JRadioButton;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JProgressBar;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class Frame1 {
    //Поля для формы
	private JFrame frmUnexbank;
	private JTextField textField;
	private JLabel lblInputEdrpou;
	private JTextField textField_1;
    private JDateChooser dateChooser;
    private JDateChooser dateChooser_1;
    private JRadioButton rdbtnNewRadioButton_1;
    private JRadioButton rdbtnNewRadioButton;
    private JRadioButton rdbtnLog;
    private JRadioButton rdbtnIp;
    private JTextArea outputWindow;
    private JProgressBar progressBar;
    //поля для методів    
    private long edrIn=0;
    private long client_id;
    private int id_key=0;
    private int ip_address=0;
    private int event=0;
    private int event_time=0;
    private File file;
    private BufferedWriter writer;
    private String client_name;
    private String stringOut;  //Строка виводу у текстове вікно
	//Строки для  даних з курсору
    private String stringEvent; 
	private String stringIpAdr;
	private String stringIdKey;
	//Форматування дати для виводу
	private SimpleDateFormat original = new SimpleDateFormat("yyyy-MM-dd HH:mm:sssZ");
	private SimpleDateFormat output= new SimpleDateFormat("/dd/MM/yyyy HH:mm:ss");
	private JTextField textField_2;
	private DataSelectionFromMongoDb ds;
	private MongoQuery1 mg1;
	private MongoQuery2 mg2;
	private ConnectToSql cs;
	private boolean isWorking;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame1 window = new Frame1();
					window.frmUnexbank.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Frame1() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmUnexbank = new JFrame();
		ImageIcon logonIcon= new ImageIcon(Frame1.class.getResource("unexlogo.png"));
		Image logo=logonIcon.getImage();
		frmUnexbank.setIconImage(logo);
		//frmUnexbank.setIconImage(ImageIO.read("res/unexlogo.png"));
		//frmUnexbank.setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\DPonomarenko\\Pictures\\unexlogo.png"));
		frmUnexbank.getContentPane().setBackground(new Color(230, 230, 250));
		frmUnexbank.setTitle("\u0416\u0443\u0440\u043D\u0430\u043B\u0438 \u043A\u043B\u0456\u0454\u043D\u0442-\u0431\u0430\u043D\u043A\u0430");
		frmUnexbank.setForeground(new Color(222, 184, 135));
		frmUnexbank.setFont(new Font("Times New Roman", Font.BOLD, 12));
		frmUnexbank.getContentPane().setForeground(new Color(230, 230, 250));
		frmUnexbank.setBounds(200, 200, 642, 542);
		frmUnexbank.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmUnexbank.getContentPane().setLayout(null);
		frmUnexbank.setResizable(false);
	
		/**
		 * При нажатии кнопки <Поиск>, выполняется подключение к базе КБ, выполняется поиск данных,
		 * в поле класса выводится значение ИД, в текстовое поле данные для сверки пользователем,
		 * ЕДРПОУ-long из-за совместимости форматов с БД MS-Sql
		 */
		JButton button = new JButton("\u041F\u043E\u0448\u0443\u043A");
		button.setFont(new Font("Tahoma", Font.BOLD, 14));
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
			edrIn=Long.parseLong(textField.getText());	
			//Очищення поля з введенним кодом ЄДРПОУ
			//textField.setText("");
		    cs= new ConnectToSql(edrIn);
	        client_id= cs.getId();
	        client_name=cs.getName();
	        String str="Назва: "+ cs.getName()+" "+"ЕДРПОУ: " + cs.getEdr();
	        if(str.contains("null")){
	        	JOptionPane.showMessageDialog(null, "Не знайдено,перевірте введений код ЄДРПОУ","Увага!",JOptionPane.INFORMATION_MESSAGE);
	        }else{
			textField_1.setText("Назва: "+ cs.getName()+" "+"ЕДРПОУ: " + cs.getEdr());
	        }
				}
				catch(NumberFormatException ex){
					JOptionPane.showMessageDialog(null,"Код ЄДРПОУ не визначений!","Увага!",JOptionPane.INFORMATION_MESSAGE);
				}
				catch(NullPointerException ex){
					JOptionPane.showMessageDialog(null,"З цим кодом ЄДРПОУ клієнта не зареєстровано,перевірте чи вірний введений код!","Увага!",JOptionPane.INFORMATION_MESSAGE);	
				}
			}
		});
		button.setBounds(470, 51, 146, 23);
		frmUnexbank.getContentPane().add(button);
		
		textField = new JTextField();
		textField.setBounds(228, 54, 166, 20);
		frmUnexbank.getContentPane().add(textField);
		textField.setColumns(10);
		
		lblInputEdrpou = new JLabel("\u041F\u043E\u0448\u0443\u043A \u043F\u043E \u043A\u043E\u0434\u0443  \u0404\u0414\u0420\u041F\u041E\u0423");
		lblInputEdrpou.setForeground(new Color(0, 0, 0));
		lblInputEdrpou.setBackground(new Color(169, 169, 169));
		lblInputEdrpou.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblInputEdrpou.setBounds(27, 55, 181, 14);
		frmUnexbank.getContentPane().add(lblInputEdrpou);
		
		textField_1 = new JTextField();
		textField_1.setBounds(27, 119, 589, 20);
		frmUnexbank.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblNewLabel = new JLabel(" \u041F\u0435\u0440\u0435\u0432\u0456\u0440\u0442\u0435  \u0434\u0430\u043D\u0456  \u043A\u043B\u0456\u0454\u043D\u0442\u0430");
		lblNewLabel.setForeground(new Color(0, 0, 0));
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel.setBounds(228, 85, 171, 23);
		frmUnexbank.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("\u0412\u0438\u0437\u043D\u0430\u0447\u0442\u0435 \u043F\u0435\u0440\u0456\u043E\u0434 \u0447\u0430\u0441\u0443");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_1.setBounds(27, 150, 138, 14);
		frmUnexbank.getContentPane().add(lblNewLabel_1);
		
		dateChooser = new JDateChooser();
		dateChooser.setBackground(Color.LIGHT_GRAY);
		dateChooser.getCalendarButton().setBackground(Color.LIGHT_GRAY);
		dateChooser.setBounds(27, 201, 108, 20);
		frmUnexbank.getContentPane().add(dateChooser);
		
		JLabel lblNewLabel_2 = new JLabel("\u0414\u0430\u0442\u0430 \u043F\u043E\u0447\u0430\u0442\u043A\u0443");
		lblNewLabel_2.setForeground(new Color(0, 0, 0));
		lblNewLabel_2.setBounds(145, 201, 88, 14);
		frmUnexbank.getContentPane().add(lblNewLabel_2);
		
	    dateChooser_1 = new JDateChooser();
	    dateChooser_1.getCalendarButton().setBackground(Color.LIGHT_GRAY);
		dateChooser_1.setBounds(27, 232, 108, 20);
		frmUnexbank.getContentPane().add(dateChooser_1);
		
		JLabel lblNewLabel_3 = new JLabel("\u0414\u0430\u0442\u0430 \u0437\u0430\u043A\u0456\u043D\u0447\u0435\u043D\u043D\u044F");
		lblNewLabel_3.setBounds(148, 241, 98, 14);
		frmUnexbank.getContentPane().add(lblNewLabel_3);
		
		
		
		
		
		rdbtnNewRadioButton = new JRadioButton("\u0417\u0430 \u0432\u0435\u0441\u044C \u043F\u0435\u0440\u0456\u043E\u0434 \u0440\u043E\u0431\u043E\u0442\u0438");
		rdbtnNewRadioButton.setBackground(new Color(230, 230, 250));
		rdbtnNewRadioButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnNewRadioButton.isSelected()){
				event_time=1;
				//dateChooser.setEnabled(false);
		        // dateChooser_1.setEnabled(false);	
				}else {
					event_time=0;
				}	
			}
		});
		rdbtnNewRadioButton.setBounds(27, 171, 159, 23);
		frmUnexbank.getContentPane().add(rdbtnNewRadioButton);
		
		//Выбор данных из базы MongoDB
		JButton btnNewButton = new JButton("\u0421\u0444\u043E\u0440\u043C\u0443\u0432\u0430\u0442\u0438");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(client_id==0){
					JOptionPane.showMessageDialog(null,"Клієнта не визначено!","Увага!",JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if ((event_time==0&dateChooser.getCalendar()==null)|(event_time==0&dateChooser_1.getCalendar()==null)){
					JOptionPane.showMessageDialog(null,"Період не визначено!","Увага!",JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				//если поле event-time = 0, то выбирается за период выбранный в календаре
				if(event_time==0){
					progressBar.setIndeterminate(true);
                mg1=new MongoQuery1();				
				mg1.execute();
				
				}
				
				
				// Выбор данных за весь период,вывод в JtextArea
				 if(event_time==1){
					mg2=new MongoQuery2();	
					 mg2.execute();
						}
				 
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 14));
//		btnNewButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//			//Проверка, не забыл ли пользователь указать ЕДРПОУ
//			if(client_id==0){
//				JOptionPane.showMessageDialog(null,"Клієнта не визначено!","Увага!",JOptionPane.INFORMATION_MESSAGE);
//				return;
//			}
//			//Проверка, не забыл ли пользователь указать период вообще(ловится NullPointerException )
//			//try{	
//			if(event_time==0){//поле которое меняется при выставлении флага т.е. если рол=0 то выбирается за период выбранный в календаре
//			//MongoQuery1 mg1 = new MongoQuery1();
//			mg1.execute();
//			
//			}
//			
//			 if(event_time==1){// Выбор данных за весь период,вывод в JtextArea
//				// MongoQuery2 mg2 = new MongoQuery2();
//					mg2.execute();}
//			//}catch(NullPointerException ex){
//				//JOptionPane.showMessageDialog(null,"Період не визначений!","Увага!",JOptionPane.INFORMATION_MESSAGE);
//				//return;
//			//}
//			}
//			});
		
		
		btnNewButton.setBounds(447, 163, 169, 34);
		frmUnexbank.getContentPane().add(btnNewButton);
				
		JLabel lblNewLabel_4 = new JLabel("");
		lblNewLabel_4.setBounds(379, 103, 145, 14);
		frmUnexbank.getContentPane().add(lblNewLabel_4);
		
		rdbtnIp = new JRadioButton("IP-\u0430\u0434\u0440\u0435\u0441\u0430");
		rdbtnIp.setBackground(new Color(230, 230, 250));
		rdbtnIp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnIp.isSelected()){
					ip_address=1;
					}else{
						ip_address=0;
					}
			}
		});
		rdbtnIp.setBounds(264, 171, 109, 23);
		frmUnexbank.getContentPane().add(rdbtnIp);
		
		rdbtnNewRadioButton_1 = new JRadioButton("ID-\u043A\u043B\u044E\u0447\u0430");
		rdbtnNewRadioButton_1.setBackground(new Color(230, 230, 250));
		rdbtnNewRadioButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (rdbtnNewRadioButton_1.isSelected()){
				id_key=1;}
				else{
					id_key=0;
				}
			}
		});
		rdbtnNewRadioButton_1.setBounds(264, 201, 109, 23);
		frmUnexbank.getContentPane().add(rdbtnNewRadioButton_1);
		
	    rdbtnLog = new JRadioButton("Log-\u0434\u0430\u043D\u0456");
	    rdbtnLog.setBackground(new Color(230, 230, 250));
		rdbtnLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(rdbtnLog.isSelected()){
				event=1;
				}else{
					event=0;
				}
			}
		});
		rdbtnLog.setBounds(264, 232, 109, 23);
		frmUnexbank.getContentPane().add(rdbtnLog);
		
		JLabel label = new JLabel("\u0412\u0438\u0431\u0435\u0440\u0456\u0442\u044C \u043F\u043E\u043B\u044F");
		label.setFont(new Font("Tahoma", Font.BOLD, 12));
		label.setBounds(275, 150, 98, 14);
		frmUnexbank.getContentPane().add(label);
		//Меню "Файл"
//		JMenuBar menuBar = new JMenuBar();
//		menuBar.setBackground(new Color(230, 230, 250));
//		menuBar.setBounds(0, 0, 77, 21);
//		frmUnexbank.getContentPane().add(menuBar);
//		
//		JMenu mnFile = new JMenu("   \u0424\u0430\u0439\u043B");
//		mnFile.setForeground(new Color(0, 0, 0));
//		mnFile.setIcon(null);
//		mnFile.setFont(new Font("Arial", Font.BOLD, 12));
//		mnFile.setBackground(new Color(230, 230, 250));
//		menuBar.add(mnFile);
//		
//		JMenuItem mntmSave = new JMenuItem("Зберегти");
//		mntmSave.setForeground(new Color(0, 0, 0));
//		mntmSave.setFont(new Font("Arial", Font.BOLD, 12));
//		mntmSave.setBackground(new Color(230, 230, 250));
//		mntmSave.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {           //Збереження файла по замовчанню .txt -формат.
//				JFileChooser fileSave = new JFileChooser("./");
//				int returnVal = fileSave.showSaveDialog(null);
//				file = fileSave.getSelectedFile();
//				writer=null;
//				if ( returnVal == JFileChooser.APPROVE_OPTION ) {
//		            try  
//		            {
//		            	writer = new BufferedWriter( new FileWriter( file.getAbsolutePath()+".txt"));
//		            	String s=outputWindow.getText();
//		            	writer.write(s);
//		            	writer.close();
//		            	JOptionPane.showMessageDialog(null, "Файл збережено",
//				                "Збереження", JOptionPane.INFORMATION_MESSAGE);
//		            }
//		            catch ( IOException ex ) {
//		            	JOptionPane.showMessageDialog(null, "Помилка файл не збережено",
//				                "Збереження,увага! ", JOptionPane.INFORMATION_MESSAGE);   
//		            }
//			}
//				outputWindow.setText("");
//			}
//		});
		
		//mnFile.add(mntmSave);
		
		outputWindow = new JTextArea(10,20);
		outputWindow.setLineWrap(true);;
		
		JScrollPane scrollPane = new JScrollPane(outputWindow);
		scrollPane.setBounds(27, 274, 589, 132);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		frmUnexbank.getContentPane().add(scrollPane);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(248, 432, 366, 14);
		progressBar.setVisible(true);
		frmUnexbank.getContentPane().add(progressBar);	
		
		JLabel lblNewLabel_5 = new JLabel("\u0417\u0430\u0447\u0435\u043A\u0430\u0439\u0442\u0435 \u0442\u0440\u0438\u0432\u0430\u0454 \u043F\u043E\u0448\u0443\u043A \u0434\u0430\u043D\u0438\u0445 .....");
		lblNewLabel_5.setBounds(27, 432, 206, 14);
		frmUnexbank.getContentPane().add(lblNewLabel_5);
		
		JLabel label_1 = new JLabel("\u041F\u043E\u0448\u0443\u043A \u043F\u043E \u0440\u0430\u0445\u0443\u043D\u043A\u0443");
		label_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		label_1.setBounds(27, 11, 177, 23);
		frmUnexbank.getContentPane().add(label_1);
		
		textField_2 = new JTextField();
		textField_2.setBounds(228, 14, 166, 20);
		frmUnexbank.getContentPane().add(textField_2);
		textField_2.setColumns(10);
		
		//Пошук по рахунку
		
		
		JButton btnNewButton_1 = new JButton("\u041F\u043E\u0448\u0443\u043A");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
				String account=textField_2.getText();
				ConnectToSql cs=new ConnectToSql(account);
				client_id=cs.getId();
				client_name=cs.getName();
				String str="Назва клієнта: "+cs.getName()+" "+ "Код ЄДРПОУ: "+ cs.getEdr()+" "+ "Рахунок :"+ cs.getAccount();
				//Парсінг строки ,якщо присутній null-інформує клієнта про помилку
				if(str.contains("null")==true){
					JOptionPane.showMessageDialog(null, "Не знайдено,перевірте введений рахунок!","Увага!",JOptionPane.INFORMATION_MESSAGE);
				}else{
				textField_1.setText("Назва клієнта: "+cs.getName()+" "+ "Код ЄДРПОУ: "+ cs.getEdr()+" "+ "Рахунок :"+ cs.getAccount());
				}
				}catch(NumberFormatException ex){
					JOptionPane.showMessageDialog(null, "Рахунок не визначений!","Увага!",JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton_1.setBounds(470, 11, 146, 23);
		frmUnexbank.getContentPane().add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("\u0417\u0431\u0435\u0440\u0435\u0433\u0442\u0438 \u0432 \u0444\u0430\u0439\u043B");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			//Збереження файлу(в текстовому форматі)	
				JFileChooser fileSave = new JFileChooser("./");
				int returnVal = fileSave.showSaveDialog(null);
				file = fileSave.getSelectedFile();
				writer=null;
				if ( returnVal == JFileChooser.APPROVE_OPTION ) {
		            try  
		            {
		            	writer = new BufferedWriter( new FileWriter( file.getAbsolutePath()+".txt"));
		            	String s=outputWindow.getText();
		            	writer.write(s);
		            	writer.close();
		            	JOptionPane.showMessageDialog(null, "Файл збережено",
				                "Збереження", JOptionPane.INFORMATION_MESSAGE);
		            }
		            catch ( IOException ex ) {
		            	JOptionPane.showMessageDialog(null, "Помилка файл не збережено",
				                "Збереження,увага! ", JOptionPane.INFORMATION_MESSAGE);   
		            }
			}
				outputWindow.setText("");
			}
				
			
		});
		
		btnNewButton_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton_2.setBounds(447, 465, 169, 38);
		frmUnexbank.getContentPane().add(btnNewButton_2);
		
		JLabel lblNewLabel_6 = new JLabel("\u0417\u0440\u043E\u0431\u043B\u0435\u043D\u043E \u0432 \u0434\u0435\u043F\u0430\u0440\u0442\u0430\u043C\u0435\u043D\u0442\u0456 \u0406\u0422(\u042E\u043D\u0435\u043A\u0441\u0411\u0430\u043D\u043A)by Denys Ponomarenko");
		lblNewLabel_6.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblNewLabel_6.setBounds(27, 479, 346, 14);
		frmUnexbank.getContentPane().add(lblNewLabel_6);
		
		// Припинення пошуку
		
		JButton btnNewButton_3 = new JButton("Припинити пошук");
		btnNewButton_3.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
//				outputWindow.setText("1 frame "+ mg1.isDone() + "2 frame "+ mg2.isDone());
				//if (isWorking == true){
					mg1.endWorker();
                    mg1.done();
					progressBar.setIndeterminate(false);
//				//return;
				
//				}
//				if(mg2.isCancelled()==false){
					//mg2.endWorker();
//					progressBar.setIndeterminate(false);
//				}
//			//	mg2.done();
				JOptionPane.showMessageDialog(null, "Пошук припинено!","Увага!",JOptionPane.INFORMATION_MESSAGE);
			//	outputWindow.setText("");
			}
		});
		btnNewButton_3.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewButton_3.setBounds(447, 227, 169, 25);
		frmUnexbank.getContentPane().add(btnNewButton_3);
	}
	
	
	
	class MongoQuery1 extends SwingWorker<Void, Void>{

		@Override
		protected Void doInBackground() throws Exception {
			String str;
			isWorking=true;
			try{
				progressBar.setIndeterminate(true);
				Calendar startDate=dateChooser.getCalendar();
				Calendar expirationDate=dateChooser_1.getCalendar();
				//Перевірка дат(якщо початок періоду більший за кінець)
				if(startDate.getTimeInMillis()>expirationDate.getTimeInMillis()){
					
					JOptionPane.showMessageDialog(null, "Перевірте чи вірно вказано період!","Невірний період!",JOptionPane.INFORMATION_MESSAGE);
					
				}else{
				 ds=new DataSelectionFromMongoDb(client_id, startDate, expirationDate, event, ip_address, id_key);
				 // TEST!!!
//				 Thread.currentThread();
//					try {
//						Thread.sleep(50000);
//					} catch (InterruptedException e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
//				//TEST!! 
				
				while(ds.getCursor().hasNext()){
					DBObject cur = ds.getCursor().next();
					String isoFormat = original.format(cur.get("event_time"));
					Date d = original.parse(isoFormat);
					String formattedTime = output.format(d);
					if(event==1){ 
						stringEvent="Log-роботи клієнта: "+ String.valueOf(cur.get("event"));
					}
					if(ip_address==1){ 
						stringIpAdr="IP-адреса: "+String.valueOf(cur.get("ip_address"));
					}
					if(id_key==1){ 
						stringIdKey="Ідентифікатор ключа :"+String.valueOf(cur.get("id_key"));
					}
					stringOut="Назва: "+ client_name + " " +stringEvent+" "+stringIpAdr+ " " +stringIdKey +" "+"Час з`єднання: " + formattedTime+ "\n" ;
					stringOut = stringOut.replaceAll("null", "");
					outputWindow.append(stringOut);
				
//					str=ds.getCursor().next().toString();
//					outputWindow.append(str+ "\n");
				}
				
				if (outputWindow.getText().trim().length()==0){
					JOptionPane.showMessageDialog(null, "За вказаний період часу,даних не знайдено!");
				}	
				
				}
			}catch(NullPointerException ex){
				JOptionPane.showMessageDialog(null," Перервано користувачем!");
			}
			return null;
		}
		
		public void done(){
			try{
				ds.closeCursor();
			progressBar.setIndeterminate(isCancelled());
			dateChooser.setCalendar(null);
			dateChooser_1.setCalendar(null);
			textField.setText("");
			textField_1.setText("");
			rdbtnNewRadioButton.setSelected(false);		
			rdbtnNewRadioButton_1.setSelected(false);
			rdbtnIp.setSelected(false);
			rdbtnLog.setSelected(false);
	   //	outputWindow.setText(String.valueOf(mg1.isCancelled()));
			//ds.mongoConnectionClose();
			}catch(NullPointerException ex){}
		     catch(MongoException ex){
		    	 JOptionPane.showMessageDialog(null,"Операцію перервано користувачем!");	 
		     }
		}
		public void endWorker(){
			try{
			//ds.closeCursor();
			mg1.cancel(true);
			//progressBar.setIndeterminate(isCancelled());
		//	outputWindow.setText(String.valueOf(progressBar.set));
			}catch(NullPointerException ex){
				JOptionPane.showMessageDialog(null,"Операцію перервано користувачем!");	
			}
		}
	}
	
	class MongoQuery2 extends SwingWorker<Void, Void>{
		
		@Override
		protected Void doInBackground() throws Exception {
			progressBar.setIndeterminate(true);
			
				 ds=new DataSelectionFromMongoDb(client_id,event, ip_address, id_key);
				
				 while(ds.getCursor().hasNext()){
					DBObject cur = ds.getCursor().next();
					String isoFormat = original.format(cur.get("event_time"));
					Date d = original.parse(isoFormat);
					String formattedTime = output.format(d);
					if(event==1){ 
						stringEvent="Log-роботи клієнта: "+ String.valueOf(cur.get("event"));
					}
					if(ip_address==1){ 
						stringIpAdr="IP-адреса: "+String.valueOf(cur.get("ip_address"));
					}
					if(id_key==1){ 
						stringIdKey="Ідентифікатор ключа :"+String.valueOf(cur.get("id_key"));
					}
					stringOut="Назва: "+ client_name + " " +stringEvent+" "+stringIpAdr+ " " +stringIdKey +" "+"Час з`єднання: " + formattedTime+ "\n" ;
					stringOut = stringOut.replaceAll("null", "");
					outputWindow.append(stringOut);
				}
			return null;
		}
		
		public void done(){
			try{
			progressBar.setIndeterminate(isCancelled());
			dateChooser.setCalendar(null);
			dateChooser_1.setCalendar(null);
			textField.setText("");
			textField_1.setText("");
			rdbtnNewRadioButton.setSelected(false);		
			rdbtnNewRadioButton_1.setSelected(false);
			rdbtnIp.setSelected(false);
			rdbtnLog.setSelected(false);
			//ds.mongoConnectionClose();
			}  catch(NullPointerException ex){
				JOptionPane.showMessageDialog(null, "Данні відсутні!");
			} catch(MongoException ex){
				JOptionPane.showMessageDialog(null, "Перервано користувачем!");
			}
		}
		
		public void endWorker(){
			ds.clearCursor();
			ds.closeCursor();
			mg2.cancel(true);
		}
	}
}
