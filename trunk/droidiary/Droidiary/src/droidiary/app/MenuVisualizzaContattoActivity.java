package droidiary.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MenuVisualizzaContattoActivity extends Activity {
    /** Called when the activity is first created. */
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuvisualizzacontatto);
        
       
        final Typeface mFont = Typeface.createFromAsset(getAssets(),"fonts/AidaSerifObliqueMedium.ttf"); 
        final ViewGroup mContainer = (ViewGroup) findViewById(android.R.id.content).getRootView();
        MenuRubricaActivity.setAppFont(mContainer, mFont);
        
        EditText nome= (EditText)findViewById(R.id.nomecontatto);
        nome.setFocusable(false);
        EditText cognome= (EditText)findViewById(R.id.cognomecontatto);
        cognome.setFocusable(false);
        EditText cellulare= (EditText)findViewById(R.id.telefonocellularecontatto);
        cellulare.setFocusable(false);
        EditText casa= (EditText)findViewById(R.id.telefonocasacontatto);
        casa.setFocusable(false);
        EditText citta= (EditText)findViewById(R.id.cittacontatto);
        citta.setFocusable(false);
        EditText email= (EditText)findViewById(R.id.emailcontatto);
        email.setFocusable(false);
       
    
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