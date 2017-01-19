package cs.android.rpc;

import cs.android.viewbase.CSContextController;
import cs.android.viewbase.CSViewController;
import cs.java.callback.CSRun;
import cs.java.callback.CSRunWith;
import cs.java.collections.CSList;
import cs.java.event.CSEvent;
import cs.java.event.CSEvent.EventRegistration;
import cs.java.event.CSListener;
import cs.java.net.CSURL;

import static cs.java.lang.CSLang.NO;
import static cs.java.lang.CSLang.YES;
import static cs.java.lang.CSLang.error;
import static cs.java.lang.CSLang.event;
import static cs.java.lang.CSLang.exception;
import static cs.java.lang.CSLang.fire;
import static cs.java.lang.CSLang.getRootCauseMessage;
import static cs.java.lang.CSLang.info;
import static cs.java.lang.CSLang.is;
import static cs.java.lang.CSLang.list;
import static cs.java.lang.CSLang.notImplemented;
import static cs.java.lang.CSLang.set;
import static cs.java.lang.CSLang.string;

public class CSResponse<Data> extends CSContextController {

    public static final String NO_INTERNET = "No Internet";

    private final CSEvent<CSResponse<Data>> _onDone = event();
    private final CSEvent<CSResponse<?>> _onFailed = event();
    private final CSEvent<CSResponse<Data>> _onSuccess = event();
    private final CSEvent<CSResponse<?>> _onSend = event();
    protected Data _data;
    protected boolean _cached;
    protected boolean _reload;
    protected boolean _useCache;
    private String _message;
    private boolean _done;
    private boolean _success;
    private boolean _failed;
    private boolean _canceled;
    private CSURL _url;
    private CSViewController _controller;
    private String _title;
    private boolean _sending;
    private CSList<String> _noReportMessage = list();
    private String _id;
    private CSResponse<?> _failedResponse;
    private Throwable _exception;
    private int _failedCount = 0;

    public CSResponse() {
    }

    public CSResponse(CSURL url) {
        _url = url;
    }

    public int failedCount() {
        return _failedCount;
    }

    public void setController(CSViewController controller) {
        _controller = controller;
    }

    public void addNoReportMessage(String string) {
        _noReportMessage.add(string);
    }

    public CSResponse<Data> onSuccess(final CSRunWith<Data> run) {
        _onSuccess.add(new CSListener<CSResponse<Data>>() {
            public void onEvent(EventRegistration registration, CSResponse<Data> arg) {
                if (is(run)) run.run(_data);
            }
        });
        return this;
    }

    public CSResponse<Data> cached(boolean cached) {
        _cached = cached;
        return this;
    }

    public CSResponse<Data> onSuccess(final CSViewController parent, final CSRunWith<Data> run) {
        _onSuccess.add(new CSListener<CSResponse<Data>>() {
            public void onEvent(EventRegistration registration, CSResponse<Data> arg) {
                if (parent.isResumed()) if (is(run)) run.run(_data);
            }
        });
        return this;
    }

    public CSResponse<Data> onSuccess(final CSRun run) {
        _onSuccess.add(new CSListener<CSResponse<Data>>() {
            public void onEvent(EventRegistration registration, CSResponse<Data> arg) {
                if (is(run)) run.run();
            }
        });
        return this;
    }

    public CSResponse<Data> onSuccess(final CSViewController parent, final CSRun run) {
        _onSuccess.add(new CSListener<CSResponse<Data>>() {
            public void onEvent(EventRegistration registration, CSResponse<Data> arg) {
                if (parent.isResumed()) if (is(run)) run.run();
            }
        });
        return this;
    }

    public CSResponse<Data> onFailed(final CSRunWith<CSResponse> run) {
        _onFailed.add(new CSListener<CSResponse<?>>() {
            public void onEvent(EventRegistration registration, CSResponse<?> arg) {
                if (is(run)) run.run((CSResponse) arg);
            }
        });
        return this;
    }

    public CSResponse<Data> onFailed(final CSViewController parent, final CSRun run) {
        _onFailed.add(new CSListener<CSResponse<?>>() {
            public void onEvent(EventRegistration registration, CSResponse<?> arg) {
                if (!parent.isDestroyed()) if (is(run)) run.run();
            }
        });
        return this;
    }

    public CSResponse<Data> onFailed(final CSRun run) {
        _onFailed.add(new CSListener<CSResponse<?>>() {
            public void onEvent(EventRegistration registration, CSResponse<?> arg) {
                if (is(run)) run.run();
            }
        });
        return this;
    }

    public CSResponse<Data> cache(boolean useCache) {
        _useCache = useCache;
        _cached = useCache;
        return this;
    }

    public void cancel() {
        info("Response cancel", this, "isCanceled", _canceled, "isDone", _done, "isSuccess", _success, "isFailed", _failed);
        if (_canceled || _done || _success || _failed) return;
        _canceled = YES;
        onDoneImpl();
    }

    protected final void onDoneImpl() {
        info("Response onDone", this);
        if (_done) throw exception("already done");
        _done = YES;
        _sending = NO;
        onDone();
        fire(_onDone, this);
    }

    protected void onDone() {
    }

