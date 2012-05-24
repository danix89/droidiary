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
import droidiary.db.Account;
import droidiary.db.DroidiaryDatabaseHelper;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
				EditText txtnome = (EditText)findViewById(R.id.username); //creazione riferimenti a editText
				EditText txtpsw = (EditText)findViewById(R.id.password);
				username=txtnome.getText().toString();
				password=txtpsw.getText().toString();
				System.out.println("Username: "+username);
				System.out.println("Password: "+password);
				System.out.println(Account.getStringAccountByUserPsw(username, password));
				//String res=send();
				/*try {
					JSONObject root = new JSONObject(res);
					JSONArray jArray = root.getJSONArray("array");
					for(int i=0;i<jArray.length();i++){
                        JSONObject json_data = jArray.getJSONObject(i);
                        Log.d("ID", json_data.getString("username"));
                        Log.d("Value ", json_data.getString("password"));
					}
				} catch (JSONException e) {
					
					e.printStackTrace();
				}*/
				//System.out.println(res);
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
	
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
	       switch (item.getItemId()) {
	           case R.id.menu_about:Intent intent = new Intent(DroidiaryActivity.this, AboutActivity.class);
		    	startActivity(intent);
	           case R.id.menu_setup:Log.v("ttt", "Registrazione!");
	                 break;
	       }
	       return true;
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
		    HttpPost httppost = new HttpPost("http://www.droidiary.altervista.org/query.php");
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