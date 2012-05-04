package droidiary.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Appuntamento {
	
	/**
	 * inserisce nuovo appuntamento
	 * @param db
	 * @param id_c
	 * @param id_a
	 * @param data_ora
	 * @param citta
	 * @param ind
	 * @param des
	 */
	public static void insertAccount(SQLiteDatabase db, String id_c, String id_a, String data, String ora, String citta, String ind, String des)
	{
		ContentValues v= new ContentValues();
		v.put(ID_CONTATTO, id_c);
		v.put(ID_ACCOUNT, id_a);
		v.put(DATA, data);
		v.put(ORA, ora);
		v.put(CITTA, citta);
		v.put(INDIRIZZO, ind);
		v.put(DESCRIZIONE, des);
		db.insert(TABELLA, null, v);
	}
	
	/**
	 * ritorna la lista di tutti gli appuntamenti dell'account
	 * @param db
	 * @return
	 */
	public static Cursor getAppuntamentiFromId(SQLiteDatabase db, int codice){
		Cursor c= db.rawQuery("select * from appuntamento where id_account='"+codice+"'", null);
		return c;
    }
	
	public static final String ID= "_id";
	public static final String ID_CONTATTO= "id_contatto";
	public static final String ID_ACCOUNT= "id_account";
	public static final String DATA= "data";
	public static final String ORA= "ora";
	public static final String CITTA = "citta";
	public static final String INDIRIZZO = "indirizzo";
	public static final String DESCRIZIONE= "desc";
	public static final String TABELLA = "account";
    public static final String[] COLONNE = new String[]{ID, ID_CONTATTO, ID_ACCOUNT, DATA, ORA, CITTA};
}
