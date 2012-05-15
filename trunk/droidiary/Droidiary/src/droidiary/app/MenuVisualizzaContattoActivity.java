package droidiary.app;

import droidiary.db.Contatto;
import droidiary.db.DroidiaryDatabaseHelper;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.NumberKeyListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuVisualizzaContattoActivity extends Activity {
	/** Called when the activity is first created. */

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menuvisualizzacontatto);

		contatto = getIntent().getExtras().getString("droidiary.app.MenuRubricaActivity");

		System.out.println("Parametro Contatto: "+contatto);

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
			codUtente=result.getString(0);
			System.out.println("Codice Account: " + codUtente);
			TextView utente=(TextView) findViewById(R.id.Utente);
			utente.setText("Contatto: "+result.getString(1) + " " + result.getString(2));
			
			TextView nome= (TextView)findViewById(R.id.nomecontatto);
			nome.setText(result.getString(1));
			nome.setKeyListener(new NumberKeyListener() {
			    public int getInputType() {
			        return InputType.TYPE_NULL;
			    }

			    protected char[] getAcceptedChars() {
			        return new char[] {};
			    }
			});
			TextView cognome= (TextView)findViewById(R.id.cognomecontatto);
			cognome.setText(result.getString(2));
			cellulare= (TextView)findViewById(R.id.telefonocellularecontatto);
			cellulare.setText(result.getString(4));
			casa= (TextView)findViewById(R.id.telefonocasacontatto);
			casa.setText(result.getString(5));
			TextView citta= (TextView)findViewById(R.id.cittacontatto);
			citta.setText(result.getString(3));
			TextView email= (TextView)findViewById(R.id.emailcontatto);
			email.setText(result.getString(6));
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
				Intent intent = new Intent(MenuVisualizzaContattoActivity.this, ModificaContattoActivity.class);
				intent.putExtra("droidiary.app.MenuVisualizzaContattoActivity", contatto);
				dbd.close();
				startActivity(intent);
			}
		});
	}

	private DroidiaryDatabaseHelper dbd;
	private SQLiteDatabase db;
	private String codUtente;
	private String contatto;
	private TextView casa, cellulare;
}