package droidiary.app;

import android.app.Activity;
import android.content.Intent;
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
        
        Button nuovoContatto = (Button) findViewById(R.id.menunuovocontatto);
        nuovoContatto.setOnClickListener(new OnClickListener() 
        						{
        							public void onClick(View arg0) {
        								Intent nuovo = new Intent(MenuRubricaActivity.this, NuovoContattoActivity.class);
        								startActivity(nuovo);
        							}
        						}
        					);
        
        Button modificaContatto = (Button) findViewById(R.id.menumodificacontatto);
        modificaContatto.setOnClickListener(new OnClickListener() 
        						{
        							public void onClick(View arg0) {
        								Intent modifica = new Intent(MenuRubricaActivity.this, ModificaContattoActivity.class);
        								startActivity(modifica);
        							}
        						}
        					);
        
        /*Button eliminaContatto = (Button) findViewById(R.id.menueliminacontatto);
        eliminaContatto.setOnClickListener(new OnClickListener() 
        						{
        							public void onClick(View arg0) {
        								Intent elimina = new Intent(MenuRubricaActivity.this, EliminaContattoActivity.class);
        								startActivity(elimina);
        							}
        						}
        					);
    
    */
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
    
}