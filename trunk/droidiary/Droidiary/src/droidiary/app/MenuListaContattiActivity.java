package droidiary.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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

public class MenuListaContattiActivity extends Activity {
	ListView list; 
	private String contatti[] = { "Iphone", "Tutorials", "Gallery", "Android","item 1", "item 2", "item3", "item 4" };

	/** Called when the activity is first created. */ 
	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
	     super.onCreate(savedInstanceState);
	     setContentView(R.layout.menulistacontatti);
	     
	     list = (ListView) findViewById(R.id.listacontatti);
	     list.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, contatti));        


	     list.setOnItemClickListener(
	         new OnItemClickListener()
	         {
	             public void onItemClick(AdapterView<?> arg0, View view,
	                     int position, long id) {
	                 Object o = list.getItemAtPosition(position);
	                 String appuntamento = o.toString();
	                 Toast.makeText(getApplicationContext(), appuntamento, Toast.LENGTH_LONG).show();
	             }   
	         });
        
       
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