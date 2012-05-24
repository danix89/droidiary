package droidiary.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.*;
import droidiary.db.Account;
import droidiary.db.DroidiaryDatabaseHelper;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class DroidiaryActivity extends Activity{
	/** Called when the activity is first created. */

	public void onCreate(Bundle savedInstanceState) {

		//codice per il font
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		dbd = new DroidiaryDatabaseHelper(this); //collegamento database
		db=dbd.getReadableDatabase();
		try {
			dbd.createDataBase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}

		final RadioButton online= (RadioButton) findViewById(R.id.online);
		final RadioButton offline=(RadioButton) findViewById(R.id.offline);
		
		
		Button entra = (Button) findViewById(R.id.entra);

		
		entra.setOnClickListener(new OnClickListener() {


			public void onClick(View v) {
				
				if(offline.isChecked()){

				EditText txtnome = (EditText)findViewById(R.id.username); //creazione riferimenti a editText
				txtnome.setImeOptions(EditorInfo.IME_ACTION_NEXT);

				EditText txtpsw = (EditText)findViewById(R.id.password);
				txtpsw.setImeOptions(EditorInfo.IME_ACTION_DONE);


				String[] arg={txtnome.getText().toString(), txtpsw.getText().toString()};
				
				username=txtnome.getText().toString();
				password=txtpsw.getText().toString();

				try {
					dbd.openDataBase();
				}catch(SQLException sqle){

					throw sqle;

				}
				
								
				Cursor c= Account.getAccountByUserPsw(db, arg);
				if(c.moveToFirst()){
					Toast.makeText(getApplicationContext(), "Login effettuato con successo!", Toast.LENGTH_LONG).show();
					int codUtente= c.getInt(0);
					System.out.println(codUtente);
					Intent intent = new Intent(DroidiaryActivity.this, MenuPrincipaleActivity.class);
					boolean status=false;
					intent.putExtra("droidiary.app.DroidiaryActivity", true);
					intent.putExtra("droidiary.app.DroidiaryActivity", codUtente);
					dbd.close();
					startActivity(intent);
				}else{
					Toast.makeText(getApplicationContext(), "Dati non esatti", Toast.LENGTH_LONG).show();
				}
			}else if(online.isChecked()){
				try {
					dbd.openDataBase();
				}catch(SQLException sqle){

					throw sqle;

				}
				System.out.println("Username: "+username);
				System.out.println("Password: "+password);
				String query = Account.getStringAccountByUserPsw(username, password);
				String res=send(query);
				int codUtente =0;
				try {
					JSONArray jArray = new JSONArray(res);
					for(int i=0;i<jArray.length();i++){
                        JSONObject json_data = jArray.getJSONObject(i);
                        Log.d("Username", json_data.getString("username"));
                        Log.d("Password ", json_data.getString("password"));
                        username = json_data.getString("username");
                        password = json_data.getString("password");
                        codUtente = json_data.getInt("_id");
					}
				} catch (JSONException e) {
					
					e.printStackTrace();
				}
				System.out.println(codUtente);
				if(username!=null && password!=null && codUtente!=0){
					Toast.makeText(getApplicationContext(), "Login effettuato con successo!", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(DroidiaryActivity.this, MenuPrincipaleActivity.class);
					boolean status=true;
					intent.putExtra("droidiary.app.DroidiaryActivity", status);
					intent.putExtra("droidiary.app.DroidiaryActivity", codUtente);
					
					startActivity(intent);
				}else{
					Toast.makeText(getApplicationContext(), "Dati non presenti", Toast.LENGTH_LONG).show();
				}
				send(Account.getStringAccountByUserPsw(username, password));
				dbd.close();
			}
			}
		});

		Button cancella = (Button) findViewById(R.id.cancella);
		cancella.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				EditText txtnome = (EditText)findViewById(R.id.username);
				EditText txtpsw = (EditText)findViewById(R.id.password);
				txtnome.setText("");
				txtpsw.setText("");
			}
		});
		
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
	
	   
	//chiusura dell'app
	public void onBackPressed(){
		Intent intent = new Intent(Intent.ACTION_MAIN);
		   intent.addCategory(Intent.CATEGORY_HOME);
		   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		   startActivity(intent);
	}
	private DroidiaryDatabaseHelper dbd;
	private SQLiteDatabase db;
	private String username, password;
}