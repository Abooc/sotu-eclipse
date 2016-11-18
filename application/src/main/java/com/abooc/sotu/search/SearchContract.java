package com.abooc.sotu.search;

import com.abooc.sotu.BasePresenter;
import com.abooc.sotu.BaseView;
import com.abooc.sotu.modle.Image;
import com.abooc.sotu.modle.ImageCategory;
import com.abooc.sotu.modle.SearchResult;

import java.util.List;

/**
 * Created by dayu on 2016/11/10.
 */

public interface SearchContract {


    interface View extends BaseView<Presenter> {

        void showInfo(SearchResult category);

        void showImages(List<Image> list);
    }

    interface Presenter extends BasePresenter {

        void load(String word);
    }

}
