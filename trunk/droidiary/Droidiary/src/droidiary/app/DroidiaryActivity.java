package droidiary.app;

import java.io.IOException;

import droidiary.db.Account;
import droidiary.db.DroidiaryDatabaseHelper;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
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

		try {
			dbd.openDataBase();
		}catch(SQLException sqle){

			throw sqle;

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
								
				Cursor c= Account.getAccountByUserPsw(db, arg);
				if(c.moveToFirst()){
					Toast.makeText(getApplicationContext(), "Login effettuato con successo!", Toast.LENGTH_LONG).show();
					int codUtente= c.getInt(0);
					System.out.println(codUtente);
					Intent intent = new Intent(DroidiaryActivity.this, MenuPrincipaleActivity.class);
					intent.putExtra("droidiary.app.DroidiaryActivity", codUtente);
					startActivity(intent);
				}else{
					Toast.makeText(getApplicationContext(), "Dati non esatti", Toast.LENGTH_LONG).show();
				}
			}else if(online.isChecked()){
				Toast.makeText(getApplicationContext(), "Funzionalità online non implementata", Toast.LENGTH_LONG).show();
				
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
		
		
		ImageView img= (ImageView)findViewById(R.id.about);
		img.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	Intent intent = new Intent(DroidiaryActivity.this, AboutActivity.class);
		    	startActivity(intent);
		    }
		});
		
		
	}
	
	public void onBackPressed() {
	    // do something on back.
	    return;
	}
	private DroidiaryDatabaseHelper dbd;
	private SQLiteDatabase db;
}