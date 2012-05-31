package droidiary.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Contatto {

	/**
	 * inserisce un nuovo contatto
	 * @param db
	 * @param id_a
	 * @param nome
	 * @param cognome
	 * @param citta
	 * @param cell
	 * @param ncasa
	 * @param mail
	 * @return 
	 */
	public static long insertContatto(SQLiteDatabase db, int id_a, String nome, String cognome, String citta, String cell, String ncasa, String mail)
	{
		ContentValues cv = new ContentValues();
		cv.put(ID_ACCOUNT, id_a);
		cv.put(NOME, nome);
		cv.put(COGNOME, cognome);
		cv.put(CITTA, citta);
		cv.put(CELLULARE, cell);
		cv.put(NUMEROCASA, ncasa);
		cv.put(MAIL, mail);
		long i = db.insert(TABELLA, null, cv);
		return i;
	}
	
	public static long insertContattoAccount(SQLiteDatabase db, int id, int id_a, String nome, String cognome, String citta, String cell, String ncasa, String mail)
	{
		ContentValues cv = new ContentValues();
		cv.put(ID, id);
		cv.put(ID_ACCOUNT, id_a);
		cv.put(NOME, nome);
		cv.put(COGNOME, cognome);
		cv.put(CITTA, citta);
		cv.put(CELLULARE, cell);
		cv.put(NUMEROCASA, ncasa);
		cv.put(MAIL, mail);
		long i = db.insert(TABELLA, null, cv);
		return i;
	}
	
	public static int modificaContatto(SQLiteDatabase db, int id_contatto, int codUtente, String nome, String cognome, String citta, String cell, String ncasa, String mail)
	{
		ContentValues cv = new ContentValues();
		cv.put(ID_ACCOUNT, codUtente);
		cv.put(NOME, nome);
		cv.put(COGNOME, cognome);
		cv.put(CITTA, citta);
		cv.put(CELLULARE, cell);
		cv.put(NUMEROCASA, ncasa);
		cv.put(MAIL, mail);
		int u = db.update(TABELLA, cv, "_id='"+id_contatto+"' and id_account='"+codUtente+"'", null);
		return u;
	}
	
	public static int eliminaContatto(SQLiteDatabase db, int id_c, int codUtente)
	{
		int u = db.delete(TABELLA, "_id='"+id_c+"' and id_account='"+codUtente+"'", null);
		return u;
	}
	/**
	 * ritorna tutti i contatti dell' account
	 * @param db
	 * @param s
	 * @return
	 */
	public static Cursor getContattiById(SQLiteDatabase db, long id){
		Cursor c= db.rawQuery("select id_account, nome, cognome, citta, cellulare, numeroCasa, mail from contatto where id_account='"+id+"'", null);
		return c;
    }
	
	public static Cursor getDatiFromString(SQLiteDatabase db, String contatto){
		String dati[]=contatto.split("-");
		Cursor c= db.rawQuery("select _id, id_account, nome, cognome, citta, cellulare, numeroCasa, mail from contatto where nome='"+dati[0]+"' and cognome='"+dati[1]+"'", null);
		return c;
    }
	
	public static final String ID= "_id";
	public static final String ID_ACCOUNT= "id_account";
	public static final String NOME= "nome";
	public static final String COGNOME= "cognome";
	public static final String CITTA= "citta";
	public static final String CELLULARE= "cellulare";
	public static final String NUMEROCASA= "numeroCasa";
	public static final String MAIL= "mail";
	public static final String TABELLA = "contatto";
    public static final String[] COLONNE = new String[]{ID, ID_ACCOUNT, NOME, COGNOME, CITTA, CELLULARE, NUMEROCASA, MAIL};
}
