package droidiary.app;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import doirdiary.db.sync.AppuntamentoSync;
import droidiary.db.Account;
import droidiary.db.Appuntamento;
import droidiary.db.DroidiaryDatabaseHelper;

public class NuovoAppuntamentoActivity extends Activity{
	private TextView mDateDisplay;
	private Button mPickDate;
	private int mYear;
	private int mMonth;
	private int mDay;
	static final int DATE_DIALOG_ID = 0;
	private TextView mTimeDisplay;
	private Button mPickTime;
	private int mHour;
	private int mMinute;
	static final int TIME_DIALOG_ID = 1;

	private Button salva;
	private Button cancella;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menunuovoappuntamento);

		codUtente = getIntent().getExtras().getInt("droidiary.app.MenuAppuntamentiActivity.codUtente");

		System.out.println("Parametro Menu Appuntamento:"+codUtente);

		dbd = new DroidiaryDatabaseHelper(this); //collegamento database
		db=dbd.getWritableDatabase();
		try {
			dbd.openDataBase();
		}catch(SQLException sqle){

			throw sqle;

		}

		Cursor c= Account.getAccountById(db, codUtente);
		TextView utente = (TextView) findViewById(R.id.Utente);
		System.out.println("Codice:" + codUtente);
		while(c.moveToNext()){
			String nome=c.getString(0);
			String cognome=c.getString(1);
			utente.setText("Utente: " + nome + " " + cognome);
		}

		// cattura di tutti i view del layout
		mDateDisplay = (TextView) findViewById(R.id.dateDisplay);
		mPickDate = (Button) findViewById(R.id.pickDate);
		mTimeDisplay = (TextView) findViewById(R.id.timeDisplay);
		mPickTime = (Button) findViewById(R.id.pickTime);
		salva = (Button) findViewById(R.id.salvappuntamento);
		cancella = (Button) findViewById(R.id.cancellappuntamento);


		// add a click listener to the button
		mPickDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});

		mPickTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(TIME_DIALOG_ID);
			}
		});

		et = (EditText) findViewById(R.id.descrizioneappuntamento);
		et.addTextChangedListener(new TextWatcher()
		{
			public void afterTextChanged(Editable s)
			{
				int i = s.length();
				if(i == 44)
					Toast.makeText(getApplicationContext(),  "Limite di caratteri consentiti per la descrizione dell'appuntamento raggiunto", Toast.LENGTH_LONG).show();
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after){ }

			public void onTextChanged(CharSequence s, int start, int before, int count) { }		
		});

		salva.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				EditText txtdescrizione = (EditText)findViewById(R.id.descrizioneappuntamento);
				descrizione = txtdescrizione.getText().toString();
				EditText txtindirizzo = (EditText)findViewById(R.id.indirizzoappuntamento);
				indirizzo = txtindirizzo.getText().toString();
				EditText txtluogo = (EditText)findViewById(R.id.luogoappuntamento);
				luogo = txtluogo.getText().toString();
				TextView data= (TextView) findViewById(R.id.dateDisplay);
				TextView ora= (TextView) findViewById(R.id.timeDisplay);
				if(descrizione.equals("")){
					Toast.makeText(getApplicationContext(),  "Inserire almeno una descrizione dell'appuntamento", Toast.LENGTH_LONG).show();
				}else if(data.equals("Data") || ora.equals("Ora")){
					Toast.makeText(getApplicationContext(),  "Inserire la data e l'ora dell'appuntamento", Toast.LENGTH_LONG).show();
				}else{ 
					dbd.close();
					onClickSalva();
				}
			}
		});

		cancella.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				EditText descrizione= (EditText) findViewById(R.id.descrizioneappuntamento);
				descrizione.setText("");
				EditText luogo= (EditText) findViewById(R.id.luogoappuntamento);
				luogo.setText("");
				EditText indirizzo= (EditText) findViewById(R.id.indirizzoappuntamento);
				indirizzo.setText("");

			}
		});

		// get the current date
		final Calendar date = Calendar.getInstance();
		mYear = date.get(Calendar.YEAR);
		mMonth = date.get(Calendar.MONTH);
		mDay = date.get(Calendar.DAY_OF_MONTH);
		mHour = date.get(Calendar.HOUR_OF_DAY);
		mMinute = date.get(Calendar.MINUTE);

		status = getIntent().getStringExtra("Status");
		System.out.println("Status Nuovo Contatto: "+status);

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
		dbd.close();
	}

	public void onClickSalva() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Vuoi Salvare l'appuntamento?")
		.setCancelable(false)
		.setPositiveButton("Si", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				inserisciAppuntamento();
			}
		})
		.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void inserisciAppuntamento(){

		if(status.equals("true")){
			AppuntamentoSync.insertAppuntamento(codUtente, descrizione, indirizzo, luogo, mDateDisplay.getText().toString(), mTimeDisplay.getText().toString());
		}
		
		db=dbd.getWritableDatabase();
		try {
			dbd.openDataBase();
		}catch(SQLException sqle){

			throw sqle;

		}

		long res = Appuntamento.insertAppuntamento(db, codUtente, descrizione, indirizzo, luogo, mDateDisplay.getText().toString().trim(), mTimeDisplay.getText().toString().trim());
		if(res == -1){
			Toast.makeText(getApplicationContext(),  "Problema con la query", Toast.LENGTH_LONG).show();
		}
		else{
			Toast.makeText(getApplicationContext(),  "Contatto Salvato Correttamente", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(NuovoAppuntamentoActivity.this, MenuAppuntamentiActivity.class);
			System.out.println(codUtente);
			intent.putExtra("droidiary.app.NuovoAppuntamentoActivity.codUtente", codUtente);
			intent.putExtra("Status", status);
			dbd.close();
			startActivity(intent);
		}
	}	

	private void updateDate() {
		mDateDisplay.setText(
				new StringBuilder()
				// Month is 0 based so add 1
				.append(mDay).append("/")
				.append(mMonth + 1).append("/")
				.append(mYear).append(" "));
		showDialog(TIME_DIALOG_ID);
	}

	public void updatetime()
	{
		mTimeDisplay.setText(
				new StringBuilder()
				.append(pad(mHour)).append(":")
				.append(pad(mMinute))); 
	}

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	// Timepicker dialog generation
	private TimePickerDialog.OnTimeSetListener mTimeSetListener =
			new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			updatetime();
		}


	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this,
					mDateSetListener,
					mYear, mMonth, mDay);

		case TIME_DIALOG_ID:
			return new TimePickerDialog(this,
					mTimeSetListener, mHour, mMinute, false);

		}
		return null;
	}

	// the callback received when the user "sets" the date in the dialog
	private DatePickerDialog.OnDateSetListener mDateSetListener =
			new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, 
				int monthOfYear, int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDate();
		}
	};

	private DroidiaryDatabaseHelper dbd;
	private SQLiteDatabase db;
	private EditText et;
	private int codUtente;
	String descrizione, indirizzo, luogo, status;
}
