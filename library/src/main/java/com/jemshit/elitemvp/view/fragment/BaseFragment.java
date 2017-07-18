/*
 * Copyright (c) 2017 Hoang Hiep.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.jemshit.elitemvp.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jemshit.elitemvp.RxBus;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hoanghiep on 7/18/17.
 */

public abstract class BaseFragment extends Fragment {
    private Unbinder mUnbinder;
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
        mUnbinder = ButterKnife.bind(this, view);
        bindEventHandlers(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }

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
