package cs.android.view;

import cs.android.viewbase.CSViewInterface;

/**
 * Created by renetik on 05/12/17.
 */

public interface CSPagerPageInterface extends CSViewInterface {
    default String csPagerControllerTitle() {
        return null;
    }

    default Integer csPagerControllerImage() {
        return null;
    }
}
