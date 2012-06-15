package droidiary.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import doirdiary.db.sync.AccountSync;
import doirdiary.db.sync.ContattoSync;
import droidiary.db.Account;
import droidiary.db.Contatto;
import droidiary.db.DroidiaryDatabaseHelper;

public class NuovoAccountActivity extends Activity{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menunuovoaccount);

		Button salva=(Button)findViewById(R.id.salva);
		salva.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText txtnome = (EditText)findViewById(R.id.nomeaccount);
				nome = txtnome.getText().toString();
				EditText txtcognome = (EditText)findViewById(R.id.cognomeaccount);
				cognome = txtcognome.getText().toString();
				EditText txtcasa = (EditText)findViewById(R.id.telefonocasaaccount);
				telefonoCasa = txtcasa.getText().toString();
				EditText txtcellulare = (EditText) findViewById(R.id.telefonocellulareaccount);
				cellulare= txtcellulare.getText().toString();
				EditText txtuser= (EditText) findViewById(R.id.useraccount);
				user=txtuser.getText().toString();
				EditText txtpsw= (EditText) findViewById(R.id.passwordaccount);
				psw=txtpsw.getText().toString();
				if(nome.equals("") && cognome.equals("") && user.equals("") && psw.equals("")){
					Toast.makeText(getApplicationContext(),  "Controlla i campi Nome, Cognome, User e Password", Toast.LENGTH_LONG).show();
				}else if(telefonoCasa.equals("") && cellulare.equals("")){
					Toast.makeText(getApplicationContext(),  "Inserire almeno un Recapito Telefonico", Toast.LENGTH_LONG).show();
				}else{ 
					onClickSalva();
				}

			}
		}); 

		Button cancella=(Button)findViewById(R.id.cancella);
		cancella.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				EditText txtnome = (EditText)findViewById(R.id.nomeaccount);
				txtnome.setText("");
				EditText txtcognome = (EditText)findViewById(R.id.cognomeaccount);
				txtcognome.setText("");
				EditText txtcasa = (EditText)findViewById(R.id.telefonocasaaccount);
				txtcasa.setText("");
				EditText txtcellulare = (EditText) findViewById(R.id.telefonocellulareaccount);
				txtcellulare.setText("");
				EditText txtuser= (EditText) findViewById(R.id.username);
				txtuser.setText("");
				EditText txtpsw= (EditText) findViewById(R.id.password);
				txtpsw.setText("");
			}
		});

	} 

	public void onClickSalva() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Vuoi Salvare il Contatto?")
		.setCancelable(false)
		.setPositiveButton("Si", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				inserisci();
			}
		})
		.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();

	}



	public void inserisci(){

		dbd = new DroidiaryDatabaseHelper(this);
		db=dbd.getWritableDatabase();
		try {
			dbd.openDataBase();
		}catch(SQLException sqle){

			throw sqle;

		}


		String res=AccountSync.getStringAccountByUserPsw(user, psw);
		String cnt=AccountSync.countAccount();
		try {
			JSONArray jArray = new JSONArray(cnt);
			for(int i=0;i<jArray.length();i++){
				JSONObject json_data = jArray.getJSONObject(i);
				count =(json_data.getInt("COUNT( _id )"))+1;
				System.out.println("Count: " +count);
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}

		Cursor query=Account.getAccountByUserPsw(db, user, psw);

		if(res.contains("null") || query.moveToFirst()){
			db=dbd.getWritableDatabase();
			db2=dbd.getWritableDatabase();
			db3=dbd.getWritableDatabase();
			long res1=Account.insertAccount(db, count, user, psw);
			if(res1 == -1){
				Toast.makeText(getApplicationContext(),  "Problema con la query Insert Account", Toast.LENGTH_LONG).show();
			}else{
				AccountSync.insertAccount(count, user, psw);
			}
			long res2=Contatto.insertContattoAccount(db2, count, count, nome, cognome, "", cellulare, telefonoCasa, "");
			if(res2 == -1){
				Toast.makeText(getApplicationContext(),  "Problema con la query Insert Contatto", Toast.LENGTH_LONG).show();
			}else{
				ContattoSync.insertContattoAccount(count, count, nome, cognome, "", cellulare, telefonoCasa, "");
				Toast.makeText(getApplicationContext(),  "Contatto Salvato Correttamente", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(NuovoAccountActivity.this, DroidiaryActivity.class);
				System.out.println("Codice da Passare "+count);
				intent.putExtra("droidiary.app.NuovoAccountActivity", count);
				intent.putExtra("Status", "true");
				startActivity(intent);
			}
		}else{
			Toast.makeText(getApplicationContext(),  "Account gia' esistente", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(NuovoAccountActivity.this, DroidiaryActivity.class);
			startActivity(intent);
		}
	}


	private DroidiaryDatabaseHelper dbd;
	private SQLiteDatabase db, db2, db3;
	int codUtente;
	int count;
	String nome, cognome, telefonoCasa, user, cellulare, psw;
}
