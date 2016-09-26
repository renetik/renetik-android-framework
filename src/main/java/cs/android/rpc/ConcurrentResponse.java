package cs.android.rpc;

import cs.java.callback.Run;
import cs.java.callback.RunWith;
import cs.java.collections.CSList;

import static cs.java.lang.Lang.doLater;
import static cs.java.lang.Lang.empty;
import static cs.java.lang.Lang.is;
import static cs.java.lang.Lang.list;

public class ConcurrentResponse<T> extends Response<T> {

    private CSList<Response> _sendingResponses = list();
    private Response<T> _mainResponse;

    public ConcurrentResponse() {
    }

    public ConcurrentResponse(String title) {
        title(title);
    }

    public ConcurrentResponse(Response<T> response, Response<?>... responses) {
        addMain(response);
        addAll(responses);
    }

    private Response<T> addMain(Response<T> response) {
        _mainResponse = response;
        return add(response);
    }

    public <Data> Response<Data> add(final Response<Data> response) {
        _sendingResponses.add(response);
        if (response.isSending()) onResponseSend();
        return response.onFailed(new RunWith<Response>() {
            public void run(Response failedResponse) {
                onResponseFailed(failedResponse);
            }
        }).
                onSend(new Run() {
                    public void run() {
                        onResponseSend();
                    }
                }).
                onSuccess(new Run() {
                    public void run() {
                        onResponseSuccess(response);
                    }
                });
    }

    private void onResponseSend() {
        if (!isSending()) sending(this);
    }

    private void onResponseFailed(Response failedResponse) {
        if (!isFailed()) {
            failed(failedResponse);
            for (Response response : _sendingResponses)
                if (response != failedResponse) response.cancel();
        }
    }

    public void onResponseSuccess(Response response) {
        _sendingResponses.delete(response);
        if (empty(_sendingResponses)) success();
    }

    public ConcurrentResponse<T> addAll(Response<?>... responses) {
        for (Response<?> response : responses) add(response);
        return this;
    }

    public void cancel() {
        super.cancel();
        for (Response response : _sendingResponses) response.cancel();
    }

    public Response<?> resend() {
        reset();
        for (Response response : _sendingResponses) response.resend();
        return this;
    }

    public void success() {
        if (is(_mainResponse)) success(_mainResponse.data());
        else super.success();
    }

    public void onAddDone() {
        if (empty(_sendingResponses)) doLater(new Run() {
            public void run() {
                success();
            }
        });
    }

}
