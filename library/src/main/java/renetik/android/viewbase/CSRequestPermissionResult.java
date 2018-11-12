package renetik.android.viewbase;

public class CSRequestPermissionResult {

    public final int requestCode;
    public final String[] _permissions;
    public final int[] statuses;

    public CSRequestPermissionResult(int requestCode, String[] permissions, int[] permisionResults) {
        this.requestCode = requestCode;
        _permissions = permissions;
        this.statuses = permisionResults;
    }
}