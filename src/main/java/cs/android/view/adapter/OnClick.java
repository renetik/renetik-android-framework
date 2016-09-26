package cs.android.view.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import cs.android.viewbase.IsView;
import cs.android.viewbase.CSView;

public abstract class OnClick implements OnClickListener {

	public OnClick(){}

	public OnClick(IsView view) {
		view.asView().setOnClickListener(this);
	}

	public OnClick(ViewParent view) {
		((ViewGroup) view).setOnClickListener(this);
	}

	public OnClick(ViewGroup view) {
		view.setOnClickListener(this);
	}

	public OnClick(View view) {
		view.setOnClickListener(this);
	}

	public OnClick(CSView<?> view, int... viewId) {
		for (int id : viewId)
			view.findView(id).setOnClickListener(this);
	}

}
