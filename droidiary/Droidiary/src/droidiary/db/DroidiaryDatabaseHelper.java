package droidiary.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DroidiaryDatabaseHelper extends SQLiteOpenHelper{

	
	public DroidiaryDatabaseHelper(Context context) {
	super(context, DB_NAME, null, DB_VERSION);
	}
	
	
	public void onCreate(SQLiteDatabase db) {
		
		//Metodo usato per creare il DB se non esiste
		
		db.execSQL(CREATE_TABLE_ACCOUNT);
		
		db.execSQL(CREATE_TABLE_CONTATTO);
		
		db.execSQL(CREATE_TABLE_APPUNTAMENTO);
		
	}

	
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		
		
	}
	
	private static final String DB_NAME = "droidiary";
	private static final int DB_VERSION = 1;
	private static final String CREATE_TABLE_ACCOUNT = "create table account IF NOT EXISTS (_id integer PRIMARY KEY AUTOINCREMENT, username VARCHAR(6) NOT NULL, password VARCHAR(5) NOT NULL);";
	private static final String CREATE_TABLE_CONTATTO = "create table contatto IF NOT EXISTS (_id integer PRIMARY KEY AUTOINCREMENT, id_account INT(4) NOT NULL, nome VARCHAR(15) NOT NULL, " +
														"cognome VARCHAR(15) NOT NULL, citta VARCHAR(15), cellulare VARCHAR(11), numerocasa VARCHAR(11)," +
														" mail VARCHAR(11), FOREIGN KEY(id_account) REFERENCES account(_id));";
	private static final String CREATE_TABLE_APPUNTAMENTO = "create table appuntamento IF NOT EXISTS (_id integer PRIMARY KEY AUTOINCREMENT, id_contatto INT(4), id_account INT(4) NOT NULL," +
			   												"data_ora DATETIME NOT NULL, citta VARCHAR(15), indirizzo VARCHAR(15), descrizione VARCHAR(30), FOREIGN KEY(id_account) REFERENCES account(id_account)," +
			   												"FOREIGN KEY(id_contatto) REFERENCES contatto(_id));";
}