    public CSResponse<Data> connect(CSResponse<Data> response) {
        return failIfFail(successIfSuccess(response));
    }

    public String content() {
        return "";
    }

    public Data data() {
        return _data;
    }

    public void failed(final Exception e, String message) {
        if (_canceled) return;
        _message = message;
        _exception = e;
        onFailedImpl(this);
        onDoneImpl();
    }

    protected final void onFailedImpl(CSResponse<?> response) {
        info("Response onFailedImpl", this, url());
        if (_done)
            throw exception("already done");
        if (_failed)
            throw exception("already failed");
        _failedResponse = response;
        _failed = YES;
        _sending = NO;
        if (set(response.getException()))
            _message = string(" ", response.message(), getRootCauseMessage(response.getException()));
        else _message = response.message();
        _exception = set(response.getException()) ? response.getException() : new Throwable();
        error(_exception, _message);
        fire(_onFailed, response);
        _failedCount++;
    }

    public CSURL url() {
        return _url;
    }

    public Throwable getException() {
        return _exception;
    }

    public String message() {
        return _message;
    }

    public CSResponse failedResponse() {
        return _failedResponse;
    }

    public final CSResponse<Data> failedWithMessage(final String message) {
        if (_canceled) return this;
        _message = message;
        failed(this);
        return this;
    }

    public final void failed(CSResponse response) {
        if (_canceled) return;
        onFailedImpl(response);
        onDoneImpl();
    }

    public <T> CSResponse<T> failIfFail(final CSResponse<T> response) {
        response.onFailed(new CSRunWith<CSResponse>() {
            public void run(CSResponse failedResponse) {
                failed(failedResponse);
            }
        });
        return response;
    }

    public String id() {
        return _id;
    }

    public CSResponse<Data> id(String id) {
        _id = id;
        return this;
    }

    public boolean isCanceled() {
        return _canceled;
    }

    public boolean isDone() {
        return _done;
    }

    public boolean isFailed() {
        return _failed;
    }

    public boolean isSending() {
        return _sending;
    }

    public boolean isShouldBeReported() {
        return !_noReportMessage.contains(message());
    }

    public boolean isSuccess() {
        return _success;
    }

    public CSResponse<Data> onDone(final CSRunWith<CSResponse<Data>> run) {
        _onDone.add(new CSListener<CSResponse<Data>>() {
            public void onEvent(EventRegistration registration, CSResponse<Data> arg) {
                if (is(run)) run.run(CSResponse.this);
            }
        });
        return this;
    }

    public CSResponse<Data> onDone(final CSViewController parent, final CSRun run) {
        _onDone.add(new CSListener<CSResponse<Data>>() {
            public void onEvent(EventRegistration registration, CSResponse<Data> arg) {
                if (parent.isResumed()) if (is(run)) run.run();
            }
        });
        return this;
    }

    public CSResponse<Data> onDone(final CSRun run) {
        _onDone.add(new CSListener<CSResponse<Data>>() {
            public void onEvent(EventRegistration registration, CSResponse<Data> arg) {
                if (is(run)) run.run();
            }
        });
        return this;
    }

    public CSResponse<Data> onSend(final CSRun run) {
        _onSend.add(new CSListener<CSResponse<?>>() {
            public void onEvent(EventRegistration registration, CSResponse<?> arg) {
                if (is(run)) run.run();
            }
        });
        return this;
    }

    public CSResponse<Data> onSend(final CSRunWith<CSResponse<Data>> run) {
        _onSend.add(new CSListener<CSResponse<?>>() {
            public void onEvent(EventRegistration registration, CSResponse<?> arg) {
                if (is(run)) run.run(CSResponse.this);
            }
        });
        return this;
    }

    public String postData() {
        return "";
    }

    public String title() {
        return _title;
    }

    public CSResponse<Data> title(String title) {
        _title = title;
        return this;
    }

    public CSResponse<Data> reload(boolean reload) {
        _reload = reload;
        return this;
    }

    public CSResponse<?> resend() {
        throw notImplemented();
    }

    public final CSResponse<Data> sending(CSResponse<?> response) {
        reset();
        _sending = YES;
        fire(_onSend, response);
        return this;
    }

    public void reset() {
        _exception = null;
        _message = "";
        _sending = NO;
        _done = NO;
        _success = NO;
        _failed = NO;
        _canceled = NO;
    }

    public void success() {
        if (_canceled) return;
        onSuccessImpl();
        onDoneImpl();
    }

    private void onSuccessImpl() {
        info("Response onSuccessImpl", this, url());
        if (_failed) throw exception("already failed");
        if (_success) throw exception("already success");
        if (_done) throw exception("already done");
        _success = YES;
        _sending = NO;
        onSuccess();
        fire(_onSuccess, this);
    }

    protected void onSuccess() {
    }

    public final void success(Data data) {
        if (_canceled) return;
        _data = data;
        onSuccessImpl();
        onDoneImpl();
    }

    public CSResponse<Data> successIfSuccess(final CSResponse<Data> response) {
        return response.onSuccess(new CSRunWith<Data>() {
            public void run(Data data) {
                success(data);
            }
        });
    }

    public String toString() {
        return string(" ", _title, string(_url));
    }

    protected CSViewController controller() {
        return _controller;
    }


}

