package cs.android.rpc;

import cs.java.callback.Run;

import static cs.java.lang.Lang.empty;
import static cs.java.lang.Lang.is;

public class MultiResponse<T> extends Response<T> {

    private Response<?> _addedResponse;

    public MultiResponse() {
    }

    public MultiResponse(String title) {
        title(title);
    }

    public Response<?> resend() {
        reset();
        return _addedResponse.resend();
    }

    public <V> Response<V> add(final Response<V> addedResponse) {
        _addedResponse = addedResponse;
        if (empty(addedResponse.title())) addedResponse.title(title());
        if (addedResponse.isSending()) sending(addedResponse);
        addedResponse.onSend(new Run() {
            public void run() {
                sending(addedResponse);
            }
        });
        failIfFail(addedResponse);
        return addedResponse;
    }

    public Response<T> addLast(final Response<T> response) {
        return successIfSuccess(add(response));
    }

    public void cancel() {
        super.cancel();
        if (is(_addedResponse)) _addedResponse.cancel();
    }

}
