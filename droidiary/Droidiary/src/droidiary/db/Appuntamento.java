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
	 * @return 
	 */
	public static long insertAppuntamento(SQLiteDatabase db, int id_a, String des, String ind, String luogo, String data, String ora)
	{
		ContentValues v= new ContentValues();
		v.put(ID_ACCOUNT, id_a);
		v.put(DESCRIZIONE, des);
		v.put(INDIRIZZO, ind);
		v.put(LUOGO, luogo);
		v.put(DATA, data);
		v.put(ORA, ora);
		long i = db.insert(TABELLA, null, v);
		return i;
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
	
	public static Cursor getDatiFromString(SQLiteDatabase db, int codUtente, String appuntamento) {
		Cursor c= db.rawQuery("select * from " + TABELLA + " where id_account ="+ codUtente +" and descrizione ='"+ appuntamento + "'", null);
		return c;
	}

	public static Cursor getDatiFromId(SQLiteDatabase db, int codUtente, int id) {
		Cursor c= db.rawQuery("select * from " + TABELLA + " where _id='"+id+"' and id_account ="+ codUtente, null);
		return c;
	}
	
	public static int modificaAppuntamento(SQLiteDatabase db, int id, int id_a, String des, String ind, String luogo, String data, String ora)
	{
		ContentValues cv = new ContentValues();
		cv.put(ID_ACCOUNT, id_a);
		cv.put(DESCRIZIONE, des);
		cv.put(INDIRIZZO, ind);
		cv.put(LUOGO, luogo);
		cv.put(DATA, data);
		cv.put(ORA, ora);
		int u = db.update(TABELLA, cv, "_id='"+id+"' and id_account='"+id_a+"'", null);
		return u;
	}
	
	public static int eliminaAppuntamento(SQLiteDatabase db, int id, int id_a) {
		int u = db.delete(TABELLA, "_id='"+id+"' and id_account='"+id_a+"'", null);
		return u;
	}
	
	public static final String ID= "_id";
	public static final String ID_ACCOUNT= "id_account";
	public static final String DESCRIZIONE= "descrizione";
	public static final String INDIRIZZO = "indirizzo";
	public static final String LUOGO = "luogo";
	public static final String DATA= "data";
	public static final String ORA= "ora";
	public static final String TABELLA = "appuntamento";
    public static final String[] COLONNE = new String[]{ID, ID_ACCOUNT, DESCRIZIONE, INDIRIZZO, LUOGO, DATA, ORA};
}
