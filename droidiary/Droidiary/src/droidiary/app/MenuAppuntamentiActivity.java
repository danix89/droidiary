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
        setContentView(R.layout.menuvisualizzappuntamento);
        
        final int codUtente = getIntent().getExtras().getInt("droidiary.app.MenuPrincipaleActivity");
        
        Button nuovoAppuntamento = (Button) findViewById(R.id.menunuovoappuntamento);
        nuovoAppuntamento.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) 
			{
				Intent intent = new Intent(MenuAppuntamentiActivity.this, NuovoAppuntamentoActivity.class);
				intent.putExtra("droidiary.app.MenuAppuntamentiActivity", codUtente);
				startActivity(intent);				
			}
		});
        
    }
}