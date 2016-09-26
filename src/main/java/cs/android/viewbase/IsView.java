package cs.android.viewbase;

import android.content.Context;
import android.view.View;
import cs.android.HasContext;

public interface IsView extends HasContext {

	View asView();

	@Override
	Context context();

}
