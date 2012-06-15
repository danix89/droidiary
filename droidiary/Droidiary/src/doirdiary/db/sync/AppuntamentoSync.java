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
import android.util.Log;

public class AppuntamentoSync {

	public static void insertAppuntamento(int id_a, String des, String ind, String luogo, String data, String ora)
	{
		String c="insert into appuntamento (id_account, descrizione, indirizzo, luogo, data, ora) VALUES ('"+id_a+"',  '"+des+"', '"+ind+"', '"+luogo+"', '"+data+"', '"+ora+"')";
		System.out.println("Query da Inviare: " + c);
		send(c);
	}

	public static String getAppuntamentiFromId(int id_a, int id){
		String c="select * from appuntamento where id_account='"+id_a+"' and _id='"+id+"'";
		System.out.println("Query da Inviare: " + c);
		String res= send(c);
		return res;
	}
	
	public static String getAppuntamentiFromId(int codUtente) {
		String c="select * from appuntamento where id_account='"+codUtente+"'";
		System.out.println("Query da Inviare: " + c);
		String res= send(c);
		return res;
	}

	public static String getDatiFromString(int codUtente, String appuntamento) {
		String c="select * from appuntamento where id_account ="+ codUtente +" and descrizione ='"+ appuntamento + "'";
		System.out.println("Query da Inviare: " + c);
		String res= send(c);
		return res;
	}

	public static String getDatiFromId(int codUtente, int id) {
		String c= "select * from appuntamento where _id='"+id+"' and id_account ="+codUtente;
		System.out.println("Query da Inviare: " + c);
		String res= send(c);
		return res;
	}
	
	public static String getDatiFromId(int codUtente) {
		String c= "select * from appuntamento where id_account ='"+codUtente+"'";
		System.out.println("Query da Inviare: " + c);
		String res= send(c);
		return res;
	}

	public static void modificaAppuntamento (int id, int id_a, String des, String ind, String luogo, String data, String ora)
	{
		String res1=AppuntamentoSync.getAppuntamentiFromId(id_a, id);
		if(res1.contains("null")){
			insertAppuntamento(id_a, des, ind, luogo, data, ora);
		}else{
			String c= "update appuntamento set descrizione='"+des+"', indirizzo='"+ind+"', luogo='"+luogo+"', data='"+data+"', ora='"+ora+"' where id_account='"+id_a+"'";
			System.out.println("Query da Inviare: " + c);
			send(c);
		}
	}

	public static void eliminaAppuntamento(int id, int id_a) {
		String c="delete from appuntamento where _id='"+id+"' and id_account='"+id_a+"'";
		System.out.println("Query da Inviare: " + c);
		send(c);
	}

	public static void eliminaTuttiAppuntamenti(int id_c)
	{
		String c="delete from appuntamento where id_account='"+id_c+"'";
		System.out.println("Query da Inviare: " + c);
		send(c);
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


	static String id_account;
	static String descrizione;
	static String indirizzo;
	static String luogo;
	static String data;
	static String ora;

}
