package droidiary.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import doirdiary.db.sync.ContattoSync;
import droidiary.db.Contatto;
import droidiary.db.DroidiaryDatabaseHelper;

public class ModificaContattoActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menumodificacontatto);

		contatto=getIntent().getExtras().getString("droidiary.app.contatto");
		id_account=getIntent().getExtras().getInt("CodUtente");
		System.out.println("Codice ID account: " + id_account);
		id_contatto=getIntent().getExtras().getInt("ID");
		System.out.println("Codice ID contatto: " + id_contatto);
		status = getIntent().getStringExtra("Status");
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
		
		ImageView stat = (ImageView) findViewById(R.id.status);
		int online = R.drawable.online;
		int offline = R.drawable.offline;
		status = getIntent().getStringExtra("Status");
		System.out.println("Status: "+status);
		if(status!=null){
			if(status.equals("true")){
				stat.setImageResource(online);
			}
			if(status.equals("false")){
				stat.setImageResource(offline);
			}
		}

		status = getIntent().getStringExtra("Status");
		System.out.println("Status Modifica Contatto: "+status);

		
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
				if(nome.equals("") && cognome.equals("")){
					Toast.makeText(getApplicationContext(),  "Controlla i campi Nome, Cognome", Toast.LENGTH_LONG).show();
				}else if(telefonoCasa.equals("") && cellulare.equals("")){
					Toast.makeText(getApplicationContext(),  "Inserire almeno un Recapito Telefonico", Toast.LENGTH_LONG).show();
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


		if(status.equals("true")){
			ContattoSync.modificaContatto(id_contatto, id_account, nome, cognome, citta, telefonocellulare, telefonoCasa, email);
		}
		tmp = new DroidiaryDatabaseHelper(this);
		db=tmp.getWritableDatabase();
		tmp.openDataBase();
		System.out.println("Id_Contatto: "+id_contatto+" Nome: " + nome + " Cognome:" + cognome + "Citta: " + citta+ "Cellulare: " + telefonocellulare + "Casa: " + telefonoCasa + "Email: " + email);
		int res= Contatto.modificaContatto(db, id_contatto, id_account, nome, cognome, citta, telefonocellulare, telefonoCasa, email);

		if(res>0){
			Toast.makeText(getApplicationContext(),  "Salvataggio Effettuato con Successo!", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(ModificaContattoActivity.this, MenuRubricaActivity.class);
			System.out.println(id_account);
			intent.putExtra("droidiary.app.ModificaContattoActivity", id_account);
			intent.putExtra("Status", status);
			startActivity(intent);
			tmp.close();
		}
	}

	private DroidiaryDatabaseHelper dbd, tmp;
	private SQLiteDatabase db;
	int id_contatto, id_account;
	private String contatto;
	private EditText casa;
	private String nome, cognome, telefonoCasa, citta, email, telefonocellulare;
	private EditText cellulare;
	String status;
}