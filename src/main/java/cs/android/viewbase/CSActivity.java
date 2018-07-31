package cs.android.viewbase;

import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.ActionBar;
import android.view.MenuInflater;
import cs.android.CSActivityInterface;
import cs.java.callback.CSReturn;

public interface CSActivity extends CSActivityInterface {

	CSViewController controller();

	ActionBar getSupportActionBar();

	FragmentManager getSupportFragmentManager();

	MenuInflater getSupportMenuInflater();

	void supportInvalidateOptionsMenu();

	CSViewController createController();

}
