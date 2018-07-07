package cs.android.json.old;

/**
 * Created by Rene Dohan on 01/04/15.
 */
public class CSJSONNullType extends CSJSONType {

    public CSJSONNullType() {
        super(org.json.JSONObject.NULL);
    }

    public Object getValue() {
        return null;
    }
}
