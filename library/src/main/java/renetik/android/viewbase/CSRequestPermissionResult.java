package renetik.android.viewbase;

public class CSRequestPermissionResult {

    public final int requestCode;
    public final String[] _permissions;
    public final int[] grantResults;

    public CSRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        this.requestCode = requestCode;
        _permissions = permissions;
        this.grantResults = grantResults;
    }
}