package droidiary.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DroidiaryDatabaseHelper extends SQLiteOpenHelper{

	
	public DroidiaryDatabaseHelper(Context context) {
	super(context, DB_NAME, null, DB_VERSION);
	}
	
	
	public void onCreate(SQLiteDatabase db) {
		
		//creazione tabelle
		
		db.execSQL("create table `account`(`_id` INT(4) PRIMARY KEY, `userName` VARCHAR(6) NOT NULL, `password` VARCHAR(5) NOT NULL);");
		
		db.execSQL("create table `contatto`( `_id` INT(4) PRIMARY KEY, `id_account` INT(4) NOT NULL, `nome` VARCHAR(15) NOT NULL, " +
				            				"`cognome` VARCHAR(15) NOT NULL, `citta` VARCHAR(15), `cellulare` VARCHAR(11), `numeroCasa` VARCHAR(11)," +
				            				" `mail` VARCHAR(11), FOREIGN KEY(`id_account`) REFERENCES account(`_id`));");
		
		db.execSQL("create table `appuntamento`(`_id` INT(4) PRIMARY KEY, `id_contatto` INT(4), `id_account` INT(4) NOT NULL," +
											   "`data/ora` DATETIME NOT NULL, `citta` VARCHAR(15), FOREIGN KEY(`id_account`) REFERENCES account(`id_account`)," +
				 							   "FOREIGN KEY(`id_contatto`) REFERENCES contatto(`_id`));");
		
		//riempimento tabelle
		
		ContentValues cv= new ContentValues();
		
		/**/
		
		cv.put("username", "marpir");
		cv.put("password", "m0001");
		db.insert("account", null, cv);
		
		cv.put("username", "saladd");
		cv.put("password", "s0001");
		db.insert("account", null, cv);
		
		/**/
		
		cv.put("id_account", "1");
		cv.put("nome", "mario");
		cv.put("cognome", "pirone");
		cv.put("citta", "napoli");
		cv.put("cellulare", "3931567893");
		cv.put("numeroCasa", "082523185");
		cv.put("mail", "mp78@hotmail.it");
		db.insert("contatto", null, cv);
		
		cv.put("id_account", "1");
		cv.put("nome", "salvatore");
		cv.put("cognome", "addesa");
		cv.put("citta", "milano");
		cv.put("cellulare", "3924566789");
		cv.put("numeroCasa", "081234565");
		cv.put("mail", "sd@hotmail.it");
		db.insert("contatto", null, cv);
		
		/**/
		
		cv.put("id_contatto", "1");
		cv.put("id_account", "1");
		cv.put("data/ora", "2012/11/11 13:20:00");
		cv.put("citta", "napoli");
		db.insert("appuntamento", null, cv);
		
		cv.put("id_contatto", "2");
		cv.put("id_account", "1");
		cv.put("data/ora", "2012/09/11 15:40:00");
		cv.put("citta", "milano");
		db.insert("appuntamento", null, cv);
		
		
	}

	
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		
		
	}
	
	private static final String DB_NAME = "droidiary";
	private static final int DB_VERSION = 1;
}
