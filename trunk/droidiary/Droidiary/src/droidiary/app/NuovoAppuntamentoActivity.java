package droidiary.app;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

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
		final Typeface mFont = Typeface.createFromAsset(getAssets(),"fonts/AidaSerifObliqueMedium.ttf"); 
		final ViewGroup mContainer = (ViewGroup) findViewById(android.R.id.content).getRootView();
		DroidiaryActivity.setAppFont(mContainer, mFont);

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
        
        salva.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
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
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

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
            
          
	
	
	public static final void setAppFont(ViewGroup mContainer, Typeface mFont){
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
