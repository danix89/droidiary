package droidiary.db;

import org.json.JSONArray;
import org.json.JSONObject;

import doirdiary.db.sync.ContattoSync;
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
	
	public static int eliminaContatto(SQLiteDatabase db,int codUtente)
	{
		int u = db.delete(TABELLA, "id_account='"+codUtente+"'", null);
		return u;
	}
	/**
	 * ritorna tutti i contatti dell' account
	 * @param db
	 * @param s
	 * @return
	 */
	public static Cursor getContattiById(SQLiteDatabase db, int id){
		Cursor c= db.rawQuery("select id_account, nome, cognome, citta, cellulare, numeroCasa, mail from contatto where id_account='"+id+"' order by nome asc", null);
		return c;
	}

	public static Cursor getDatiById(SQLiteDatabase db, int id){
		Cursor c= db.rawQuery("select * from contatto where id_account='"+id+"'", null);
		return c;
	}

	public static Cursor getDatiFromString(SQLiteDatabase db, String contatto){
		String dati[]=contatto.split("-");
		Cursor c= db.rawQuery("select _id, id_account, nome, cognome, citta, cellulare, numeroCasa, mail from contatto where nome='"+dati[0]+"' and cognome='"+dati[1]+"'", null);
		return c;
	}

	public static int SincronizzaContatto(SQLiteDatabase db, int codUtente){ //da offline a online
		Cursor contatti=getDatiById(db, codUtente);
		String contattiSync= ContattoSync.getContattiById(codUtente);
		JSONArray jArray = null;
		try{
			jArray = new JSONArray(contattiSync);
		}catch (Exception e) {
		}
		System.out.println("Grandezza offline: " + contatti.getCount());
		System.out.println("Grandezza online: " + jArray.length());
		if(contatti.getCount()>jArray.length()){
			eliminaContatto(db, codUtente);
			ContattoSync.eliminaTuttiContatto(codUtente);
			while(contatti.moveToNext()){
				int id=contatti.getInt(0);
				System.out.println("ID: " +id);
				int id_account=contatti.getInt(1);
				System.out.println("id_account: " +id_account);
				String nome=contatti.getString(2);
				System.out.println("nome: " +nome);
				String cognome=contatti.getString(3);
				System.out.println("cognome: " +cognome);
				String citta=contatti.getString(4);
				System.out.println("citta: " +citta);
				String cell=contatti.getString(5);
				System.out.println("cellulare: " +cell);
				String numeroCasa=contatti.getString(6);
				System.out.println("numeroCasa: " +numeroCasa);
				String mail=contatti.getString(7);
				System.out.println("mail: " +mail);
				ContattoSync.modificaContatto(id, id_account, nome, cognome, citta, cell, numeroCasa, mail);
			}
		}else if(contatti.getCount()<=jArray.length()){
			eliminaContatto(db, codUtente);
			try{
				for(int i=0;i<jArray.length();i++){
					JSONObject json_data = jArray.getJSONObject(i);
					int id=json_data.getInt("_id");
					System.out.println("ID: " +id);
					int id_account=json_data.getInt("id_account");
					System.out.println("id_account: " +id_account);
					String nome=json_data.getString("nome");
					System.out.println("nome: " +nome);
					String cognome=json_data.getString("cognome");
					System.out.println("cognome: " +cognome);
					String citta=json_data.getString("citta");
					System.out.println("citta: " +citta);
					String cell=json_data.getString("cellulare");
					System.out.println("cellulare: " +cell);
					String numeroCasa=json_data.getString("numeroCasa");
					System.out.println("numeroCasa: " +numeroCasa);
					String mail=json_data.getString("mail");
					System.out.println("mail: " +mail);
					insertContattoAccount(db, id, id_account, nome, cognome, citta, cell, numeroCasa, mail);
				}
			}catch (Exception e) {
				// TODO: handle exception
			}

		}
		return 1;

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
