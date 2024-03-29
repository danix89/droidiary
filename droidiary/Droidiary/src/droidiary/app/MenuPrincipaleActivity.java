package droidiary.app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import doirdiary.db.sync.AccountSync;
import droidiary.db.Account;
import droidiary.db.Appuntamento;
import droidiary.db.Contatto;
import droidiary.db.DroidiaryDatabaseHelper;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MenuPrincipaleActivity extends Activity {
	/** Called when the activity is first created. */

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menuprincipale);

		dbd = new DroidiaryDatabaseHelper(this); //collegamento database
		db=dbd.getWritableDatabase();
		try {
			dbd.openDataBase();
		}catch(SQLException sqle){

			throw sqle;

		}
		if(codUtente==0){
			codUtente = getIntent().getExtras().getInt("droidiary.app.DroidiaryActivity");
		}
		if(codUtente==0){
			codUtente = getIntent().getExtras().getInt("droidiary.app.MenuRubricaActivity");
		}
		if(codUtente==0){
			codUtente = getIntent().getExtras().getInt("droidiary.app.MenuAppuntamentiActivity");
		}
		if(codUtente==0){
			codUtente = getIntent().getExtras().getInt("droidiary.app.NuovoAccountActivity");
		}

		System.out.println("Parametro Passato Login:" + codUtente);

		//implementazione status

		ImageView stat = (ImageView) findViewById(R.id.status);
		int online = R.drawable.online;
		int offline = R.drawable.offline;
		status = getIntent().getStringExtra("Status");
		System.out.println("Status: "+status);
		if(status!=null){
			if(status.equals("true")){
				stat.setImageResource(online);
				Toast.makeText(getApplicationContext(),  "Sei Online! Sincronizza i tuoi contatti e appuntamenti!!", Toast.LENGTH_LONG).show();

			}
			if(status.equals("false")){
				stat.setImageResource(offline);
			}
		}

		TextView utente = (TextView) findViewById(R.id.Utente);
		if(status.equals("true")){
			String res=AccountSync.getContattoAccountById(codUtente);
			try {
				JSONArray jArray = new JSONArray(res);
				for(int i=0;i<jArray.length();i++){
					JSONObject json_data = jArray.getJSONObject(i);
					String nome = json_data.getString("nome");
					String cognome = json_data.getString("cognome");
					utente.setText("Utente: " + nome + " " + cognome);
				}
			} catch (JSONException e) {

				e.printStackTrace();
			}

		}



		if(status.equals("false")){
			Cursor c= Account.getAccountById(db, codUtente);
			while(c.moveToNext()){
				String nome=c.getString(0);
				String cognome=c.getString(1);
				utente.setText("Utente: " + nome + " " + cognome);
				dbd.close();
			}
			dbd.close();
		}
		dbd = new DroidiaryDatabaseHelper(this); //collegamento database
		db2=dbd.getWritableDatabase();
		try {
			dbd.openDataBase();
		}catch(SQLException sqle){

			throw sqle;

		}

		// visualizzazione notifiche appuntamenti


		lv=(ListView) findViewById(R.id.listanotifiche);
		Cursor appuntamenti= Appuntamento.getAppuntamentiToday(db2, codUtente);
		listview_array=getOneColumn(appuntamenti);

		lv.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, listview_array){
			public View getView(int position, View convertView,
					ViewGroup parent) {
				View view =super.getView(position, convertView, parent);
				TextView textView=(TextView) view.findViewById(android.R.id.text1);
				//colore degli item
				textView.setTextColor(Color.BLACK);
				return view;
			}
		});

		//listener per la lista notifiche
		lv.setOnItemClickListener(
				new OnItemClickListener()
				{
					public void onItemClick(AdapterView<?> arg0, View view,int position, long id) {
						Object o = lv.getItemAtPosition(position);
						String appuntamento = o.toString();
						Intent intent = new Intent(MenuPrincipaleActivity.this, MenuVisualizzaAppuntamentoActivity.class);
						intent.putExtra("droidiary.app.MenuAppuntamentiActivity.codUtente", codUtente);
						intent.putExtra("droidiary.app.MenuAppuntamentiActivity.appuntamento", appuntamento);
						intent.putExtra("Status", status);
						dbd.close();
						startActivity(intent);
					}   
				});


		Button rubrica = (Button) findViewById(R.id.rubrica);
		rubrica.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View arg0) {
				Intent intent = new Intent(MenuPrincipaleActivity.this, MenuRubricaActivity.class);
				intent.putExtra("droidiary.app.MenuPrincipaleActivity", codUtente);
				intent.putExtra("Status", status);
				dbd.close();
				startActivity(intent);
			}
		}
				);

		Button appuntamento = (Button) findViewById(R.id.appuntamenti);
		appuntamento.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View arg0) {
				Intent intent = new Intent(MenuPrincipaleActivity.this, MenuAppuntamentiActivity.class);
				intent.putExtra("droidiary.app.MenuPrincipaleActivity", codUtente);
				intent.putExtra("Status", status);
				dbd.close();
				db.close();
				startActivity(intent);
			}
		}
				);


		dbd.close();

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



	public void onBackPressed(){
		Intent intent = new Intent(MenuPrincipaleActivity.this, DroidiaryActivity.class);
		Toast.makeText(getApplicationContext(),  "Non sei piu' loggato!", Toast.LENGTH_LONG).show();
		startActivity(intent);
	}

	//tutto il risultato del cursore in un array
	private String[] getOneColumn(Cursor cursor){ 
		String myTitle = "";
		String[] myArray = null;            
		startManagingCursor(cursor);
		while(cursor.moveToNext()){
			myTitle+=cursor.getString(cursor.getColumnIndex(Appuntamento.DESCRIZIONE))+";";              
		}   
		myArray = myTitle.split(";");     
		return myArray;
	}

	private DroidiaryDatabaseHelper dbd;
	private SQLiteDatabase db, db2;
	private ListView lv;
	private String listview_array[];
	private int codUtente;
	String status;
	private ProgressDialog progressDialog;

}