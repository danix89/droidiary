package doirdiary.db.sync;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ContattoSync {

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
	public static void insertContatto(int id_a, String nome, String cognome, String citta, String cell, String ncasa, String mail)
	{
		String c="INSERT INTO contatto (id_account, nome, cognome, citta, cellulare, numeroCasa, mail) VALUES ('"+id_a+"',  '"+nome+"', '"+cognome+"', '"+citta+"', '"+cell+"', '"+ncasa+"', '"+mail+"')";
		System.out.println("Query da Inviare: " + c);
		send(c);
	}

	public static void insertContattoID(int id, int id_a, String nome, String cognome, String citta, String cell, String ncasa, String mail)
	{
		String c="INSERT INTO contatto (_id, id_account, nome, cognome, citta, cellulare, numeroCasa, mail) VALUES ('"+id+"','"+id_a+"',  '"+nome+"', '"+cognome+"', '"+citta+"', '"+cell+"', '"+ncasa+"', '"+mail+"')";
		System.out.println("Query da Inviare: " + c);
		send(c);
	}

	public static void insertContattoAccount(int id, int id_a, String nome, String cognome, String citta, String cell, String ncasa, String mail)
	{
		String c="INSERT INTO contatto (_id, id_account, nome, cognome, citta, cellulare, numeroCasa, mail) VALUES ('"+id+"', '"+id_a+"',  '"+nome+"', '"+cognome+"', '"+citta+"', '"+cell+"', '"+ncasa+"', '"+mail+"')";
		System.out.println("Query da Inviare: " + c);
		send(c);
	}

	public static void modificaContatto(int id, int id_a, String nome, String cognome, String citta, String cell, String ncasa, String mail)
	{
		String res1=getContattiById(id_a, id);
		if(res1.contains("null")){
			insertContattoID(id, id_a, nome, cognome, citta, cell, ncasa, mail);
		}else{
			String c= "update contatto set nome='"+nome+"', cognome='"+cognome+"', citta='"+citta+"', cellulare='"+cell+"', numeroCasa='"+ncasa+"', mail='"+mail+"' where id_account='"+id_a+"' and _id='"+id+"'";
			System.out.println("Query da Inviare: " + c);
			send(c);
		}
	}

	public static void eliminaContatto(int id, int id_c)
	{
		String c="delete from contatto where _id='"+id+"' and id_account='"+id_c+"'";
		System.out.println("Query da Inviare: " + c);
		send(c);
	}

	public static void eliminaTuttiContatto(int id_c)
	{
		String c="delete from contatto where id_account='"+id_c+"'";
		System.out.println("Query da Inviare: " + c);
		send(c);
	}

	public static String getContattiById(int id_account, int id_contatto){
		String query="select * from contatto where id_account='"+id_account+"' and _id='"+id_contatto+"'";
		System.out.println("Query da Inviare: " + query);
		String res=send(query);
		return res;
	}

	public static String getContattiById(int id_account){
		String query="select * from contatto where id_account='"+id_account+"'";
		System.out.println("Query da Inviare: " + query);
		String res=send(query);
		return res;
	}

	public static String getAllContatti(int id_a, String nome, String cognome, String citta, String cell, String ncasa, String mail){
		String query="select * from contatto where id_account='"+id_a+"' and nome='"+nome+"' and cognome='"+cognome+"' and citta='"+citta+"' and cellulare='"+cell+"' and numeroCasa='"+ncasa+"' and mail='"+mail+"'";
		System.out.println("Query da Inviare: " + query);
		String res=send(query);
		return res;
	}

	public static String getDatiFromString(int codUtente, String contatto){
		String dati[]=contatto.split("-");
		String c="select _id, id_account, nome, cognome, citta, cellulare, numeroCasa, mail from contatto where nome='"+dati[0]+"' and cognome='"+dati[1]+"' and id_account='"+codUtente+"'";
		String res=send(c);
		return res;
	}

	public static String send(String query) {
		String result = "0";
		InputStream is = null;

		//the query to send
		ArrayList<NameValuePair> querySend = new ArrayList<NameValuePair>();

		querySend.add(new BasicNameValuePair("querySend",query));

		//http post
		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://droidiary.altervista.org/query.php");
			httppost.setEntity(new UrlEncodedFormEntity(querySend));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		}catch(Exception e){
			Log.e("log_tag", "Error in http connection "+e.toString());
		}

		//convert response to string
		try{
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is,"iso-8859-1"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result=sb.toString();

		}catch(Exception e){
			Log.e("log_tag", "Error converting result: "+e.toString());
		}

		Log.i("SendQUERY", result);
		return result;
	}
}
