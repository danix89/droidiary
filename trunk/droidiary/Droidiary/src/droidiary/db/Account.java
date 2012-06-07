package droidiary.db;

import org.json.JSONArray;
import org.json.JSONObject;

import doirdiary.db.sync.AccountSync;
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
	 * @return 
	 */
	public static long insertAccount(SQLiteDatabase db, String user, String psw)
	{
		ContentValues v= new ContentValues();
		v.put(USERNAME, user);
		v.put(PASSWORD, psw);
		long i = db.insert(TABELLA, null, v);
		return i;
	}

	public static long insertAccount(SQLiteDatabase db, int id_Account, String user, String psw)
	{
		ContentValues v= new ContentValues();
		v.put(ID, id_Account);
		v.put(USERNAME, user);
		v.put(PASSWORD, psw);
		long i = db.insert(TABELLA, null, v);
		return i;
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

	public static Cursor getAccountByID(SQLiteDatabase db, long id) throws SQLException {
		Cursor c= db.rawQuery("select * from account where _id='"+id+"'", null);
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

	public static Cursor getAccountByUserPsw(SQLiteDatabase db, String user, String pass) throws SQLException {
		Cursor c= db.rawQuery("select * from account where username='"+user+"' and password='"+pass+"'", null);
		return c;
	}

	public static Cursor getMemorizzaAccesso(SQLiteDatabase db) throws SQLException {
		Cursor c= db.rawQuery("select accesso, username, password from account where accesso='si'", null);
		System.out.println("select accesso, username, password from account where accesso='si'");
		return c;
	}

	public static int MemorizzaAccessoSI(SQLiteDatabase db, String user, String pass) throws SQLException {
		ContentValues cv = new ContentValues();
		cv.put("accesso", "si");
		int u = db.update(TABELLA, cv, "username='"+user+"' and password='"+pass+"'", null);
		return u;
	}

	public static int MemorizzaAccessoNO(SQLiteDatabase db, String user, String pass) throws SQLException {
		ContentValues cv = new ContentValues();
		cv.put("accesso", "no");
		int u = db.update(TABELLA, cv, "username='"+user+"' and password='"+pass+"'", null);
		return u;
	}

	public static int getCountAccount(SQLiteDatabase db) throws SQLException {
		int res=0;
		Cursor c= db.rawQuery("SELECT  count(accesso) from account", null);
		if(c.moveToFirst()){
			res=c.getInt(0);
		}
		return res;
	}

	public static int SincronizzaAccount(SQLiteDatabase db, int codUtente){ //da offline a online
		Cursor account=getAccountByID(db, codUtente);
		String accountSync=AccountSync.getAccountById(codUtente);
		JSONArray jArray = null;
		try{
			jArray = new JSONArray(accountSync);
		}catch (Exception e) {
		}
		int sizeOnline=jArray.length();
		int sizeOffline=account.getCount();
		System.out.println("Grandezza offline: " + sizeOffline);
		System.out.println("Grandezza online: " + sizeOnline);
		
		if(sizeOffline>sizeOnline){
			while(account.moveToNext()){
				int id_account=account.getInt(0);
				System.out.println("id_account: " +id_account);
				String user=account.getString(1);
				System.out.println("username: " +user);
				String psw=account.getString(2);
				System.out.println("password: " +psw);
				AccountSync.insertAccount(id_account, user, psw);
			}
		}else{
			try{
				for(int i=0;i<jArray.length();i++){
					JSONObject json_data = jArray.getJSONObject(i);
					int id_account=json_data.getInt("_id");
					System.out.println("id_account: " +id_account);
					String username=json_data.getString("username");
					System.out.println("nome: " +username);
					String password=json_data.getString("password");
					System.out.println("cognome: " +password);
					insertAccount(db, id_account, username, password);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}

		}
		return 1;

	}

	public static final String ID= "_id";
	public static final String USERNAME= "username";
	public static final String PASSWORD= "password";
	public static final String TABELLA = "account";
	public static final String[] COLONNE = new String[]{ID, USERNAME, PASSWORD};
}
