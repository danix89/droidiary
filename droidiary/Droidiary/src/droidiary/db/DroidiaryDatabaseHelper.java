package droidiary.db;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DroidiaryDatabaseHelper extends SQLiteOpenHelper{


	public DroidiaryDatabaseHelper(Context context) {
		super(context, DB_NAME, null, 1);
		this.myContext= context;
	}

	public void onCreate(SQLiteDatabase db) {
		this.myDataBase=db;
	}

	public void onUpgrade() {
		int vers = this.myDataBase.getVersion() + 1;
		this.myDataBase.setVersion(vers);
	}

	public void createDataBase() throws IOException{

		boolean dbExist = checkDataBase();

		if(dbExist){
			
		}else{
			//..altrimenti lo creiamo
			this.getReadableDatabase();
		}
	}

	/*
		Metodo usato per creare il DB se non esiste

		db.execSQL(CREATE_TABLE_ACCOUNT);

		db.execSQL(CREATE_TABLE_CONTATTO);

		db.execSQL(CREATE_TABLE_APPUNTAMENTO);
	 */

	private void copyDataBase() {
		//Open your local db as the input stream
		InputStream myInput = null;
		//Open the empty db as the output stream
		OutputStream myOutput = null;
		try {
			//database in asset
			myInput = myContext.getAssets().open(DB_NAME);
			//database in smartphone
			myOutput = new FileOutputStream(DB_PATH+"droidiary");

			//transfer bytes from the inputfile to the outputfile
			byte[] buffer = new byte[1024];
			int length;
			while ((length = myInput.read(buffer))>0){
				myOutput.write(buffer, 0, length);
			}


			System.out.println("***************************************");
			System.out.println("####### Data base copied ##############");
			System.out.println("***************************************");


		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			//Close the streams
			try {
				myOutput.flush();
				myOutput.close();
				myInput.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void openDataBase() throws SQLException{
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

	}

	public synchronized void close() {

		if(myDataBase != null)
			myDataBase.close();

		super.close();

	}

	//controlla se il database esiste
	@SuppressWarnings("null")
	private boolean checkDataBase(){
		Boolean res=false;
		SQLiteDatabase checkDB = null;

		try{
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
		}
		catch (SQLiteException e) {
			//our database doesn't exist, so we'll create one or take an action.
		}
		if(checkDB!=null){
			Cursor cur;
			cur=checkDB.rawQuery("SELECT * FROM sqlite_master",null);
			if(cur.getCount()<=1){
				copyDataBase();
				
			}
			checkDB.close();
			res=true;
		}else{
			res=false;
			checkDB.close();
		}
		System.out.println(res);
		return res;
	}



	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {


	}

	private final Context myContext;
	private static String DB_PATH= "/data/data/droidiary.app/databases/";
	private static String DB_NAME = "droidiary";
	private SQLiteDatabase myDataBase;
	/*private static final String CREATE_TABLE_ACCOUNT = "create table account IF NOT EXISTS (_id integer PRIMARY KEY AUTOINCREMENT, username VARCHAR(6) NOT NULL, password VARCHAR(5) NOT NULL);";
	private static final String CREATE_TABLE_CONTATTO = "create table contatto IF NOT EXISTS (_id integer PRIMARY KEY AUTOINCREMENT, id_account INT(4) NOT NULL, nome VARCHAR(15) NOT NULL, " +
														"cognome VARCHAR(15) NOT NULL, citta VARCHAR(15), cellulare VARCHAR(11), numerocasa VARCHAR(11)," +
														" mail VARCHAR(11), FOREIGN KEY(id_account) REFERENCES account(_id));";
	private static final String CREATE_TABLE_APPUNTAMENTO = "create table appuntamento IF NOT EXISTS (_id integer PRIMARY KEY AUTOINCREMENT, id_contatto INT(4), id_account INT(4) NOT NULL," +
			   												"data_ora DATETIME NOT NULL, citta VARCHAR(15), indirizzo VARCHAR(15), descrizione VARCHAR(30), FOREIGN KEY(id_account) REFERENCES account(id_account)," +
			   												"FOREIGN KEY(id_contatto) REFERENCES contatto(_id));";*/
}



