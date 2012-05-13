package droidiary.db;

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
	public static Cursor insertContatto(SQLiteDatabase db, int id_a, String nome, String cognome, String citta, String cell, String ncasa, String mail)
	{
		Cursor c= db.rawQuery("INSERT INTO contatto (id_account,nome,cognome,citta,cellulare, numeroCasa,mail)VALUES ('"+id_a+"', '"+nome+"', '"+cognome+"', '"+citta+"', '"+cell+"', '"+ncasa+"', '"+mail+"')", null);
		return c;
	}
	
	/**
	 * ritorna tutti i contatti dell' account
	 * @param db
	 * @param s
	 * @return
	 */
	public static Cursor getContattiById(SQLiteDatabase db, int id){
		Cursor c= db.rawQuery("select * from contatto where id_account='"+id+"'", null);
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
