package cs.android.viewbase;

import android.content.Intent;

public class ActivityResult {
	public final int requestCode;
	public final int resultCode;
	public final Intent data;

	public ActivityResult(int requestCode, int resultCode, Intent data) {
		this.requestCode = requestCode;
		this.resultCode = resultCode;
		this.data = data;
	}
}