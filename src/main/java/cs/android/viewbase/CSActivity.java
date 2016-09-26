package cs.android.viewbase;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.MenuInflater;
import cs.android.HasActivity;
import cs.java.callback.Return;

public interface CSActivity extends HasActivity, Return<CSViewController> {

	CSViewController controller();

	ActionBar getSupportActionBar();

	FragmentManager getSupportFragmentManager();

	MenuInflater getSupportMenuInflater();

	void supportInvalidateOptionsMenu();

}
