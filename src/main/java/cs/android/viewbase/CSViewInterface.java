package cs.android.viewbase;

import android.content.Context;
import android.view.View;
import cs.android.CSContextInterface;

public interface CSViewInterface extends CSContextInterface {

	View asView();

	Context context();

}
