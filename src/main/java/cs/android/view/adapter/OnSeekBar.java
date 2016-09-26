package cs.android.view.adapter;

import cs.android.viewbase.CSView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class OnSeekBar implements OnSeekBarChangeListener {

	public OnSeekBar(CSView<?> view, int... viewId) {
		for (int id : viewId)
			((SeekBar) view.findView(id)).setOnSeekBarChangeListener(this);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}

}
