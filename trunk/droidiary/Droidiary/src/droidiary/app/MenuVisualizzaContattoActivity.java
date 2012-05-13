package droidiary.app;

import java.io.IOException;

import droidiary.db.Account;
import droidiary.db.Contatto;
import droidiary.db.DroidiaryDatabaseHelper;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.NumberKeyListener;
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

		contatto = getIntent().getExtras().getString("droidiary.app.MenuRubricaActivity");

		System.out.println("Parametro Contatto:"+contatto);

		dbd = new DroidiaryDatabaseHelper(this); //collegamento database
		db=dbd.getWritableDatabase();
		try {
			dbd.openDataBase();
		}catch(SQLException sqle){

			throw sqle;

		}

		//nome, cognome, citta, cellulare, numeroCasa, email

		Cursor result=Contatto.getDatiFromString(db, contatto);

		if(result.moveToFirst()){
			EditText nome= (EditText)findViewById(R.id.nomecontatto);
			nome.setText(result.getString(0));
			nome.setKeyListener(new NumberKeyListener() {
			    public int getInputType() {
			        return InputType.TYPE_NULL;
			    }

			    protected char[] getAcceptedChars() {
			        return new char[] {};
			    }
			});
			EditText cognome= (EditText)findViewById(R.id.cognomecontatto);
			cognome.setText(result.getString(1));
			cognome.setKeyListener(new NumberKeyListener() {
			    public int getInputType() {
			        return InputType.TYPE_NULL;
			    }

			    protected char[] getAcceptedChars() {
			        return new char[] {};
			    }
			});
			EditText cellulare= (EditText)findViewById(R.id.telefonocellularecontatto);
			cellulare.setText(result.getString(3));
			cellulare.setKeyListener(new NumberKeyListener() {
			    public int getInputType() {
			        return InputType.TYPE_NULL;
			    }

			    protected char[] getAcceptedChars() {
			        return new char[] {};
			    }
			});
			EditText casa= (EditText)findViewById(R.id.telefonocasacontatto);
			casa.setText(result.getString(4));
			casa.setKeyListener(new NumberKeyListener() {
			    public int getInputType() {
			        return InputType.TYPE_NULL;
			    }

			    protected char[] getAcceptedChars() {
			        return new char[] {};
			    }
			});
			EditText citta= (EditText)findViewById(R.id.cittacontatto);
			citta.setText(result.getString(2));
			citta.setKeyListener(new NumberKeyListener() {
			    public int getInputType() {
			        return InputType.TYPE_NULL;
			    }

			    protected char[] getAcceptedChars() {
			        return new char[] {};
			    }
			});
			EditText email= (EditText)findViewById(R.id.emailcontatto);
			email.setText(result.getString(5));
			email.setKeyListener(new NumberKeyListener() {
			    public int getInputType() {
			        return InputType.TYPE_NULL;
			    }

			    protected char[] getAcceptedChars() {
			        return new char[] {};
			    }
			});
		}
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
	private int codUtente;
	private String contatto;
}