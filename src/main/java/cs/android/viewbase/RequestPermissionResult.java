package cs.android.viewbase;

public class RequestPermissionResult {

    public final int requestCode;
    public final String[] _permissions;
    public final int[] grantResults;

    public RequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        this.requestCode = requestCode;
        _permissions = permissions;
        this.grantResults = grantResults;
    }
}