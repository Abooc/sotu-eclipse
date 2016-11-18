package com.abooc.sotu.home;

import com.abooc.sotu.BasePresenter;
import com.abooc.sotu.BaseView;
import com.abooc.sotu.modle.Image;
import com.abooc.sotu.modle.ImageCategory;

import java.util.List;

/**
 * Created by dayu on 2016/11/10.
 */

public interface HomeContract {


    interface View extends BaseView<Presenter> {

        void showInfo(ImageCategory category);

        void showImages(List<Image> list);
    }

    interface Presenter extends BasePresenter {

        void load();
    }

}
