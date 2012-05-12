package droidiary.app;

import java.io.IOException;
import java.util.ArrayList;

import droidiary.db.Account;
import droidiary.db.Contatto;
import droidiary.db.DroidiaryDatabaseHelper;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MenuRubricaActivity extends Activity {
	/** Called when the activity is first created. */
	private ListView lv;
	private EditText et;
	private String listview_array[] = { "ONE", "TWO", "THREE", "FOUR", "FIVE",
			"SIX", "SEVEN", "EIGHT", "NINE", "TEN" };
	private ArrayList<String> array_sort= new ArrayList<String>();
	int textlength=0;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menurubrica);

		codUtente = getIntent().getExtras().getInt("droidiary.app.MenuPrincipaleActivity");
		System.out.println("Parametro Menu Rubrica:"+codUtente);

		dbd = new DroidiaryDatabaseHelper(this); //collegamento database
		db=dbd.getWritableDatabase();
		try {
			dbd.createDataBase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}

		try {
			dbd.openDataBase();
		}catch(SQLException sqle){

			throw sqle;

		}

		Cursor c= Account.getAccountById(db, codUtente);


		TextView utente = (TextView) findViewById(R.id.Utente);

		while(c.moveToNext()){
			String nome=c.getString(0);
			String cognome=c.getString(1);
			utente.setText("Utente: " + nome + " " + cognome);
		}


		//implementazione ricerca
		lv = (ListView) findViewById(R.id.listacontatti);
		et = (EditText) findViewById(R.id.EditText01);
		lv.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, listview_array));


		et.addTextChangedListener(new TextWatcher()
		{
			public void afterTextChanged(Editable s)
			{
				// Abstract Method of TextWatcher Interface.
			}
			public void beforeTextChanged(CharSequence s,
					int start, int count, int after)
			{
				// Abstract Method of TextWatcher Interface.
			}
			public void onTextChanged(CharSequence s,
					int start, int before, int count)
			{
				textlength = et.getText().length();
				array_sort.clear();
				for (int i = 0; i < listview_array.length; i++)
				{
					if (textlength <= listview_array[i].length())
					{
						if(et.getText().toString().equalsIgnoreCase(
								(String)
								listview_array[i].subSequence(0,
										textlength)))
						{
							array_sort.add(listview_array[i]);
						}
					}
				}
				lv.setAdapter(new ArrayAdapter<String>
				(MenuRubricaActivity.this,
						android.R.layout.simple_list_item_1, array_sort));
			}
		});

		lv.setOnItemClickListener(
				new OnItemClickListener()
				{
					public void onItemClick(AdapterView<?> arg0, View view,
							int position, long id) {
						Object o = lv.getItemAtPosition(position);
						String appuntamento = o.toString();
						Toast.makeText(getApplicationContext(), appuntamento, Toast.LENGTH_LONG).show();
					}   
				});





		Button nuovoContatto = (Button) findViewById(R.id.buttonuovocontatto);
		nuovoContatto.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View arg0) {
				Intent intent = new Intent(MenuRubricaActivity.this, NuovoContattoActivity.class);
				intent.putExtra("droidiary.app.MenuRubricaActivity", codUtente);
				startActivity(intent);
			}
		}
				);

		Button cercaContatto = (Button) findViewById(R.id.buttoncercacontatto);
		nuovoContatto.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View arg0) {
				Intent intent = new Intent(MenuRubricaActivity.this, NuovoContattoActivity.class);
				intent.putExtra("droidiary.app.MenuRubricaActivity", codUtente);
				startActivity(intent);
			}
		}
				);



		final Typeface mFont = Typeface.createFromAsset(getAssets(),"fonts/AidaSerifObliqueMedium.ttf"); 
		final ViewGroup mContainer = (ViewGroup) findViewById(android.R.id.content).getRootView();
		MenuRubricaActivity.setAppFont(mContainer, mFont);


	}

	private String contatti[] = { "Iphone", "Tutorials", "Gallery", "Android","item 1", "item 2", "item3", "item 4" };

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

}