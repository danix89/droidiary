package droidiary.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
    
           
        Button salva=(Button)findViewById(R.id.salva);
        salva.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				 EditText txtnome = (EditText)findViewById(R.id.nomecontatto);
			     String nome = txtnome.getText().toString();
			     EditText txtcognome = (EditText)findViewById(R.id.cognomecontatto);
			     String cognome = txtcognome.getText().toString();
			     EditText txtcasa = (EditText)findViewById(R.id.telefonocasacontatto);
			     String telefonoCasa = txtcasa.getText().toString();
			     EditText txtcellulare = (EditText) findViewById(R.id.telefonocellularecontatto);
			     String cellulare= txtcellulare.getText().toString();
			     EditText txtmail= (EditText) findViewById(R.id.emailcontatto);
			     String email=txtmail.getText().toString();
			     if(nome.equals("") || cognome.equals("") || telefonoCasa.equals("") || cellulare.equals("")|| email.equals("")){
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
               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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
    
    
    public static final void inserisciContatto(){
    	
    	
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
    
}