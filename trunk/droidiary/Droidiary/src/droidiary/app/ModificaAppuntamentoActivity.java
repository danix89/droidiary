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
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
import droidiary.db.Appuntamento;
import droidiary.db.DroidiaryDatabaseHelper;

public class ModificaAppuntamentoActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menumodificaappuntamento);

		id=getIntent().getExtras().getInt("droidiary.app.MenuVisualizzaAppuntamentoActivity.id");
		System.out.println("ID modifica Appuntamenti: " + id);
		codUtente=getIntent().getExtras().getInt("droidiary.app.MenuVisualizzaAppuntamentoActivity.codUtente");
		System.out.println("id_account modifica Apuntamenti: " + codUtente);
		status = getIntent().getStringExtra("Status");

		// cattura di tutti i view del layout
		mDateDisplay = (TextView) findViewById(R.id.dateDisplay);
		mPickDate = (Button) findViewById(R.id.pickDate);
		mTimeDisplay = (TextView) findViewById(R.id.timeDisplay);
		mPickTime = (Button) findViewById(R.id.pickTime);
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

		// get the current date
		final Calendar date = Calendar.getInstance();
		mYear = date.get(Calendar.YEAR);
		mMonth = date.get(Calendar.MONTH);
		mDay = date.get(Calendar.DAY_OF_MONTH);
		mHour = date.get(Calendar.HOUR_OF_DAY);
		mMinute = date.get(Calendar.MINUTE);

		Cursor result=Appuntamento.getDatiFromId(db, codUtente, id);
		if(result.moveToFirst()){
			TextView utente=(TextView) findViewById(R.id.Utente);
			utente.setText("Modifica Appuntamento");

			TextView descr = (TextView)findViewById(R.id.descrizioneappuntamento);
			descr.setText(result.getString(2));
			TextView indirizzo = (TextView)findViewById(R.id.indirizzoappuntamento);
			indirizzo.setText(result.getString(3));
			TextView luogo = (TextView)findViewById(R.id.luogoappuntamento);
			luogo.setText(result.getString(4));

			if(result.getString(5).isEmpty())
				mDateDisplay.setText("Data");
			else
				mDateDisplay.setText(result.getString(5));

			if(result.getString(6).isEmpty())
				mTimeDisplay.setText("Ora");
			else
				mTimeDisplay.setText(result.getString(6));

			db.close();
		}

		Button salva=(Button)findViewById(R.id.salvappuntamento);
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
					onClickAggiorna();
				}
			}
		}); 
	}   

	public void onClickAggiorna() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Vuoi Aggionare l'Appuntamento?")
		.setCancelable(false)
		.setPositiveButton("Si", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				aggiornaAppuntamento();
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

	public void aggiornaAppuntamento(){

		if(status.equals("true")){
			AppuntamentoSync.modificaAppuntamento(id, codUtente, descrizione, indirizzo, luogo, mDateDisplay.getText().toString(), mTimeDisplay.getText().toString());
		}

		tmp = new DroidiaryDatabaseHelper(this);
		db=tmp.getWritableDatabase();
		tmp.openDataBase();
		int res= Appuntamento.modificaAppuntamento(db, id, codUtente, descrizione, indirizzo, luogo, mDateDisplay.getText().toString(), mTimeDisplay.getText().toString());

		if(res>0){
			Toast.makeText(getApplicationContext(),  "Salvataggio Effettuato con Successo!", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(ModificaAppuntamentoActivity.this, MenuAppuntamentiActivity.class);
			System.out.println(codUtente);
			intent.putExtra("droidiary.app.ModificaAppuntamentoActivity.codUtente", codUtente);
			intent.putExtra("Status", status);
			startActivity(intent);
			tmp.close();
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

	private DroidiaryDatabaseHelper dbd, tmp;
	private SQLiteDatabase db;
	private int codUtente;
	private int id;	
	String descrizione, indirizzo, luogo;
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
	String status;
	private Button cancella;
}
