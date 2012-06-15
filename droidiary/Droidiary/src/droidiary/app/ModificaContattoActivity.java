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
		nome1= (EditText)findViewById(R.id.nomecontatto);
		cognome1= (EditText)findViewById(R.id.cognomecontatto);
		cellulare= (EditText)findViewById(R.id.telefonocellularecontatto);;
		casa1= (EditText)findViewById(R.id.telefonocasacontatto);
		citta1= (EditText)findViewById(R.id.cittacontatto);

		dbd = new DroidiaryDatabaseHelper(this); //collegamento database
		db=dbd.getWritableDatabase();
		try {
			dbd.openDataBase();
		}catch(SQLException sqle){

			throw sqle;
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

		if(status.equals("true")){
			String res=ContattoSync.getDatiFromString(contatto);
			try {
				JSONArray jArray = new JSONArray(res);
				for(int i=0;i<jArray.length();i++){
					JSONObject json_data = jArray.getJSONObject(i);
					nome1.setText(json_data.getString("nome"));
					cognome1.setText(json_data.getString("cognome"));
					casa1.setText(json_data.getString("numeroCasa"));
					citta1.setText(json_data.getString("citta"));
					cellulare.setText(json_data.getString("cellulare"));
					email1.setText(json_data.getString("email"));
				}
			} catch (JSONException e) {

				e.printStackTrace();
			}
		}

		if(status.equals("false")){
			Cursor result=Contatto.getDatiFromString(db, contatto);

			if(result.moveToFirst()){
				TextView utente=(TextView) findViewById(R.id.Contatto);
				utente.setText("Modifica Contatto");
				nome1= (EditText)findViewById(R.id.nomecontatto);
				nome1.setText(result.getString(2));
				cognome1= (EditText)findViewById(R.id.cognomecontatto);
				cognome1.setText(result.getString(3));
				cellulare= (EditText)findViewById(R.id.telefonocellularecontatto);
				cellulare.setText(result.getString(5));
				casa1= (EditText)findViewById(R.id.telefonocasacontatto);
				casa1.setText(result.getString(6));
				citta1= (EditText)findViewById(R.id.cittacontatto);
				citta1.setText(result.getString(4));
				email1= (EditText)findViewById(R.id.emailcontatto);
				email1.setText(result.getString(7));
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
		EditText cellulare, nome1, cognome1, casa1, citta1, email1;
		String status;
	}