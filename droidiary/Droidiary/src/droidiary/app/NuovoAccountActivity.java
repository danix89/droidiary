package droidiary.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import droidiary.db.Account;
import droidiary.db.Contatto;
import droidiary.db.DroidiaryDatabaseHelper;

public class NuovoAccountActivity extends Activity{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menunuovoaccount);

		
		dbd = new DroidiaryDatabaseHelper(this); //collegamento database
		db=dbd.getWritableDatabase();
		try {
			dbd.openDataBase();
		}catch(SQLException sqle){

			throw sqle;
		}

		
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
					dbd.close();
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
				Cursor c= inserisciAccount();
				inserisciContatto(c);
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

	public Cursor inserisciAccount(){
		temp1 = new DroidiaryDatabaseHelper(this);
		db=temp1.getWritableDatabase();
		try {
			temp1.openDataBase();
		}catch(SQLException sqle){

			throw sqle;

		}

		long res = Account.insertAccount(db, user, psw);


		if(res == -1){
			Toast.makeText(getApplicationContext(),  "Problema con la query", Toast.LENGTH_LONG).show();
		}
		else{
			Toast.makeText(getApplicationContext(),  "Account Salvato Correttamente", Toast.LENGTH_LONG).show();
			String[] arg={user, psw};
			Cursor c= Account.getAccountByUserPsw(db, arg);
			
			
			if(c.moveToFirst()){
				codUtente= c.getInt(0);
				System.out.print(codUtente);
			}
			
			temp1.close();
			return c;
		}

		return null;
	}

	public void inserisciContatto(Cursor c){
		temp2 = new DroidiaryDatabaseHelper(this);
		db=temp2.getWritableDatabase();
		try {
			temp2.openDataBase();
		}catch(SQLException sqle){

			throw sqle;

		}

		
		
		long res = Contatto.insertContatto(db, codUtente, nome, cognome, "", cellulare, telefonoCasa, "");
		if(res == -1){
			Toast.makeText(getApplicationContext(),  "Problema con la query", Toast.LENGTH_LONG).show();
		}
		else{
			Toast.makeText(getApplicationContext(),  "Contatto Salvato Correttamente", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(NuovoAccountActivity.this, DroidiaryActivity.class);
			System.out.println("Codice da Passare"+codUtente);
			intent.putExtra("droidiary.app.NuovoAccountActivity", codUtente);
			temp2.close();
			startActivity(intent);
		}

	}


	private DroidiaryDatabaseHelper dbd, temp1, temp2;
	private SQLiteDatabase db;
    int codUtente;
	String nome, cognome, telefonoCasa, user, cellulare, psw;
}
