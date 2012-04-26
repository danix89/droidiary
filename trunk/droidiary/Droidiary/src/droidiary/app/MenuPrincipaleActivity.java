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

public class MenuPrincipaleActivity extends Activity {
    /** Called when the activity is first created. */
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuprincipale);
        final Typeface mFont = Typeface.createFromAsset(getAssets(),"fonts/AidaSerifObliqueMedium.ttf"); 
        final ViewGroup mContainer = (ViewGroup) findViewById(android.R.id.content).getRootView();
        MenuPrincipaleActivity.setAppFont(mContainer, mFont);
        
        final int codUtente = getIntent().getExtras().getInt("droidiary.app.DroidiaryActivity");
        
        Button rubrica = (Button) findViewById(R.id.rubrica);
        rubrica.setOnClickListener(new OnClickListener() 
        						{
        							public void onClick(View arg0) {
        								Intent intent = new Intent(MenuPrincipaleActivity.this, MenuRubricaActivity.class);
        								intent.putExtra("droidiary.app.MenuPrincipaleActivity", codUtente);
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
        								startActivity(intent);
        							}
        						}
        					);
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