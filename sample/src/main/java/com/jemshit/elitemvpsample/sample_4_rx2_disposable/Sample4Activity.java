/*
 * Copyright (c) 2017 Jemshit Iskanderov.
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

package com.jemshit.elitemvpsample.sample_4_rx2_disposable;

import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jemshit.elitemvp.RxBus;
import com.jemshit.elitemvp.view.activitys.BaseActivity;
import com.jemshit.elitemvpsample.R;

public class Sample4Activity extends BaseActivity implements Sample4Contract.View {
    private static final String TAG = Sample4Activity.class.getSimpleName();
    private TextView textView;

    private Sample4Contract.Presenter presenter;

    // Called by Presenter
    @Override
    @SuppressWarnings("SetTextI18n")
    public void showList(String item) {
        textView.setText(textView.getText() + "\n" + item);
    }

    // Destroy (Detach View from) Presenter. Also unsubscribes from Subscriptions
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_sample_rx;
    }

    @Override
    protected void initPresenter() {
        // Initialize Presenter
        presenter = new Sample4Presenter();
    }

    @Override
    protected void attachView() {
        // Attach View to it
        presenter.attachView(this);
    }

    @Override
    protected void initOnCreate(Bundle savedInstanceState) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.example_rx2));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        // FindViewByIds, ClickListeners
        textView = (TextView) findViewById(R.id.text_sampleRx_list);
        AppCompatButton buttonGenerate = (AppCompatButton) findViewById(R.id.button_sampleRx_generate);
        buttonGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Call Presenter Method
                presenter.createList();
                RxBus.getInstance().sendEvent("a");
            }
        });
    }

    @Override
    protected void onSubscribeEvent(Object object) {
        if (object == "a") {
            Toast.makeText(this, "A", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onSubscribeEvent: ");
        }
    }

    @Override
    protected void onDestroyPresenter() {
        presenter.onDestroy();
    }

}