package cs.android.rpc;

import static cs.java.lang.CSLang.empty;
import static cs.java.lang.CSLang.is;

public class CSMultiResponse<T> extends CSResponse<T> {

    private CSResponse<?> _addedResponse;

    public CSMultiResponse() {
    }

    public CSMultiResponse(String title) {
        title(title);
    }

    public CSResponse<?> resend() {
        reset();
        return _addedResponse.resend();
    }

    public <V> CSResponse<V> add(final CSResponse<V> addedResponse) {
        _addedResponse = addedResponse;
        if (empty(addedResponse.title())) addedResponse.title(title());
        if (addedResponse.isSending()) sending(addedResponse);
        addedResponse.onSend(() -> sending(addedResponse));
        failIfFail(addedResponse);
        return addedResponse;
    }

    public CSResponse<T> addLast(final CSResponse<T> response) {
        return successIfSuccess(add(response));
    }

    public void cancel() {
        super.cancel();
        if (is(_addedResponse)) _addedResponse.cancel();
    }

}
