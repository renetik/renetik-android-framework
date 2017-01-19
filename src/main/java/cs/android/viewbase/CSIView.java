package cs.android.viewbase;

import android.content.Context;
import android.view.View;
import cs.android.CSIContext;

public interface CSIView extends CSIContext {

	View asView();

	@Override
	Context context();

}
