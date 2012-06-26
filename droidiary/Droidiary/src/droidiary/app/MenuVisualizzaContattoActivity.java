package droidiary.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import doirdiary.db.sync.AccountSync;
import doirdiary.db.sync.ContattoSync;
import droidiary.db.Account;
import droidiary.db.Appuntamento;
import droidiary.db.Contatto;
import droidiary.db.DroidiaryDatabaseHelper;

public class MenuVisualizzaContattoActivity extends Activity {
	/** Called when the activity is first created. */

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menuvisualizzacontatto);
		if(contatto==null){
			contatto = getIntent().getExtras().getString("droidiary.app.MenuRubricaActivity");
		}
		if(contatto==null){
			contatto = getIntent().getExtras().getString("droidiary.app.ModificaContattoActivity");
		}


		System.out.println("Parametro Contatto: "+contatto);

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

//		TextView utente = (TextView) findViewById(R.id.Utente);
//		if(status.equals("true")){
//			String res=AccountSync.getContattoAccountById(codUtente);
//			try {
//				JSONArray jArray = new JSONArray(res);
//				for(int i=0;i<jArray.length();i++){
//					JSONObject json_data = jArray.getJSONObject(i);
//					String nome = json_data.getString("nome");
//					String cognome = json_data.getString("cognome");
//					utente.setText("Benvenuto, " + nome + " " + cognome);
//				}
//			} catch (JSONException e) {
//
//				e.printStackTrace();
//			}
//
//		}
//
//		if(status.equals("false")){
//			dbd = new DroidiaryDatabaseHelper(this); //collegamento database
//			db=dbd.getWritableDatabase();
//			try {
//				dbd.openDataBase();
//			}catch(SQLException sqle){
//
//				throw sqle;
//
//			}
//			Cursor res= Account.getAccountById(db, codUtente);
//			while(res.moveToNext()){
//				String nome=res.getString(0);
//				String cognome=res.getString(1);
//				utente.setText("Benvenuto, " + nome + " " + cognome);
//				dbd.close();
//			}
//			dbd.close();
//		}

		status = getIntent().getStringExtra("Status");
		System.out.println("Status Visualizza Contatto: "+status);

		nome= (TextView)findViewById(R.id.nomecontatto);
		cognome= (TextView)findViewById(R.id.cognomecontatto);
		cellulare= (TextView)findViewById(R.id.telefonocellularecontatto);
		casa= (TextView)findViewById(R.id.telefonocasacontatto);
		citta= (TextView)findViewById(R.id.cittacontatto);
		email= (TextView)findViewById(R.id.emailcontatto);

		//id_account, nome, cognome, citta, cellulare, numeroCasa, email

		if(status.equals("true")){
			String res=ContattoSync.getDatiFromString(contatto);
			try {
				JSONArray jArray = new JSONArray(res);
				for(int i=0;i<jArray.length();i++){
					JSONObject json_data = jArray.getJSONObject(i);
					codUtente = json_data.getInt("id_account");
					nome.setText(json_data.getString("nome"));
					cognome.setText(json_data.getString("cognome"));
					casa.setText(json_data.getString("numeroCasa"));
					citta.setText(json_data.getString("citta"));
					cellulare.setText(json_data.getString("cellulare"));
					email.setText(json_data.getString("email"));

				}
			} catch (JSONException e) {

				e.printStackTrace();
			}
		}

		if(status.equals("false")){
			dbd = new DroidiaryDatabaseHelper(this); //collegamento database
			db=dbd.getWritableDatabase();
			try {
				dbd.openDataBase();
			}catch(SQLException sqle){

				throw sqle;

			}
			Cursor result=Contatto.getDatiFromString(db, contatto);

			if(result.moveToFirst()){
				codUtente=result.getInt(1);
				id=result.getInt(0);
				System.out.println("Codice id: " + id);
				codUtente=result.getInt(1);
				System.out.println("Codice Account: " + codUtente);
				nome= (TextView)findViewById(R.id.nomecontatto);
				nome.setText(result.getString(2));
				cognome= (TextView)findViewById(R.id.cognomecontatto);
				cognome.setText(result.getString(3));
				cellulare= (TextView)findViewById(R.id.telefonocellularecontatto);
				cellulare.setText(result.getString(5));
				casa= (TextView)findViewById(R.id.telefonocasacontatto);
				casa.setText(result.getString(6));
				citta= (TextView)findViewById(R.id.cittacontatto);
				citta.setText(result.getString(4));
				email= (TextView)findViewById(R.id.emailcontatto);
				email.setText(result.getString(7));
			}
			dbd.close();
		}



		if(codUtente==id){
			Button eliminaContatto=(Button) findViewById(R.id.eliminacontatto);
			eliminaContatto.setVisibility(View.INVISIBLE);
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

		Button modificaContatto=(Button) findViewById(R.id.modificacontatto);
		modificaContatto.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				if(status.equals("true")){
					Toast.makeText(getApplicationContext(),  "Per modificare devi sincronizzare i contatti", Toast.LENGTH_LONG).show();
				}
				if(status.equals("false")){		
					Intent intent = new Intent(MenuVisualizzaContattoActivity.this, ModificaContattoActivity.class);
					intent.putExtra("droidiary.app.contatto", contatto);
					intent.putExtra("ID", id);
					intent.putExtra("CodUtente", codUtente);
					intent.putExtra("Status", status);
					dbd.close();
					startActivity(intent);
				}
			}
		});



		Button eliminaContatto=(Button)findViewById(R.id.eliminacontatto);
		eliminaContatto.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if(status.equals("true")){
					Toast.makeText(getApplicationContext(),  "Per eliminare devi sincronizzare i contatti", Toast.LENGTH_LONG).show();
				}
				if(status.equals("false")){
					onClickElimina();
				}
			}
		});


	}

	//implementazione menu
	public boolean onCreateOptionsMenu(Menu menu)
	{
		if(status.equals("true")){
			getMenuInflater().inflate(R.menu.menusync, menu);
			return true;
		}else{
			System.out.println("Modalità offline non si sincronizza");
			return true;
		}
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_sync:runDialog(5); 
		return true;
		}
		return true;
	}

	private void runDialog(final int seconds)
	{
		progressDialog = ProgressDialog.show(this, "Attendere Prego....", "Sincronizzazione in corso");

		new Thread(new Runnable(){
			public void run(){
				int flag=sincronizza();
				if(flag==1){
					progressDialog.dismiss();
				}
			}
		}).start();
	}


	//metodo sincronizzazione

	public int sincronizza(){
		dbd = new DroidiaryDatabaseHelper(this); //collegamento database
		db=dbd.getWritableDatabase();
		try {
			dbd.openDataBase();
		}catch(SQLException sqle){

			throw sqle;

		}
		int flag=0;
		flag=Contatto.SincronizzaContatto(db, codUtente);
		flag=Appuntamento.SincronizzaAppuntamenti(db, codUtente);
		flag=Account.SincronizzaAccount(db, codUtente);
		dbd.close();
		return flag;

	}

	public void onClickElimina() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Vuoi Eliminare il Contatto?")
		.setCancelable(false)
		.setPositiveButton("Si", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				eliminaContatto();
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

	public void onBackPressed(){
		Intent intent = new Intent(MenuVisualizzaContattoActivity.this, MenuRubricaActivity.class);
		intent.putExtra("droidiary.app.MenuVisualizzaContatto", codUtente);
		intent.putExtra("Status", status);
		startActivity(intent);
	}
	public void eliminaContatto(){

		if(status.equals("true")){
			ContattoSync.eliminaContatto(id, codUtente);
		}
		tmp2 = new DroidiaryDatabaseHelper(this);
		db=tmp2.getWritableDatabase();
		tmp2.openDataBase();
		int res=Contatto.eliminaContatto(db, id, codUtente);
		if(res>0){
			Toast.makeText(getApplicationContext(),  "Contatto Elminato con Successo!", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(MenuVisualizzaContattoActivity.this, MenuRubricaActivity.class);
			System.out.println(codUtente);
			intent.putExtra("droidiary.app.ModificaContattoActivity", codUtente);
			intent.putExtra("Status", status);
			startActivity(intent);
			tmp2.close();
		}
	}
	private DroidiaryDatabaseHelper dbd, tmp2;
	private SQLiteDatabase db;
	private int codUtente;
	private int id;
	private String contatto;
	private TextView casa, cellulare, nome, cognome, citta, email;
	String status;
	private ProgressDialog progressDialog;

}