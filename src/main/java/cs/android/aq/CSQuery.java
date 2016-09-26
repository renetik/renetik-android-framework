package cs.android.aq;

import android.content.Context;

import com.androidquery.AQuery;
import com.androidquery.AbstractAQuery;
import com.androidquery.callback.ImageOptions;

import java.io.File;

import cs.android.viewbase.CSView;
import cs.android.viewbase.CSViewController;
import cs.android.viewbase.ContextController;

public class CSQuery extends AbstractAQuery<CSQuery> {

    public CSQuery(ContextController controller) {
        super(controller.context());
    }

    public CSQuery(CSViewController controller) {
        super(controller.activity());
    }

    public CSQuery(CSView widget) {
        super(widget.asView());
    }

    public CSQuery(Context context) {super(context);}

    public CSQuery gone(boolean gone) {
        if (gone) gone();
        else visible();
        return this;
    }

    public CSQuery image(File file) {
        if (view != null) image(file, view.getWidth());
        return this;
    }

    public ImageOptions imageOptions(int widthInt) {
        ImageOptions options = new ImageOptions();
        options.animation = AQuery.FADE_IN;
        options.targetWidth = widthInt;
        return options;
    }

    public int width() {
        return getView().getWidth();
    }

}