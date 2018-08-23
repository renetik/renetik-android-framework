package cs.android.rpc;

import cs.java.callback.CSRunWith;

import static cs.java.lang.CSLang.empty;
import static cs.java.lang.CSLang.is;

public class CSMultiResponse extends CSResponse {

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

    public <V> CSResponse<V> add(final CSResponse<V> addedResponse, boolean isLast) {
        return isLast ? addLast(addedResponse) : add(addedResponse);
    }


    public <V> CSResponse<V> add(final CSResponse<V> addedResponse) {
        _addedResponse = addedResponse;
        if (empty(addedResponse.title())) addedResponse.title(title());
        if (addedResponse.isSending()) sending(addedResponse);
        addedResponse.onSend(() -> sending(addedResponse));
        failIfFail(addedResponse);
        return addedResponse;
    }

    public <V> CSResponse<V> addLast(final CSResponse<V> addedResponse)  {
        return add(addedResponse).onSuccess(value -> success());
    }

    public void cancel() {
        super.cancel();
        if (is(_addedResponse)) _addedResponse.cancel();
    }

}
