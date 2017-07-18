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

package com.jemshit.elitemvp;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by hoanghiep on 7/18/17.
 */

public class RxBus {
    private static volatile RxBus ourInstance = null;
    private final Relay<Object> relay;

    private RxBus() {
        relay = PublishRelay.create().toSerialized();
    }

    public static RxBus getInstance() {
        if (ourInstance == null) {
            synchronized (RxBus.class) {
                if (ourInstance == null) {
                    ourInstance = new RxBus();
                }
            }
        }
        return ourInstance;
    }

    /**
     * Sent event with RxJava
     *
     * @param event
     */
    public synchronized void sendEvent(Object event) {
        relay.accept(event);
    }

    /**
     * Receive event
     *
     * @param clazz
     * @param consumer
     * @param <T>
     * @return object event
     */
    public <T> Disposable receiveEvent(final Class<T> clazz, Consumer<T> consumer) {
        return receive(clazz).subscribe(consumer);
    }

    public <T> Observable<T> receive(final Class<T> clazz) {
        return receive().ofType(clazz);
    }

    public synchronized Observable<Object> receive() {
        return relay;
    }
}
