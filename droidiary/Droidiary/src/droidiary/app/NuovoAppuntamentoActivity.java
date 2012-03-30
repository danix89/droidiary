package droidiary.app;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

public class NuovoAppuntamentoActivity extends Activity{
    private TextView mDataDisplay;
    private int mYear;
    private int mMonth;
    private int mDay;
    static final int DATE_DIALOG_ID = 1;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menunuovoappuntamento);
		final Typeface mFont = Typeface.createFromAsset(getAssets(),"fonts/AidaSerifObliqueMedium.ttf"); 
		final ViewGroup mContainer = (ViewGroup) findViewById(android.R.id.content).getRootView();
		DroidiaryActivity.setAppFont(mContainer, mFont);

		Button pickDate = (Button) findViewById(R.id.pickDate);
		pickDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});
		
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		updateDisplay();
	}

	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this,
					mDateSetListener,
					mYear, mMonth, mDay);
		}
		return null;
	}
	
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DATE_DIALOG_ID:
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
			break;
		}
	}
	
	private void updateDisplay() {
		mDataDisplay.setText(
				new StringBuilder()
				// Month is 0 based so add 1
				.append(mMonth + 1).append("-")
				.append(mDay).append("-")
				.append(mYear).append(" "));
	}
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay();
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
