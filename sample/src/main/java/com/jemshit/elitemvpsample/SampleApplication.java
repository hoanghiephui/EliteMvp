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

package com.jemshit.elitemvpsample;

import android.app.Activity;
import android.app.Application;

//import com.jemshit.elitemvpsample.sample_6_dagger_injection.di.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.DaggerApplication;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

// All of these are just for Dagger 2
public class SampleApplication extends Application implements HasActivityInjector {
    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        /*DaggerAppComponent
                .builder()
                .application(this)
                .build()
                .inject(this);*/
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
