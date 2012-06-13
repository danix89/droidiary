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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import doirdiary.db.sync.AppuntamentoSync;
import droidiary.db.Appuntamento;
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
		
		codUtente = getIntent().getExtras().getInt("droidiary.app.MenuAppuntamentiActivity.codUtente");
		
		System.out.println("Parametro appuntamento: "+appuntamento);

		dbd = new DroidiaryDatabaseHelper(this); //collegamento database
		db=dbd.getWritableDatabase();
		try {
			dbd.openDataBase();
		}catch(SQLException sqle){

			throw sqle;

		}

		Cursor result=Appuntamento.getDatiFromString(db, codUtente, appuntamento);

		if(result.moveToFirst()){
			id=result.getInt(0);
			System.out.println("Codice id: " + id);
			System.out.println("Codice Account: " + codUtente);
			TextView utente=(TextView) findViewById(R.id.Utente);
			utente.setText("Dettagli Appuntamento");
			
			TextView descr = (TextView)findViewById(R.id.descrizioneappuntamento);
			descr.setText(appuntamento);
			TextView indirizzo  = (TextView)findViewById(R.id.indirizzoappuntamento);
			indirizzo.setText(result.getString(3));
			TextView luogo = (TextView)findViewById(R.id.luogoappuntamento);
			luogo.setText(result.getString(4));
			TextView data = (TextView)findViewById(R.id.dataappuntamento);
			data.setText(result.getString(5));
			TextView ora = (TextView)findViewById(R.id.oraappuntamento);
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
				dbd.close();
				db.close();
				startActivity(intent);
			}
		});
		
		
		
		Button eliminaAppuntamento=(Button)findViewById(R.id.eliminaappuntamento);
		eliminaAppuntamento.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				onClickElimina();
				
			}
		});
		
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
	private DroidiaryDatabaseHelper dbd, tmp2;
	private SQLiteDatabase db;
	private int codUtente;
	private int id;
	private String appuntamento;
	String status;
}