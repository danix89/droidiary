package droidiary.app;

import java.io.IOException;

import droidiary.db.Contatto;
import droidiary.db.DroidiaryDatabaseHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NuovoContattoActivity extends Activity {
    /** Called when the activity is first created. */
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menunuovocontatto);
        final Typeface mFont = Typeface.createFromAsset(getAssets(),"fonts/AidaSerifObliqueMedium.ttf"); 
        final ViewGroup mContainer = (ViewGroup) findViewById(android.R.id.content).getRootView();
        MenuRubricaActivity.setAppFont(mContainer, mFont);
    
        codUtente = getIntent().getExtras().getInt("droidiary.app.MenuRubricaActivity");
           
        Button salva=(Button)findViewById(R.id.salva);
        salva.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				 EditText txtnome = (EditText)findViewById(R.id.nomecontatto);
			     nome = txtnome.getText().toString();
			     EditText txtcognome = (EditText)findViewById(R.id.cognomecontatto);
			     cognome = txtcognome.getText().toString();
			     EditText txtcasa = (EditText)findViewById(R.id.telefonocasacontatto);
			     telefonoCasa = txtcasa.getText().toString();
			     EditText txtcellulare = (EditText) findViewById(R.id.telefonocellularecontatto);
			     cellulare= txtcellulare.getText().toString();
			     EditText txtmail= (EditText) findViewById(R.id.emailcontatto);
			     email=txtmail.getText().toString();
			     EditText txtcitta= (EditText) findViewById(R.id.cittacontatto);
			     citta=txtcitta.getText().toString();
			     if(nome.equals("Nome:") || cognome.equals("Cognome:") || telefonoCasa.equals("Casa:") || cellulare.equals("Cellulare:")|| email.equals("Email:") || citta.equals("Città:")){
			    	Toast.makeText(getApplicationContext(),  "Controlla tutti i campi", Toast.LENGTH_LONG).show();
			     }else{ 
			    	 onClickSalva();
			    
			     }
				
			}
		}); 
        
       

    }   
    
    public void onClickSalva() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Vuoi Salvare il Contatto?")
               .setCancelable(false)
               .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                        inserisciContatto();
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
    
    
    public void inserisciContatto(){
    	dbd = new DroidiaryDatabaseHelper(this);
		db=dbd.getWritableDatabase();
		try {
			dbd.createDataBase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}

		try {
			dbd.openDataBase();
		}catch(SQLException sqle){

			throw sqle;

		}
    	
		Cursor res=Contatto.insertContatto(db, codUtente, nome, cognome, citta, cellulare, telefonoCasa, email);
		if (res.moveToFirst()){
			Toast.makeText(getApplicationContext(),  "Contatto Salvato Correttamente", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(NuovoContattoActivity.this, MenuRubricaActivity.class);
			intent.putExtra("droidiary.app.NuovoContattoActivity", codUtente);
			startActivity(intent);
			
		}
    	
    }
    
    public static final void setAppFont(ViewGroup mContainer, Typeface mFont)
    {
        if (mContainer == null || mFont == null) return;

        final int mCount = mContainer.getChildCount();

        // Loop through all of the children.
        for (int i = 0; i < mCount; ++i)
        {
            final View mChild = mContainer.getChildAt(i);
            if (mChild instanceof TextView)
            {
                // Set the font if it is a TextView.
                ((TextView) mChild).setTypeface(mFont);
            }
            else if (mChild instanceof ViewGroup)
            {
                // Recursively attempt another ViewGroup.
                setAppFont((ViewGroup) mChild, mFont);
            }
        }
    }
    
    private DroidiaryDatabaseHelper dbd;
	private SQLiteDatabase db;
	private int codUtente;
	String nome, cognome, telefonoCasa, email, cellulare, citta;
}