package droidiary.app;

import java.io.IOException;
import org.json.*;

import doirdiary.db.sync.AccountSync;
import droidiary.db.Account;
import droidiary.db.DroidiaryDatabaseHelper;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class DroidiaryActivity extends Activity{
	/** Called when the activity is first created. */

	public void onCreate(Bundle savedInstanceState) {

		dbd = new DroidiaryDatabaseHelper(this); //collegamento database
		dbd.getReadableDatabase();
		try {
			dbd.createDataBase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}
		//codice per il font
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		online= (RadioButton) findViewById(R.id.online);
		offline=(RadioButton) findViewById(R.id.offline);
		access = (CheckBox)findViewById(R.id.access);

		//riempimento txt accesso

		boolean res=checkMemorizzaAccesso();
		System.out.println("Memorizza Accesso: " + res);
		if(res==true){
			dbd = new DroidiaryDatabaseHelper(this); //collegamento database
			db=dbd.getReadableDatabase();
			dbd.openDataBase();
			Cursor c= Account.getMemorizzaAccesso(db);
			EditText txtnome = (EditText)findViewById(R.id.username);
			EditText txtpass = (EditText)findViewById(R.id.password);
			if(c.moveToNext()){
				String username=c.getString(1);
				String password=c.getString(2);
				txtnome.setText(username);
				txtpass.setText(password);
				access.setChecked(true);
				offline.setChecked(true);
			}
			dbd.close();
		}

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

					db=dbd.getReadableDatabase();
					try {
						dbd.openDataBase();
					}catch(SQLException sqle){

						throw sqle;

					}
					Cursor c= Account.getAccountByUserPsw(db, arg);
					if(access.isChecked()){
						System.out.println("Click Accesso");
						MemorizzaAccessoSI(username, password);
					}else{
						MemorizzaAccessoNO(username, password);
					}
					
					if(c.moveToFirst()){
						Toast.makeText(getApplicationContext(), "Login effettuato con successo!", Toast.LENGTH_LONG).show();
						int codUtente= c.getInt(0);
						System.out.println(codUtente);
						Intent intent = new Intent(DroidiaryActivity.this, MenuPrincipaleActivity.class);
						String status="false";
						intent.putExtra("droidiary.app.DroidiaryActivity", codUtente);
						intent.putExtra("Status", status);
						db.close();
						dbd.close();
						startActivity(intent);
					}else{
						Toast.makeText(getApplicationContext(), "Dati non esatti", Toast.LENGTH_LONG).show();
					}
				}else if(online.isChecked()){
						EditText txtnome = (EditText)findViewById(R.id.username); //creazione riferimenti a editText
						EditText txtpsw = (EditText)findViewById(R.id.password);
						username=txtnome.getText().toString();
						password=txtpsw.getText().toString();
						System.out.println("Username: "+username);
						System.out.println("Password: "+password);

						if(access.isChecked()){
							System.out.println("Click Accesso");
							MemorizzaAccessoSI(username, password);
						}else{
							MemorizzaAccessoNO(username, password);
						}
						String res = AccountSync.getStringAccountByUserPsw(username, password);
						System.out.println(res);
						int codUtente=0;
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
							String status="true";
							intent.putExtra("Status", status);
							intent.putExtra("droidiary.app.DroidiaryActivity", codUtente);
							db.close();
							dbd.close();
							startActivity(intent);
						}else{
							Toast.makeText(getApplicationContext(), "Dati non presenti", Toast.LENGTH_LONG).show();
						}

					}

				}
		});
	}

	public void MemorizzaAccessoSI(String user, String pass){
		dbd = new DroidiaryDatabaseHelper(this); //collegamento database
		db=dbd.getWritableDatabase();
		dbd.openDataBase();
		int i=Account.MemorizzaAccessoSI(db, user, pass);
		if(i==-1){
			System.out.println("Problema con la query");
		}
		db.close();
		dbd.close();
	}

	public void MemorizzaAccessoNO(String user, String pass){
		dbd = new DroidiaryDatabaseHelper(this); //collegamento database
		db=dbd.getWritableDatabase();
		dbd.openDataBase();
		int i=Account.MemorizzaAccessoNO(db, user, pass);
		if(i==-1){
			System.out.println("Problema con la query");
		}
		db.close();
		dbd.close();
	}

	public boolean checkMemorizzaAccesso(){
		boolean ris=false;
		db=dbd.getReadableDatabase();
		dbd.openDataBase();
		Cursor res=Account.getMemorizzaAccesso(db);
		if(res.moveToFirst()){
			ris=true;
		}else{
			ris=false;
		}
		res.close();
		dbd.close();
		return ris;
	}



	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.menu_about:intent = new Intent(DroidiaryActivity.this, AboutActivity.class);
		dbd.close();
		startActivity(intent);
		return true;
		case R.id.menu_setup:intent= new Intent(DroidiaryActivity.this, NuovoAccountActivity.class);
		dbd.close();
		startActivity(intent);
		return true;
		}
		return true;
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
	RadioButton online;
	RadioButton offline;
	CheckBox access;
}