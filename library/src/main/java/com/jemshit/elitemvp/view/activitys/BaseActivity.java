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

package com.jemshit.elitemvp.view.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

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

public abstract class BaseActivity extends AppCompatActivity {
    private Unbinder mUnbinder;
    private CompositeDisposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
        disposable = new CompositeDisposable();
        mUnbinder = ButterKnife.bind(this);
        onSubscribeEventRx();
        initOnCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }

        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable.clear();
        }
    }

    protected abstract int getLayoutID();

    protected abstract void initOnCreate(Bundle savedInstanceState);

    protected abstract void onSubscribeEvent(Object object);

    private synchronized void onSubscribeEventRx() {
        disposable.add(RxBus.getInstance()
                .receive()
                .subscribeOn(Schedulers.io())
                .delay(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object object) throws Exception {
                        onSubscribeEvent(object);
                    }
                }));
    }
}
