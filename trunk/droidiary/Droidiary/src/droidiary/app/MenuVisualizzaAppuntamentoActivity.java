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
import doirdiary.db.sync.AppuntamentoSync;
import doirdiary.db.sync.ContattoSync;
import droidiary.db.Account;
import droidiary.db.Appuntamento;
import droidiary.db.Contatto;
import droidiary.db.DroidiaryDatabaseHelper;

public class MenuVisualizzaAppuntamentoActivity extends Activity {
	/** Called when the activity is first created. */

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menuvisualizzappuntamento);
		if(appuntamento==null){
			appuntamento = getIntent().getExtras().getString("droidiary.app.MenuAppuntamentiActivity.appuntamento");
		}
		if(appuntamento==null){
			appuntamento = getIntent().getExtras().getString("droidiary.app.ModificaAppuntamentoActivity.codUtente");
		}


		TextView utente = (TextView) findViewById(R.id.Utente);
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

		if(status.equals("true")){
			String res=AccountSync.getContattoAccountById(codUtente);
			try {
				JSONArray jArray = new JSONArray(res);
				for(int i=0;i<jArray.length();i++){
					JSONObject json_data = jArray.getJSONObject(i);
					String nome = json_data.getString("nome");
					String cognome = json_data.getString("cognome");
					utente.setText("Benvenuto, " + nome + " " + cognome);
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
			Cursor res= Account.getAccountById(db, codUtente);
			while(res.moveToNext()){
				String nome=res.getString(0);
				String cognome=res.getString(1);
				utente.setText("Benvenuto, " + nome + " " + cognome);
				dbd.close();
			}
			dbd.close();
		}

		status = getIntent().getStringExtra("Status");
		System.out.println("Status Visualizza Contatto: "+status);
		
		codUtente = getIntent().getExtras().getInt("droidiary.app.MenuAppuntamentiActivity.codUtente");

		System.out.println("Parametro appuntamento: "+appuntamento);

		descr = (TextView)findViewById(R.id.descrizioneappuntamento);
		indirizzo  = (TextView)findViewById(R.id.indirizzoappuntamento);
		luogo = (TextView)findViewById(R.id.luogoappuntamento);
		data = (TextView)findViewById(R.id.dataappuntamento);
		ora = (TextView)findViewById(R.id.oraappuntamento);

		dbd = new DroidiaryDatabaseHelper(this); //collegamento database
		db=dbd.getWritableDatabase();
		try {
			dbd.openDataBase();
		}catch(SQLException sqle){

			throw sqle;

		}

		if(status.equals("true")){
			String res=AppuntamentoSync.getDatiFromString(codUtente, appuntamento);
			try {
				JSONArray jArray = new JSONArray(res);
				for(int i=0;i<jArray.length();i++){
					JSONObject json_data = jArray.getJSONObject(i);
					id=json_data.getInt("_id");
					codUtente = json_data.getInt("id_account");
					descr.setText(json_data.getString("descrizione"));
					indirizzo.setText(json_data.getString("indirizzo"));
					luogo.setText(json_data.getString("luogo"));
					data.setText(json_data.getString("data"));
					ora.setText(json_data.getString("ora"));
				}
			} catch (JSONException e) {

				e.printStackTrace();
			}
		}

		Cursor result=Appuntamento.getDatiFromString(db, codUtente, appuntamento);

		if(result.moveToFirst()){
			id=result.getInt(0);
			System.out.println("Codice id: " + id);
			System.out.println("Codice Account: " + codUtente);
			descr = (TextView)findViewById(R.id.descrizioneappuntamento);
			descr.setText(appuntamento);
			indirizzo  = (TextView)findViewById(R.id.indirizzoappuntamento);
			indirizzo.setText(result.getString(3));
			luogo = (TextView)findViewById(R.id.luogoappuntamento);
			luogo.setText(result.getString(4));
			data = (TextView)findViewById(R.id.dataappuntamento);
			data.setText(result.getString(5));
			ora = (TextView)findViewById(R.id.oraappuntamento);
			ora.setText(result.getString(6));

		}

		ImageView img = (ImageView) findViewById(R.id.chiamatacasa);
		img.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent dialIntent=new Intent(MenuVisualizzaAppuntamentoActivity.this, VisualizzaMappaActivity.class);
				startActivity(dialIntent);
			}
		});

		Button modifica=(Button) findViewById(R.id.modificaappuntamento);
		modifica.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
					Intent intent = new Intent(MenuVisualizzaAppuntamentoActivity.this, ModificaAppuntamentoActivity.class);
					intent.putExtra("droidiary.app.MenuVisualizzaAppuntamentoActivity.id", id);
					intent.putExtra("droidiary.app.MenuVisualizzaAppuntamentoActivity.codUtente", codUtente);
					intent.putExtra("Status", status);
					startActivity(intent);
			}
		});



		Button eliminaAppuntamento=(Button)findViewById(R.id.eliminaappuntamento);
		eliminaAppuntamento.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				onClickElimina();

			}
		});
	}

	public void onClickElimina() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Vuoi Eliminare l'Appuntamento?")
		.setCancelable(false)
		.setPositiveButton("Si", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				eliminaAppuntamento();
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
		Intent intent = new Intent(MenuVisualizzaAppuntamentoActivity.this, MenuAppuntamentiActivity.class);
		intent.putExtra("droidiary.app.MenuVisualizzaAppuntamentoActivity.codUtente", codUtente);
		intent.putExtra("Status", status);
		startActivity(intent);
	}
	public void eliminaAppuntamento(){

		if(status.equals("true")){
			AppuntamentoSync.eliminaAppuntamento(id, codUtente);
		}
		tmp2 = new DroidiaryDatabaseHelper(this);
		db=tmp2.getWritableDatabase();
		tmp2.openDataBase();
		int res=Appuntamento.eliminaAppuntamento(db, id, codUtente);
		if(res>0){
			Toast.makeText(getApplicationContext(),  "Appuntamento Elminato con Successo!", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(MenuVisualizzaAppuntamentoActivity.this, MenuAppuntamentiActivity.class);
			System.out.println(codUtente);
			intent.putExtra("droidiary.app.MenuVisualizzaAppuntamentoActivity.codUtente", codUtente);
			intent.putExtra("Status", status);
			startActivity(intent);
			tmp2.close();
		}
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
	private DroidiaryDatabaseHelper dbd, tmp2;
	private SQLiteDatabase db;
	private int codUtente;
	private int id;
	private TextView descr, indirizzo, luogo, data, ora;
	private String appuntamento;
	String status;
	private ProgressDialog progressDialog;

}