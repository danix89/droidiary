package droidiary.app;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import doirdiary.db.sync.AccountSync;
import doirdiary.db.sync.AppuntamentoSync;
import doirdiary.db.sync.ContattoSync;
import droidiary.db.Account;
import droidiary.db.Appuntamento;
import droidiary.db.Contatto;
import droidiary.db.DroidiaryDatabaseHelper;

public class MenuAppuntamentiActivity extends Activity {
	//	private DroidiaryDatabaseHelper dbd;
	//	private SQLiteDatabase db;

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		populateListView();
	}

	public void onResume() {
		super.onResume();
		populateListView();
	}

	private void populateListView()	{
		setContentView(R.layout.menuappuntamenti);

		if(codUtente==0){
			codUtente = getIntent().getExtras().getInt("droidiary.app.MenuPrincipaleActivity");
		}
		if(codUtente==0){
			codUtente = getIntent().getExtras().getInt("droidiary.app.NuovoAppuntamentoActivity.codUtente");
		}
		if(codUtente==0){
			codUtente = getIntent().getExtras().getInt("droidiary.app.ModificaAppuntamentoActivity.codUtente");
		}
		if(codUtente==0){
			codUtente = getIntent().getExtras().getInt("droidiary.app.MenuVisualizzaAppuntamentoActivity.codUtente");
		}

		System.out.println("Parametro Menu Appuntamenti:"+codUtente);

		dbd = new DroidiaryDatabaseHelper(this); //collegamento database
		db=dbd.getWritableDatabase();
		try {
			dbd.openDataBase();
		}catch(SQLException sqle){

			throw sqle;

		}


		ImageView stat = (ImageView) findViewById(R.id.status);
		int online = R.drawable.online;
		int offline = R.drawable.offline;
		status = getIntent().getStringExtra("Status");
		System.out.println("Status: "+status);
		if(status!=null){
			if(status.equals("true")){
				stat.setImageResource(online);
			}
			if(status.equals("false")){
				stat.setImageResource(offline);
			}
		}


		TextView utente = (TextView) findViewById(R.id.Utente);
		if(status.equals("true")){
			String res=AccountSync.getContattoAccountById(codUtente);
			try {
				JSONArray jArray = new JSONArray(res);
				for(int i=0;i<jArray.length();i++){
					JSONObject json_data = jArray.getJSONObject(i);
					String nome = json_data.getString("nome");
					String cognome = json_data.getString("cognome");
					utente.setText("Benvenuto, " + nome + " " + cognome);
				}
			} catch (JSONException e) {

				e.printStackTrace();
			}

		}

		if(status.equals("false")){
			dbd = new DroidiaryDatabaseHelper(this); //collegamento database
			db=dbd.getWritableDatabase();
			try {
				dbd.openDataBase();
			}catch(SQLException sqle){

				throw sqle;

			}
			Cursor res= Account.getAccountById(db, codUtente);
			while(res.moveToNext()){
				String nome=res.getString(0);
				String cognome=res.getString(1);
				utente.setText("Benvenuto, " + nome + " " + cognome);
				dbd.close();
			}
			dbd.close();
		}
		
		

		lv=(ListView) findViewById(R.id.listaappuntamenti);
		Cursor appuntamenti;
		String appuntamentiSync;
		if(status.equals("false")){
			dbd = new DroidiaryDatabaseHelper(this); //collegamento database
			db=dbd.getWritableDatabase();
			try {
				dbd.openDataBase();
			}catch(SQLException sqle){

				throw sqle;

			}
			appuntamenti= Appuntamento.getAppuntamentiFromId(db, codUtente);
			listview_array=getOneColumn(appuntamenti);
			System.out.println(listview_array);
			dbd.close();
		}
		
		if(status.equals("true")){
			appuntamentiSync=AppuntamentoSync.getAppuntamentiFromId(codUtente);
			listview_array=getOneColumn(appuntamentiSync);
			System.out.println(listview_array);
		}
		
		if(listview_array==null){
			lv.setEmptyView(findViewById(R.id.empty));
			EditText e = (EditText) findViewById(R.id.EditText01);
			e.setEnabled(false);
		}else{
			TextView a = (TextView)findViewById(R.id.empty);
			a.setVisibility(View.INVISIBLE);
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
		}

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
				(MenuAppuntamentiActivity.this,
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

		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(et.getWindowToken(), 0);

		lv.setOnItemClickListener(
				new OnItemClickListener()
				{
					public void onItemClick(AdapterView<?> arg0, View view,int position, long id) {
						Object o = lv.getItemAtPosition(position);
						String appuntamento = o.toString();
						Intent intent = new Intent(MenuAppuntamentiActivity.this, MenuVisualizzaAppuntamentoActivity.class);
						intent.putExtra("droidiary.app.MenuAppuntamentiActivity.codUtente", codUtente);
						intent.putExtra("droidiary.app.MenuAppuntamentiActivity.appuntamento", appuntamento);
						intent.putExtra("Status", status);
						dbd.close();
						startActivity(intent);
					}   
				});


		status = getIntent().getStringExtra("Status");

		Button nuovoAppuntamento = (Button) findViewById(R.id.buttonaggiungiappuntamento);
		nuovoAppuntamento.setOnClickListener(new OnClickListener() 
		{
			public void onClick(View arg0) {
				Intent intent = new Intent(MenuAppuntamentiActivity.this, NuovoAppuntamentoActivity.class);
				intent.putExtra("droidiary.app.MenuAppuntamentiActivity.codUtente", codUtente);
				intent.putExtra("Status", status);
				startActivity(intent);
			}
		}
				);


	}

	public void onBackPressed(){
		Intent intent = new Intent(MenuAppuntamentiActivity.this, MenuPrincipaleActivity.class);
		intent.putExtra("droidiary.app.MenuRubricaActivity", codUtente);
		intent.putExtra("Status", status);
		startActivity(intent);
	}

	//tutto il risultato del cursore in un array
	private String[] getOneColumn(Cursor cursor){ 
		String myTitle = "";
		String[] myArray = null;            
		startManagingCursor(cursor);
		while(cursor.moveToNext()){
			myTitle+=cursor.getString(cursor.getColumnIndex(Appuntamento.DESCRIZIONE))+";";              
		}   
		myArray = myTitle.split(";");     
		return myArray;
	}
	
	private String[] getOneColumn(String res){ 
		String myTitle = "";
		String[] myArray = null;
		try {
			JSONArray jArray = new JSONArray(res);
			for(int i=0;i<jArray.length();i++){
				JSONObject json_data = jArray.getJSONObject(i);
				String descrizione = json_data.getString("descrizione");
				myTitle+=descrizione+";"; 
				myArray = myTitle.split(";");     
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return myArray;
	}

	private DroidiaryDatabaseHelper dbd;
	private SQLiteDatabase db;
	private int codUtente;
	private ListView lv;
	private EditText et;
	String listview_array[];
	private ArrayList<String> array_sort= new ArrayList<String>();
	int textlength=0;
	String status;
}