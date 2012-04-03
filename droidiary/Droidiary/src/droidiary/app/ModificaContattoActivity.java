package droidiary.app;

import droidiary.db.Account;
import droidiary.db.Contatto;
import droidiary.db.DroidiaryDatabaseHelper;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;



public class ModificaContattoActivity extends ListActivity{


    /** Called when the activity is first created. */
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.menulistacontatti);

        setContentView(R.layout.menuvisualizzacontatto);

        final Typeface mFont = Typeface.createFromAsset(getAssets(),"fonts/AidaSerifObliqueMedium.ttf"); 
        final ViewGroup mContainer = (ViewGroup) findViewById(android.R.id.content).getRootView();
        MenuRubricaActivity.setAppFont(mContainer, mFont);
            
        
        //recupero parametri passati da attivit� precedente
        int idAccount= getIntent().getExtras().getInt("droidiary.app.DroidiaryActivity");
        
        dbd = new DroidiaryDatabaseHelper(this); //collegamento database
		db= dbd.getWritableDatabase(); //apertura database
		
        ListView listaContatti = (ListView)findViewById(R.id.listacontatti);
        String[]contatti=null;
        
        String[] arg= {Integer.toString(idAccount)};
		Cursor c= Contatto.getAllContatto(db, arg);
		
		int i=0;
		while(c.moveToNext())
		{
			contatti[i]= c.getString(3);
			i++;
		}
		
		listaContatti.setAdapter(new ArrayAdapter<String>(this, android.R.id.list, contatti));
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
}