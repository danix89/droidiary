package droidiary.app;

import java.io.IOException;

import droidiary.db.Account;
import droidiary.db.DroidiaryDatabaseHelper;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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

		dbd = new Account(this); //collegamento database
		db=dbd.getWritableDatabase();
		try {

			dbd.createDataBase();

		} catch (IOException ioe) {

			throw new Error("Unable to create database");

		}

		try {
			System.out.println("Database doesn't exist");
			dbd.openDataBase();

		}catch(SQLException sqle){

			throw sqle;

		}

		Button entra = (Button) findViewById(R.id.entra);

		entra.setOnClickListener(new OnClickListener() {


			public void onClick(View v) {

				EditText txtnome = (EditText)findViewById(R.id.username); //creazione riferimenti a editText
				txtnome.setImeOptions(EditorInfo.IME_ACTION_NEXT);

				EditText txtpsw = (EditText)findViewById(R.id.password);
				txtpsw.setImeOptions(EditorInfo.IME_ACTION_DONE);


				String[] arg= {txtnome.getText().toString(), txtpsw.getText().toString()};
				
				System.out.println(arg[0]+"-"+arg[1]);
				Cursor c= Account.getAccountByUserPsw(db, arg);
				if (c.moveToFirst()){
					Toast.makeText(getApplicationContext(), "Login effettuato con successo!", Toast.LENGTH_LONG).show();
					int codUtente= c.getInt(1);
					
					Intent intent = new Intent(DroidiaryActivity.this, MenuPrincipaleActivity.class);
					intent.putExtra("droidiary.app.DroidiaryActivity", codUtente);
					startActivity(intent);
				}else{
					Toast.makeText(getApplicationContext(), "Dati non esatti", Toast.LENGTH_LONG).show();
				}
			}
		});

		Button cancella = (Button) findViewById(R.id.cancella);
		cancella.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				EditText txtnome = (EditText)findViewById(R.id.username);
				EditText txtpsw = (EditText)findViewById(R.id.password);
				txtnome.setText("");
				txtpsw.setText("");
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