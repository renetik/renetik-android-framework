package cs.android.view.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;

import cs.android.viewbase.CSView;

public abstract class CSClick implements OnClickListener {

	public CSClick(){}

	public CSClick(CSView view) {
		view.asView().setOnClickListener(this);
	}

	public CSClick(ViewParent view) {
		((ViewGroup) view).setOnClickListener(this);
	}

	public CSClick(ViewGroup view) {
		view.setOnClickListener(this);
	}

	public CSClick(View view) {
		view.setOnClickListener(this);
	}

	public CSClick(CSView<?> view, int... viewId) {
		for (int id : viewId)
			view.findView(id).setOnClickListener(this);
	}

}
