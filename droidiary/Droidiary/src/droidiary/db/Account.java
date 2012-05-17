package droidiary.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class Account extends DroidiaryDatabaseHelper{

	public Account(Context context) {
		super(context);
	}

	/**
	 * metodo per inserire nuovo account
	 * @param db
	 * @param user
	 * @param psw
	 */
	public static void insertAccount(SQLiteDatabase db, String user, String psw)
	{
		ContentValues v= new ContentValues();
		v.put(USERNAME, user);
		v.put(PASSWORD, psw);
		db.insert(TABELLA, null, v);
	}
	
	/**
	 * ritorna la lista di tutti gli account
	 * @param db
	 * @return
	 */
	 public static Cursor getAllAccount(SQLiteDatabase db){
	        return db.query(TABELLA, COLONNE, null, null, null, null, null);
	    }
	
	/**
	 * metodo che ritorno l'account con id uguale a quello passato come parametro
	 * @param db
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public static Cursor getAccountById(SQLiteDatabase db, long id) throws SQLException {
		Cursor c= db.rawQuery("select nome, cognome from contatto where _id='"+id+"' and id_account='"+id+"'", null);
        return c;
    }
	

	/**
	 * ritorna l'intera riga tramite ricerca di user e psw
	 * @param db
	 * @param s
	 * @return
	 * @throws SQLException
	 */
	public static Cursor getAccountByUserPsw(SQLiteDatabase db, String[] s) throws SQLException {
		Cursor c= db.rawQuery("select * from account where username='"+s[0]+"' and password='"+s[1]+"'", null);
		return c;
	}
	
	public static String getStringAccountByUserPsw(String user, String pass) throws SQLException {
		String c="select * from account where username='"+user+"' and password='"+pass+"'";
		return c;
	}
	
	public static final String ID= "_id";
	public static final String USERNAME= "username";
	public static final String PASSWORD= "password";
	public static final String TABELLA = "account";
    public static final String[] COLONNE = new String[]{ID, USERNAME, PASSWORD};
}
