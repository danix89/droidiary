package droidiary.app;

import java.io.IOException;
import org.json.*;

import doirdiary.db.sync.AccountSync;
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
						String status="false";
						intent.putExtra("droidiary.app.DroidiaryActivity", codUtente);
						intent.putExtra("Status", status);
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

						startActivity(intent);
					}else{
						Toast.makeText(getApplicationContext(), "Dati non presenti", Toast.LENGTH_LONG).show();
					}
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
		Intent intent;
		switch (item.getItemId()) {
		case R.id.menu_about:intent = new Intent(DroidiaryActivity.this, AboutActivity.class);
		startActivity(intent);
		return true;
		case R.id.menu_setup:intent= new Intent(DroidiaryActivity.this, NuovoAccountActivity.class);
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
}