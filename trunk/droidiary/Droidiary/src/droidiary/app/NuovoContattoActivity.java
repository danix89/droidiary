package droidiary.app;

import java.io.IOException;

import droidiary.db.Account;
import droidiary.db.Contatto;
import droidiary.db.DroidiaryDatabaseHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NuovoContattoActivity extends Activity {
    /** Called when the activity is first created. */
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menunuovocontatto);
    
        codUtente = getIntent().getExtras().getInt("droidiary.app.MenuRubricaActivity");
        
        System.out.println("Parametro Menu Rubrica:"+codUtente);

		dbd = new DroidiaryDatabaseHelper(this); //collegamento database
		db=dbd.getWritableDatabase();
		try {
			dbd.openDataBase();
		}catch(SQLException sqle){

			throw sqle;

		}
        
		Cursor c= Account.getAccountById(db, codUtente);
		TextView utente = (TextView) findViewById(R.id.Utente);
		System.out.println("Codice:" + codUtente);
		while(c.moveToNext()){
			String nome=c.getString(0);
			String cognome=c.getString(1);
			utente.setText("Utente: " + nome + " " + cognome);
		}
		
        Button salva=(Button)findViewById(R.id.salva);
        salva.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				 EditText txtnome = (EditText)findViewById(R.id.nomecontatto);
			     nome = txtnome.getText().toString();
			     EditText txtcognome = (EditText)findViewById(R.id.cognomecontatto);
			     cognome = txtcognome.getText().toString();
			     EditText txtcasa = (EditText)findViewById(R.id.telefonocasacontatto);
			     telefonoCasa = txtcasa.getText().toString();
			     EditText txtcellulare = (EditText) findViewById(R.id.telefonocellularecontatto);
			     cellulare= txtcellulare.getText().toString();
			     EditText txtmail= (EditText) findViewById(R.id.emailcontatto);
			     email=txtmail.getText().toString();
			     EditText txtcitta= (EditText) findViewById(R.id.cittacontatto);
			     citta=txtcitta.getText().toString();
			     if(nome.equals("Nome:") || cognome.equals("Cognome:") || telefonoCasa.equals("Casa:") || cellulare.equals("Cellulare:")|| email.equals("Email:") || citta.equals("Citt�:")){
			    	Toast.makeText(getApplicationContext(),  "Controlla tutti i campi", Toast.LENGTH_LONG).show();
			     }else{ 
			    	 dbd.close();
			    	 onClickSalva();
			    
			     }
				
			}
		}); 
        
       

    }   
    
    public void onClickSalva() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Vuoi Salvare il Contatto?")
               .setCancelable(false)
               .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                        inserisciContatto();
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
    
    
    //problema con la query
    
    public void inserisciContatto(){
		dbd = new DroidiaryDatabaseHelper(this); //collegamento database
		db=dbd.getWritableDatabase();
		try {
			dbd.openDataBase();
		}catch(SQLException sqle){

			throw sqle;

		}
        
		Cursor res=Contatto.insertContatto(db, codUtente, nome, cognome, citta, cellulare, telefonoCasa, email);
		if(res==null){
			Toast.makeText(getApplicationContext(),  "Problema con la query", Toast.LENGTH_LONG).show();
		}
		if (res.moveToFirst()){
			Toast.makeText(getApplicationContext(),  "Contatto Salvato Correttamente", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(NuovoContattoActivity.this, MenuRubricaActivity.class);
			intent.putExtra("droidiary.app.NuovoContattoActivity", codUtente);
			dbd.close();
			startActivity(intent);
			
		}
    	
    }
    
    private DroidiaryDatabaseHelper dbd;
	private SQLiteDatabase db;
	private int codUtente;
	String nome, cognome, telefonoCasa, email, cellulare, citta;
}