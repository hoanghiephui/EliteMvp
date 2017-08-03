package com.jemshit.elitemvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hoanghiep on 8/3/17.
 */

public abstract class BaseFragment extends Fragment {
    private CompositeDisposable disposable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        disposable = new CompositeDisposable();
        injectDependences();
        attachView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getFragmentLayout(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindEventHandlers(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable.clear();
        }
        super.onDestroy();
    }

    /**
     * Request dependencies. Can be used on a constructor, a field, or a method
     */
    protected abstract void injectDependences();

    /**
     * attach view on the presenter
     */
    protected abstract void attachView();

    protected abstract int getFragmentLayout();

    protected abstract void bindEventHandlers(View view, Bundle savedInstanceState);

    protected abstract void onSubscribeEvent(Object object);

    private synchronized void onSubscribeEventRx() {
        disposable.add(RxBus.getInstance()
                .receive().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .delay(300, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object object) throws Exception {
                        onSubscribeEvent(object);
                    }
                }));
    }
}
