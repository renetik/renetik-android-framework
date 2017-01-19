package cs.android.rpc;

import cs.java.callback.CSRun;
import cs.java.callback.CSRunWith;
import cs.java.collections.CSList;

import static cs.java.lang.CSLang.doLater;
import static cs.java.lang.CSLang.empty;
import static cs.java.lang.CSLang.is;
import static cs.java.lang.CSLang.list;

public class CSConcurrentResponse<T> extends CSResponse<T> {

    private CSList<CSResponse> _sendingResponses = list();
    private CSResponse<T> _mainResponse;

    public CSConcurrentResponse() {
    }

    public CSConcurrentResponse(String title) {
        title(title);
    }

    public CSConcurrentResponse(CSResponse<T> response, CSResponse<?>... responses) {
        addMain(response);
        addAll(responses);
    }

    private CSResponse<T> addMain(CSResponse<T> response) {
        _mainResponse = response;
        return add(response);
    }

    public <Data> CSResponse<Data> add(final CSResponse<Data> response) {
        _sendingResponses.add(response);
        if (response.isSending()) onResponseSend();
        return response.onFailed(new CSRunWith<CSResponse>() {
            public void run(CSResponse failedResponse) {
                onResponseFailed(failedResponse);
            }
        }).
                onSend(new CSRun() {
                    public void run() {
                        onResponseSend();
                    }
                }).
                onSuccess(new CSRun() {
                    public void run() {
                        onResponseSuccess(response);
                    }
                });
    }

    private void onResponseSend() {
        if (!isSending()) sending(this);
    }

    private void onResponseFailed(CSResponse failedResponse) {
        if (!isFailed()) {
            failed(failedResponse);
            for (CSResponse response : _sendingResponses)
                if (response != failedResponse) response.cancel();
        }
    }

    public void onResponseSuccess(CSResponse response) {
        _sendingResponses.delete(response);
        if (empty(_sendingResponses)) success();
    }

    public CSConcurrentResponse<T> addAll(CSResponse<?>... responses) {
        for (CSResponse<?> response : responses) add(response);
        return this;
    }

    public void cancel() {
        super.cancel();
        for (CSResponse response : _sendingResponses) response.cancel();
    }

    public CSResponse<?> resend() {
        reset();
        for (CSResponse response : _sendingResponses) response.resend();
        return this;
    }

    public void success() {
        if (is(_mainResponse)) success(_mainResponse.data());
        else super.success();
    }

    public void onAddDone() {
        if (empty(_sendingResponses)) doLater(new CSRun() {
            public void run() {
                success();
            }
        });
    }

}
