package droidiary.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import droidiary.db.Contatto;
import droidiary.db.DroidiaryDatabaseHelper;

public class ModificaContattoActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menumodificacontatto);

		contatto=getIntent().getExtras().getString("droidiary.app.contatto");
		id=getIntent().getExtras().getInt("droidiary.app.codUtente");
		
		System.out.println("Parametro contatto Modifica Contatto:"+contatto);
		
		dbd = new DroidiaryDatabaseHelper(this); //collegamento database
		db=dbd.getWritableDatabase();
		try {
			dbd.openDataBase();
		}catch(SQLException sqle){

			throw sqle;
		}
		

		//id_account, nome, cognome, citta, cellulare, numeroCasa, email
		Cursor result=Contatto.getDatiFromString(db, contatto);

		if(result.moveToFirst()){
			codUtente=result.getInt(1);
			System.out.println("Parametro id_account Modifica Contatto:"+codUtente);
			TextView utente=(TextView) findViewById(R.id.Contatto);
			utente.setText("Modifica Contatto");
			
			EditText nome= (EditText)findViewById(R.id.nomecontatto);
			nome.setText(result.getString(2));
			EditText cognome= (EditText)findViewById(R.id.cognomecontatto);
			cognome.setText(result.getString(3));
			cellulare= (EditText)findViewById(R.id.telefonocellularecontatto);
			cellulare.setText(result.getString(5));
			casa= (EditText)findViewById(R.id.telefonocasacontatto);
			casa.setText(result.getString(6));
			EditText citta= (EditText)findViewById(R.id.cittacontatto);
			citta.setText(result.getString(4));
			EditText email= (EditText)findViewById(R.id.emailcontatto);
			email.setText(result.getString(7));
			dbd.close();
		}
		
		ImageView img = (ImageView) findViewById(R.id.chiamatacasa);
		img.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	Intent dialIntent=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+(casa.getText()).toString()));
		    	startActivity(dialIntent);
		    }
		});
		
		ImageView img1 = (ImageView) findViewById(R.id.chiamatacellulare);
		img1.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	Intent dialIntent=new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+(cellulare.getText()).toString()));
		    	startActivity(dialIntent);
		    }
		});
		

		Button salva=(Button)findViewById(R.id.salvaContatto);
        salva.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				 EditText txtnome = (EditText)findViewById(R.id.nomecontatto);
			     nome = txtnome.getText().toString();
			     EditText txtcognome = (EditText)findViewById(R.id.cognomecontatto);
			     cognome = txtcognome.getText().toString();
			     EditText txtcasa = (EditText)findViewById(R.id.telefonocasacontatto);
			     telefonoCasa = txtcasa.getText().toString();
			     EditText txtcellulare = (EditText) findViewById(R.id.telefonocellularecontatto);
			     telefonocellulare= txtcellulare.getText().toString();
			     EditText txtmail= (EditText) findViewById(R.id.emailcontatto);
			     email=txtmail.getText().toString();
			     EditText txtcitta= (EditText) findViewById(R.id.cittacontatto);
			     citta=txtcitta.getText().toString();
			     if(nome.equals("Nome:") || cognome.equals("Cognome:") || telefonoCasa.equals("Casa:") || telefonocellulare.equals("Cellulare:")|| email.equals("Email:") || citta.equals("Citta:")){
			    	Toast.makeText(getApplicationContext(),  "Controlla tutti i campi", Toast.LENGTH_LONG).show();
			     }else{
			    	 onClickAggiorna();
			    
			     }
				
			}
		}); 
        
       

    }   
    
    public void onClickAggiorna() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Vuoi Aggionare il Contatto?")
               .setCancelable(false)
               .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                        aggiornaContatto();
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
    
    
    
    public void aggiornaContatto(){
    	tmp = new DroidiaryDatabaseHelper(this);
		db=tmp.getWritableDatabase();
		tmp.openDataBase();
		System.out.println("Id_Account: "+codUtente+" Nome: " + nome + " Cognome:" + cognome + "Citta: " + citta+ "Cellulare: " + telefonocellulare + "Casa: " + telefonoCasa + "Email: " + email);
		//Cursor res= Contatto.modificaContatto(db, contatto, codUtente, nome, cognome, citta, telefonocellulare, telefonoCasa, email);
		int res= Contatto.modificaContatto(db, id, codUtente, nome, cognome, citta, telefonocellulare, telefonoCasa, email);
		
		if(res>0){
			Toast.makeText(getApplicationContext(),  "Salvataggio Effettuato con Successo!", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(ModificaContattoActivity.this, MenuRubricaActivity.class);
			System.out.println(codUtente);
			intent.putExtra("droidiary.app.ModificaContattoActivity", codUtente);
			startActivity(intent);
			tmp.close();
		}
		
    }

	private DroidiaryDatabaseHelper dbd, tmp;
	private SQLiteDatabase db;
	private int id;
	private int codUtente;
	private String contatto;
	private EditText casa;
	private String nome, cognome, telefonoCasa, citta, email, telefonocellulare;
	private EditText cellulare;
}