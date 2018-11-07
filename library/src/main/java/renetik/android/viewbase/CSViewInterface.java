package renetik.android.viewbase;

import android.content.Context;
import android.view.View;
import renetik.android.CSContextInterface;

public interface CSViewInterface extends CSContextInterface {

	View getView();

	Context context();

}
