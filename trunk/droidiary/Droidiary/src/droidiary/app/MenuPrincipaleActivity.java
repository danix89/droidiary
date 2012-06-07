package droidiary.app;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

		Cursor c= Account.getAccountById(db, codUtente);
		TextView utente = (TextView) findViewById(R.id.Utente);

		while(c.moveToNext()){
			String nome=c.getString(0);
			String cognome=c.getString(1);
			utente.setText("Benvenuto, " + nome + " " + cognome);
			dbd.close();
		}


		dbd = new DroidiaryDatabaseHelper(this); //collegamento database
		db=dbd.getWritableDatabase();
		try {
			dbd.openDataBase();
		}catch(SQLException sqle){

			throw sqle;

		}


		// visualizzazione notifiche appuntamenti

		lv=(ListView) findViewById(R.id.listanotifiche);
		Cursor appuntamenti= Appuntamento.getAppuntamentiToday(db, codUtente);
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
		
		dbd.close();
		
	}
	
	//implementazione menu
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getMenuInflater().inflate(R.menu.menusync, menu);
		return true;
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
		int flag=0;
		flag=Contatto.SincronizzaContatto(db, codUtente);
		flag=Appuntamento.SincronizzaAppuntamenti(db, codUtente);
		return flag;
	}
	
	
	
	public void onBackPressed(){
		Intent intent = new Intent(MenuPrincipaleActivity.this, DroidiaryActivity.class);
		Toast.makeText(getApplicationContext(),  "Non sei più loggato!", Toast.LENGTH_LONG).show();
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
	private SQLiteDatabase db;
	private ListView lv;
	private String listview_array[];
	private int codUtente;
	String status;
	private ProgressDialog progressDialog;

}

