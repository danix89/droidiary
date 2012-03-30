package droidiary.app;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class Account {

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
	 * metodo che ritorno l'account con id uguale a quello passato come parametro
	 * @param db
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public static Cursor getAccountById(SQLiteDatabase db, long id) throws SQLException {
        Cursor c = db.query(true, TABELLA, COLONNE, ID + "=" + id, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }
	
	public static Cursor getAccountByUserPsw(SQLiteDatabase db, String[] s) throws SQLException {
		Cursor c= db.rawQuery("select * from account where username= ? and password= ?", s);
		return c;
	}
	
	public static Cursor getUsername(SQLiteDatabase db, String[] s) throws SQLException {
		Cursor c= db.rawQuery("select username from account where username= ? and password= ?", s);
		return c;
	}
	
	public static Cursor getPassword(SQLiteDatabase db, String[] s) throws SQLException {
		Cursor c= db.rawQuery("select password from account where username= ? and password= ?", s);
		return c;
	}
	
	public static final String ID= "_id";
	public static final String USERNAME= "username";
	public static final String PASSWORD= "password";
	public static final String TABELLA = "account";
    public static final String[] COLONNE = new String[]{ID, USERNAME, PASSWORD};
}
