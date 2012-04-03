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
	 */
	public static void insertContatto(SQLiteDatabase db, String id_a, String nome, String cognome, String citta, String cell, String ncasa, String mail)
	{
		ContentValues v= new ContentValues();
		v.put(ID_ACCOUNT, id_a);
		v.put(NOME, nome);
		v.put(COGNOME, cognome);
		v.put(CITTA, citta);
		v.put(CELLULARE, cell);
		v.put(NUMEROCASA, ncasa);
		v.put(MAIL, mail);
		
		db.insert(TABELLA, null, v);
	}
	
	/**
	 * ritorna tutti i contatti dell' account
	 * @param db
	 * @param s
	 * @return
	 */
	public static Cursor getAllContatto(SQLiteDatabase db, String[] s){
		Cursor c= db.rawQuery("select nome, cognome from contatto where id_account= ?", s);
		return c;
    }
	
	public static final String ID= "_id";
	public static final String ID_ACCOUNT= "id_account";
	public static final String NOME= "nome";
	public static final String COGNOME= "cognome";
	public static final String CITTA= "citta";
	public static final String CELLULARE= "cellulare";
	public static final String NUMEROCASA= "numerocasa";
	public static final String MAIL= "mail";
	public static final String TABELLA = "contatto";
    public static final String[] COLONNE = new String[]{ID, ID_ACCOUNT, NOME, COGNOME, CITTA, CELLULARE, NUMEROCASA, MAIL};
}
