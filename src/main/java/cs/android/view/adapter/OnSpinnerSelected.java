package cs.android.view.adapter;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

public class OnSpinnerSelected implements OnItemSelectedListener {

	private Spinner _spinner;

	public OnSpinnerSelected() {
	}

	public OnSpinnerSelected(Spinner spinner) {
		_spinner = spinner;
		setListener();
	}

	public void clearListener() {
		_spinner.setOnItemSelectedListener(null);
	}

	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		onItemSelected(position);
	}

	protected void onItemSelected(int position) {
	}

	protected void onNothingSelected() {
	}

	public void onNothingSelected(AdapterView<?> parent) {
		onNothingSelected();
	}

	public void setListener() {
		_spinner.setOnItemSelectedListener(this);
	}

}
