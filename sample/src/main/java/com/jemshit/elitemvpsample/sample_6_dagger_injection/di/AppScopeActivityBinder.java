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

package com.jemshit.elitemvpsample.sample_6_dagger_injection.di;

import android.app.Activity;

import com.jemshit.elitemvpsample.sample_6_dagger_injection.Sample6Activity;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

@Module(subcomponents = {Sample6ActivityComponent.class})
public abstract class AppScopeActivityBinder {

    @Binds
    @IntoMap
    @ActivityKey(Sample6Activity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourActivityInjectorFactory(Sample6ActivityComponent.Builder builder);
}