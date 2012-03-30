package droidiary.app;

import droidiary.db.Account;
import droidiary.db.DroidiaryDatabaseHelper;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DroidiaryActivity extends Activity{
	/** Called when the activity is first created. */

	public void onCreate(Bundle savedInstanceState) {

		//codice per il font
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		final Typeface mFont = Typeface.createFromAsset(getAssets(),"fonts/AidaSerifObliqueMedium.ttf"); 
		final ViewGroup mContainer = (ViewGroup) findViewById(android.R.id.content).getRootView();
		DroidiaryActivity.setAppFont(mContainer, mFont);
		//fine codice per il font

		dbd = new DroidiaryDatabaseHelper(this); //collegamento database
		db= dbd.getWritableDatabase(); //apertura database
		Account.insertAccount(db, "marpir", "m0001");
		Account.insertAccount(db, "saladd", "s0002");

		Button entra = (Button) findViewById(R.id.entra);

		entra.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				EditText txtnome = (EditText)findViewById(R.id.username); //creazione riferimenti a editText
				EditText txtpsw = (EditText)findViewById(R.id.password);
				
			
				String[] arg= {txtnome.getText().toString(), txtpsw.getText().toString()};
				Cursor c= Account.getAccountByUserPsw(db, arg);

				if (c.moveToFirst()){
						Toast.makeText(getApplicationContext(), "Login effettuato con successo!", Toast.LENGTH_LONG).show();
						Intent intent = new Intent(DroidiaryActivity.this, MenuPrincipaleActivity.class);
						startActivity(intent);
				}else{
						Toast.makeText(getApplicationContext(), "Dati non esatti", Toast.LENGTH_LONG).show();
					}
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

	private DroidiaryDatabaseHelper dbd;
	private SQLiteDatabase db;
}