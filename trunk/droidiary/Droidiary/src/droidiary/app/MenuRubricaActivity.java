package droidiary.app;

import java.io.IOException;

import droidiary.db.Account;
import droidiary.db.DroidiaryDatabaseHelper;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MenuRubricaActivity extends Activity {
    /** Called when the activity is first created. */
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menurubrica);
        
        codUtente = getIntent().getExtras().getInt("droidiary.app.MenuPrincipaleActivity");
        System.out.println("Parametro Menu Rubrica:"+codUtente);
        
        dbd = new DroidiaryDatabaseHelper(this); //collegamento database
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
		
        Cursor c= Account.getAccountById(db, codUtente);
        TextView utente = (TextView) findViewById(R.id.Utente);
        
        while(c.moveToNext()){
        String nome=c.getString(0);
        String cognome=c.getString(1);
        utente.setText("Utente: " + nome + " " + cognome);
        }
        
        
        
        Button nuovoContatto = (Button) findViewById(R.id.menunuovocontatto);
        nuovoContatto.setOnClickListener(new OnClickListener() 
        						{
        							public void onClick(View arg0) {
        								Intent intent = new Intent(MenuRubricaActivity.this, NuovoContattoActivity.class);
        								intent.putExtra("droidiary.app.MenuRubricaActivity", codUtente);
        								startActivity(intent);
        							}
        						}
        					);
        
        Button modificaContatto = (Button) findViewById(R.id.menumodificacontatto);
        modificaContatto.setOnClickListener(new OnClickListener() 
        						{
        							public void onClick(View arg0) {
        								Intent modifica = new Intent(MenuRubricaActivity.this, MenuListaContattiActivity.class);
        								modifica.putExtra("droidiary.app.MenuRubricaActivity", codUtente);
        								startActivity(modifica);
        							}
        						}
        					);
        
        Button eliminaContatto = (Button) findViewById(R.id.menueliminacontatto);
        eliminaContatto.setOnClickListener(new OnClickListener() 
        						{
        							public void onClick(View arg0) {
        								Intent elimina = new Intent(MenuRubricaActivity.this, MenuListaContattiActivity.class);
        								elimina.putExtra("droidiary.app.MenuRubricaActivity", codUtente);
        								startActivity(elimina);
        							}
        						}
        					);
    
        final Typeface mFont = Typeface.createFromAsset(getAssets(),"fonts/AidaSerifObliqueMedium.ttf"); 
        final ViewGroup mContainer = (ViewGroup) findViewById(android.R.id.content).getRootView();
        MenuRubricaActivity.setAppFont(mContainer, mFont);
        
    
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
    
}