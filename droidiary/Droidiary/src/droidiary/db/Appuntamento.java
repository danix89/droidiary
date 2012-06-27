package droidiary.db;

import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import doirdiary.db.sync.AppuntamentoSync;
import doirdiary.db.sync.ContattoSync;

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
		Cursor c= db.rawQuery("select descrizione from appuntamento where id_account='"+codice+"'", null);
		return c;
	}

	public static Cursor getAppuntamentiToday(SQLiteDatabase db, int codice)
	{
		Calendar cal= Calendar.getInstance();
		String data= "" + cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.YEAR);
		System.out.println(data);
		Cursor c= db.rawQuery("select descrizione from appuntamento where id_account='"+codice+"' and data='"+ data + "'", null);
		System.out.println("select descrizione from appuntamento where id_account='"+codice+"' and data>='"+ data + "'");
		return c;
	}

	public static Cursor getDatiFromString(SQLiteDatabase db, int codUtente, String appuntamento) {
		Cursor c= db.rawQuery("select * from " + TABELLA + " where id_account ="+ codUtente +" and descrizione ='"+ appuntamento + "'", null);
		return c;
	}
	
	

	public static Cursor getDatiFromId(SQLiteDatabase db, int codUtente, int id) {
		Cursor c= db.rawQuery("select * from appuntamento where id_account='"+codUtente+"' and _id='"+id+"'", null);
		System.out.println("select * from appuntamento where id_account='"+codUtente+"' and _id='"+id+"'");
		return c;
	}

	private static Cursor getDatiById(SQLiteDatabase db, int codUtente) {
		Cursor c= db.rawQuery("select * from " + TABELLA + " where id_account ='"+ codUtente+"'", null);
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

	public static int SincronizzaAppuntamenti(SQLiteDatabase db, int codUtente){ //da offline a online
		Cursor appuntamenti=getDatiById(db, codUtente);
		String appuntamentiSync= AppuntamentoSync.getDatiFromId(codUtente);
		int sizeOnline=0;
		int sizeOffline=appuntamenti.getCount();
		JSONArray jArray = null;
		try{
			jArray = new JSONArray(appuntamentiSync);
		}catch (Exception e) {
		}
		System.out.println("Grandezza offline: " + appuntamenti.getCount());
		if(jArray==null){
		System.out.println("Grandezza online: 0");
		}else{
			sizeOnline=jArray.length();
			System.out.println("Grandezza online: " + sizeOnline);
		}
		
		if(sizeOffline==0 && sizeOnline==0){
		//do nothing
		}else if(sizeOffline>sizeOnline){
			System.out.println("FASE 3");
			while(appuntamenti.moveToNext()){
				int id=appuntamenti.getInt(0);
				System.out.println("ID: " +id);
				int id_account=appuntamenti.getInt(1);
				System.out.println("id_account: " +id_account);
				String descrizione=appuntamenti.getString(2);
				System.out.println("nome: " +descrizione);
				String indirizzo=appuntamenti.getString(3);
				System.out.println("cognome: " +indirizzo);
				String luogo=appuntamenti.getString(4);
				System.out.println("citta: " +luogo);
				String data=appuntamenti.getString(5);
				System.out.println("cellulare: " +data);
				String ora=appuntamenti.getString(6);
				System.out.println("numeroCasa: " +ora);
				String res=AppuntamentoSync.getAppuntamentiFromId(id_account, id);
				if(res.contains("null")){
					AppuntamentoSync.insertAppuntamento(id_account, descrizione, indirizzo, luogo, data, ora);
				}else{
					AppuntamentoSync.modificaAppuntamento(id, id_account, descrizione, indirizzo, luogo, data, ora);
				}
			}
		}else if(sizeOffline<sizeOnline){
			System.out.println("FASE 4");
			try{
				for(int i=0;i<jArray.length();i++){
					JSONObject json_data = jArray.getJSONObject(i);
					int id=json_data.getInt("_id");
					System.out.println("ID: " +id);
					int id_account=json_data.getInt("id_account");
					System.out.println("id_account: " +id_account);
					String descrizione=json_data.getString("descrizione");
					System.out.println("descrizione: " +descrizione);
					String indirizzo=json_data.getString("indirizzo");
					System.out.println("indirizzo: " +indirizzo);
					String luogo=json_data.getString("luogo");
					System.out.println("luogo: " +luogo);
					String data=json_data.getString("data");
					System.out.println("data: " +data);
					String ora=json_data.getString("ora");
					System.out.println("ora: " +ora);
					Cursor res=getAppuntamentiFromId(db, id_account);
					if(!res.moveToFirst()){
						modificaAppuntamento(db, id, id_account, descrizione, indirizzo, luogo, data, ora);
					}else{
						insertAppuntamento(db, id_account, descrizione, indirizzo, luogo, data, ora);
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else if(sizeOffline!=0 && sizeOnline!=0 && sizeOffline==sizeOnline){
			System.out.println("FASE 5");
			while(appuntamenti.moveToNext()){
				int id=appuntamenti.getInt(0);
				System.out.println("ID: " +id);
				int id_account=appuntamenti.getInt(1);
				System.out.println("id_account: " +id_account);
				String descrizione=appuntamenti.getString(2);
				System.out.println("nome: " +descrizione);
				String indirizzo=appuntamenti.getString(3);
				System.out.println("cognome: " +indirizzo);
				String luogo=appuntamenti.getString(4);
				System.out.println("citta: " +luogo);
				String data=appuntamenti.getString(5);
				System.out.println("cellulare: " +data);
				String ora=appuntamenti.getString(6);
				System.out.println("numeroCasa: " +ora);
				String res=AppuntamentoSync.getAppuntamentiFromId(id_account, id);
				if(res.contains("null")){
					AppuntamentoSync.insertAppuntamento(id_account, descrizione, indirizzo, luogo, data, ora);
				}else{
					AppuntamentoSync.modificaAppuntamento(id, id_account, descrizione, indirizzo, luogo, data, ora);
				}
			}
		}
		return 1;

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
