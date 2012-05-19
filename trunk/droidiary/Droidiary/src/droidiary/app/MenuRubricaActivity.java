package droidiary.app;

import java.util.ArrayList;
import droidiary.db.Account;
import droidiary.db.Contatto;
import droidiary.db.DroidiaryDatabaseHelper;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MenuRubricaActivity extends Activity {
	/** Called when the activity is first created. */
	private ListView lv;
	private EditText et;
	private String listview_array[];
	private ArrayList<String> array_sort= new ArrayList<String>();
	int textlength=0;


	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		populateListView();
	}

	public void onResume() {
		super.onResume();
		populateListView();
	}

	private void populateListView()	{
		setContentView(R.layout.menurubrica);
		if(codUtente==0){
		codUtente = getIntent().getExtras().getInt("droidiary.app.MenuPrincipaleActivity");
		}
		if(codUtente==0){
		codUtente = getIntent().getExtras().getInt("droidiary.app.NuovoContattoActivity");
		}
		if(codUtente==0){
		codUtente = getIntent().getExtras().getInt("droidiary.app.ModificaContattoActivity");
		}
		if(codUtente==0){
		codUtente = getIntent().getExtras().getInt("droidiary.app.EliminaContattoActivity");
		}
		if(codUtente==0){
			codUtente = getIntent().getExtras().getInt("droidiary.app.MenuVisualizzaContatto");
		}
		
		
		System.out.println("Parametro Menu Rubrica:"+codUtente);

		dbd = new DroidiaryDatabaseHelper(this); //collegamento database
		db=dbd.getWritableDatabase();
		try {
			dbd.openDataBase();
		}catch(SQLException sqle){

			throw sqle;

		}

		//passaggio codice utente

		Cursor c= Account.getAccountById(db, codUtente);
		TextView utente = (TextView) findViewById(R.id.Utente);
		System.out.println("Codice:" + codUtente);

		while(c.moveToNext()){
			String nome=c.getString(0);
			String cognome=c.getString(1);
			utente.setText("Utente: " + nome + " " + cognome);
		}

		lv=(ListView) findViewById(R.id.listacontatti);
		Cursor contatti= Contatto.getContattiById(db, codUtente);
		listview_array=getOneColumn(contatti);

		lv.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, listview_array){
			public View getView(int position, View convertView,
					ViewGroup parent) {
				View view =super.getView(position, convertView, parent);
				TextView textView=(TextView) view.findViewById(android.R.id.text1);
				//colore degli item
				textView.setTextColor(Color.BLACK);
				return view;
			}
		});
		dbd.close();
		//implementazione ricerca
		et = (EditText) findViewById(R.id.EditText01);
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
						android.R.layout.simple_list_item_1, array_sort){
					public View getView(int position, View convertView,
							ViewGroup parent) {
						View view =super.getView(position, convertView, parent);
						TextView textView=(TextView) view.findViewById(android.R.id.text1);
						//colore degli item
						textView.setTextColor(Color.BLACK);
						return view;
					}
				});
			}
		});

		lv.setOnItemClickListener(
				new OnItemClickListener()
				{
					public void onItemClick(AdapterView<?> arg0, View view,
							int position, long id) {
						Object o = lv.getItemAtPosition(position);
						String contatto = o.toString();
						Intent intent = new Intent(MenuRubricaActivity.this, MenuVisualizzaContattoActivity.class);
						intent.putExtra("droidiary.app.MenuRubricaActivity", codUtente);
						intent.putExtra("droidiary.app.MenuRubricaActivity", contatto);
						dbd.close();
						startActivity(intent);
					}   
				});

		//implementazione click premuto


		Button nuovoContatto = (Button) findViewById(R.id.buttonaggiungicontatto);
		nuovoContatto.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View arg0) {
				Intent intent = new Intent(MenuRubricaActivity.this, NuovoContattoActivity.class);
				intent.putExtra("droidiary.app.MenuRubricaActivity", codUtente);
				startActivity(intent);
			}
		}
				);		
	}
	public void onBackPressed(){
		Intent intent = new Intent(MenuRubricaActivity.this, MenuPrincipaleActivity.class);
		intent.putExtra("droidiary.app.MenuRubricaActivity", codUtente);
		startActivity(intent);
	}
	
	//tutto il risultato del cursore in un array
	private String[] getOneColumn(Cursor cursor){ 
		String myTitle = "";
		String[] myArray = null;            
		startManagingCursor(cursor);

		while(cursor.moveToNext()){
			myTitle+=cursor.getString(cursor.getColumnIndex(Contatto.NOME))+"-"+cursor.getString(cursor.getColumnIndex(Contatto.COGNOME))+";";              
		}   
		myArray = myTitle.split(";");     
		return myArray;
	}

	private DroidiaryDatabaseHelper dbd;
	private SQLiteDatabase db;
	private int codUtente;

}