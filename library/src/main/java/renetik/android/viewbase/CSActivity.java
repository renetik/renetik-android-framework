package renetik.android.viewbase;

import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.ActionBar;
import android.view.MenuInflater;
import renetik.android.CSActivityInterface;

public interface CSActivity extends CSActivityInterface {

	CSViewController controller();

	ActionBar getSupportActionBar();

	FragmentManager getSupportFragmentManager();

	MenuInflater getSupportMenuInflater();

	void supportInvalidateOptionsMenu();

	CSViewController createController();

}
