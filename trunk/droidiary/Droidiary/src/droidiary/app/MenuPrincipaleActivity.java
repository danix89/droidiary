package droidiary.app;

import droidiary.db.Account;
import droidiary.db.DroidiaryDatabaseHelper;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
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
			}
			if(status.equals("false")){
				stat.setImageResource(offline);
			}
		}

	}



	public void onBackPressed(){
		Intent intent = new Intent(MenuPrincipaleActivity.this, DroidiaryActivity.class);
		Toast.makeText(getApplicationContext(),  "Non sei più loggato!", Toast.LENGTH_LONG).show();
		startActivity(intent);
	}

	private DroidiaryDatabaseHelper dbd;
	private SQLiteDatabase db;
	private int codUtente;
	String status;
}

