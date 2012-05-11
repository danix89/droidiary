package droidiary.app;

import java.io.IOException;
import droidiary.db.Account;
import droidiary.db.Appuntamento;
import droidiary.db.DroidiaryDatabaseHelper;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MenuPrincipaleActivity extends Activity {
    /** Called when the activity is first created. */
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuprincipale);
        final Typeface mFont = Typeface.createFromAsset(getAssets(),"fonts/AidaSerifObliqueMedium.ttf"); 
        final ViewGroup mContainer = (ViewGroup) findViewById(android.R.id.content).getRootView();
        MenuPrincipaleActivity.setAppFont(mContainer, mFont);
        
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
		
        codUtente = getIntent().getExtras().getInt("droidiary.app.DroidiaryActivity");
        System.out.println("Parametro Passato Login:" + codUtente);
        
        Cursor c= Account.getAccountById(db, codUtente);
        TextView utente = (TextView) findViewById(R.id.Utente);
        
        while(c.moveToNext()){
        String nome=c.getString(0);
        String cognome=c.getString(1);
        utente.setText("Benvenuto, " + nome + " " + cognome);
        }
        
        
        
        Button rubrica = (Button) findViewById(R.id.rubrica);
        rubrica.setOnClickListener(new OnClickListener() 
        						{
        							public void onClick(View arg0) {
        								Intent intent = new Intent(MenuPrincipaleActivity.this, MenuRubricaActivity.class);
        								intent.putExtra("droidiary.app.MenuPrincipaleActivity", codUtente);
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
        								startActivity(intent);
        							}
        						}
        					);
        
    }
    
    private String contatti[] = { "Iphone", "Tutorials", "Gallery", "Android","item 1", "item 2", "item3", "item 4" };
    
    
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