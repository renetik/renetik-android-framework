package cs.android.viewbase;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
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
