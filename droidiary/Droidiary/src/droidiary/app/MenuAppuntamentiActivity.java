package droidiary.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MenuAppuntamentiActivity extends Activity {
    /** Called when the activity is first created. */
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuappuntamenti);
        final Typeface mFont = Typeface.createFromAsset(getAssets(),"fonts/AidaSerifObliqueMedium.ttf"); 
        final ViewGroup mContainer = (ViewGroup) findViewById(android.R.id.content).getRootView();
        MenuAppuntamentiActivity.setAppFont(mContainer, mFont);
        
        
        Button nuovoAppuntamento = (Button) findViewById(R.id.menunuovoappuntamento);
        nuovoAppuntamento.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) 
			{
				Intent intent = new Intent(MenuAppuntamentiActivity.this, NuovoAppuntamentoActivity.class);
				startActivity(intent);				
			}
		});
        
        Button modificaAppuntamento = (Button) findViewById(R.id.menumodificaappuntamento);
        modificaAppuntamento.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) 
			{
				Intent intent = new Intent(MenuAppuntamentiActivity.this, ModificaAppuntamentoActivity.class);
				startActivity(intent);				
			}
		});
        
        Button eliminaAppuntamento = (Button) findViewById(R.id.menueliminaappuntamento);
        eliminaAppuntamento.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) 
			{
				Intent intent = new Intent(MenuAppuntamentiActivity.this, EliminaAppuntamentoActivity.class);
				startActivity(intent);				
			}
		});
        
        Button cercaAppuntamento = (Button) findViewById(R.id.menucercaappuntamento);
        cercaAppuntamento.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) 
			{
				Intent intent = new Intent(MenuAppuntamentiActivity.this, CercaAppuntamentoActivity.class);
				startActivity(intent);				
			}
		});
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