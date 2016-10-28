package cs.android.rpc;

import cs.android.viewbase.CSContextController;
import cs.android.viewbase.CSViewController;
import cs.java.callback.Run;
import cs.java.callback.RunWith;
import cs.java.collections.CSList;
import cs.java.event.CSEvent;
import cs.java.event.CSEvent.EventRegistration;
import cs.java.event.CSListener;
import cs.java.net.Url;

import static cs.java.lang.Lang.NO;
import static cs.java.lang.Lang.YES;
import static cs.java.lang.Lang.error;
import static cs.java.lang.Lang.event;
import static cs.java.lang.Lang.exception;
import static cs.java.lang.Lang.fire;
import static cs.java.lang.Lang.getRootCauseMessage;
import static cs.java.lang.Lang.info;
import static cs.java.lang.Lang.is;
import static cs.java.lang.Lang.list;
import static cs.java.lang.Lang.notImplemented;
import static cs.java.lang.Lang.set;
import static cs.java.lang.Lang.string;

public class Response<Data> extends CSContextController {

    public static final String NO_INTERNET = "No Internet";

    private final CSEvent<Response<Data>> _onDone = event();
    private final CSEvent<Response<?>> _onFailed = event();
    private final CSEvent<Response<Data>> _onSuccess = event();
    private final CSEvent<Response<?>> _onSend = event();
    protected Data _data;
    protected boolean _cached;
    protected boolean _reload;
    protected boolean _useCache;
    private String _message;
    private boolean _done;
    private boolean _success;
    private boolean _failed;
    private boolean _canceled;
    private Url _url;
    private CSViewController _controller;
    private String _title;
    private boolean _sending;
    private CSList<String> _noReportMessage = list();
    private String _id;
    private Response<?> _failedResponse;
    private Throwable _exception;
    private int _failedCount = 0;

    public Response() {
    }

    public Response(Url url) {
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

    public Response<Data> onSuccess(final RunWith<Data> run) {
        _onSuccess.add(new CSListener<Response<Data>>() {
            public void onEvent(EventRegistration registration, Response<Data> arg) {
                if (is(run)) run.run(_data);
            }
        });
        return this;
    }

    public Response<Data> onSuccess(final CSViewController parent, final RunWith<Data> run) {
        _onSuccess.add(new CSListener<Response<Data>>() {
            public void onEvent(EventRegistration registration, Response<Data> arg) {
                if (parent.isResumed()) if (is(run)) run.run(_data);
            }
        });
        return this;
    }

    public Response<Data> onSuccess(final Run run) {
        _onSuccess.add(new CSListener<Response<Data>>() {
            public void onEvent(EventRegistration registration, Response<Data> arg) {
                if (is(run)) run.run();
            }
        });
        return this;
    }

    public Response<Data> onSuccess(final CSViewController parent, final Run run) {
        _onSuccess.add(new CSListener<Response<Data>>() {
            public void onEvent(EventRegistration registration, Response<Data> arg) {
                if (parent.isResumed()) if (is(run)) run.run();
            }
        });
        return this;
    }

    public Response<Data> onFailed(final RunWith<Response> run) {
        _onFailed.add(new CSListener<Response<?>>() {
            public void onEvent(EventRegistration registration, Response<?> arg) {
                if (is(run)) run.run((Response) arg);
            }
        });
        return this;
    }

    public Response<Data> onFailed(final CSViewController parent, final Run run) {
        _onFailed.add(new CSListener<Response<?>>() {
            public void onEvent(EventRegistration registration, Response<?> arg) {
                if (parent.isResumed()) if (is(run)) run.run();
            }
        });
        return this;
    }

    public Response<Data> onFailed(final Run run) {
        _onFailed.add(new CSListener<Response<?>>() {
            public void onEvent(EventRegistration registration, Response<?> arg) {
                if (is(run)) run.run();
            }
        });
        return this;
    }

    public Response<Data> cache(boolean useCache) {
        _useCache = useCache;
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

    public Response<Data> connect(Response<Data> response) {
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

    protected final void onFailedImpl(Response<?> response) {
        info("Response onFailedImpl", this, url());
        if (_done) throw exception("already done");
        if (_failed) throw exception("already failed");
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

    public Url url() {
        return _url;
    }

    public Throwable getException() {
        return _exception;
    }

    public String message() {
        return _message;
    }

    public Response failedResponse() {
        return _failedResponse;
    }

    public final Response<Data> failedWithMessage(final String message) {
        if (_canceled) return this;
        _message = message;
        failed(this);
        return this;
    }

    public final void failed(Response response) {
        if (_canceled) return;
        onFailedImpl(response);
        onDoneImpl();
    }

    public <T> Response<T> failIfFail(final Response<T> response) {
        response.onFailed(new RunWith<Response>() {
            public void run(Response failedResponse) {
                failed(failedResponse);
            }
        });
        return response;
    }

    public String id() {
        return _id;
    }

    public Response<Data> id(String id) {
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

    public Response<Data> onDone(final RunWith<Response<Data>> run) {
        _onDone.add(new CSListener<Response<Data>>() {
            public void onEvent(EventRegistration registration, Response<Data> arg) {
                if (is(run)) run.run(Response.this);
            }
        });
        return this;
    }

    public Response<Data> onDone(final CSViewController parent, final Run run) {
        _onDone.add(new CSListener<Response<Data>>() {
            public void onEvent(EventRegistration registration, Response<Data> arg) {
                if (parent.isResumed()) if (is(run)) run.run();
            }
        });
        return this;
    }

    public Response<Data> onDone(final Run run) {
        _onDone.add(new CSListener<Response<Data>>() {
            public void onEvent(EventRegistration registration, Response<Data> arg) {
                if (is(run)) run.run();
            }
        });
        return this;
    }

    public Response<Data> onSend(final Run run) {
        _onSend.add(new CSListener<Response<?>>() {
            public void onEvent(EventRegistration registration, Response<?> arg) {
                if (is(run)) run.run();
            }
        });
        return this;
    }

    public Response<Data> onSend(final RunWith<Response<Data>> run) {
        _onSend.add(new CSListener<Response<?>>() {
            public void onEvent(EventRegistration registration, Response<?> arg) {
                if (is(run)) run.run(Response.this);
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

    public Response<Data> title(String title) {
        _title = title;
        return this;
    }

    public Response<Data> reload(boolean reload) {
        _reload = reload;
        return this;
    }

    public Response<?> resend() {
        throw notImplemented();
    }

    public final Response<Data> sending(Response<?> response) {
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

    public Response<Data> successIfSuccess(final Response<Data> response) {
        return response.onSuccess(new RunWith<Data>() {
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

