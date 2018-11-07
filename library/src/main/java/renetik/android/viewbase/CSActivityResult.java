package renetik.android.viewbase;

import android.content.Intent;

import static android.app.Activity.RESULT_OK;

public class CSActivityResult {
	public final int requestCode;
	public final int resultCode;
	public final Intent data;

	public CSActivityResult(int requestCode, int resultCode, Intent data) {
		this.requestCode = requestCode;
		this.resultCode = resultCode;
		this.data = data;
	}

	public boolean conformTo(int requestCode, int resulCode) {
		return  this.requestCode == requestCode && this.resultCode == resulCode;
	}

	public boolean isOK(int requestCode) {
		return  this.requestCode == requestCode && this.resultCode == RESULT_OK;
	}
}