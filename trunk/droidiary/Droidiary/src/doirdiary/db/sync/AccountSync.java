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
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AccountSync{


	public static String insertAccount(int id, String user, String psw)
	{
		String c="INSERT INTO  account (_id, username, password) VALUES ('"+id+"','"+user+"',  '"+psw+"')";
		System.out.println("Query da Inviare: " + c);
		String res= send(c);
		return res;
	}
	
	
	
	public static String countAccount()
	{
		String c="SELECT COUNT( _id ) FROM account";
		System.out.println("Query da Inviare: " + c);
		String res= send(c);
		return res;
	}
	
	public static String insertAccount(String user, String psw)
	{
		String c="INSERT INTO  account (username, password) VALUES ('"+user+"',  '"+psw+"')";
		System.out.println("Query da Inviare: " + c);
		String res= send(c);
		return res;
	}
	
	public static String getStringAccountByUserPsw(String user, String pass) throws SQLException {
		String c="select * from account where username='"+user+"' and password='"+pass+"'";
		System.out.println("Query da Inviare: " + c);
		String res= send(c);
		return res;
	}
	
	public static String getAllAccount(){
		String c="select * from account";
		System.out.println("Query da Inviare: " + c);
		String res= send(c);
		return res;
	}
		
	public static String getContattoAccountById(long id){
		String c="select nome, cognome from contatto where _id='"+id+"' and id_account='"+id+"'";
		System.out.println("Query da Inviare: " + c);
		String res= send(c);
		return res;
	}
	
	public static String getAccountById(int id_account){
		String query="select * from account where _id='"+id_account+"'";
		System.out.println("Query da Inviare: " + query);
		String res=send(query);
		return res;
    }
	
	public static void modificaAccount(int id_a, String user, String psw)
	{
		String res1=getAccountById(id_a);
		if(res1.contains("null")){
			insertAccount(id_a, user, psw);
		}else{
			String c= "update account set id_account='"+id_a+"', username='"+user+"', password='"+psw+"' where _id='"+id_a+"'";
			System.out.println("Query da Inviare: " + c);
			send(c);
		}
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
