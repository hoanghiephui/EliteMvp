package com.jemshit.elitemvp;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by hoanghiep on 8/3/17.
 */

public abstract class BaseActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private static final String TAG = BaseActivity.class.getSimpleName();
    public static String[] PERMISSIONS_ALL = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.RECEIVE_SMS,
            Manifest.permission.CAMERA
    };
    public static final int REQUEST_ALL = 123;
    private CompositeDisposable disposable;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutID() != 0) {
            setContentView(getLayoutID());
        }
        initPresenter();
        attachView();
        disposable = new CompositeDisposable();
        onSubscribeEventRx();
        initOnCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable.clear();
        }
        onDestroyPresenter();
    }

    protected abstract int getLayoutID();

    /**
     * Initialize Presenter
     */
    protected void initPresenter() {
    }

    /**
     * Attach View to it
     */
    protected void attachView() {
    }


    protected abstract void initOnCreate(Bundle savedInstanceState);

    /**
     * Subsrcibe event rx
     *
     * @param object
     */
    protected void onSubscribeEvent(Object object) {
    }

    /**
     * Destroy (Detach View from) Presenter. Also unsubscribes from Subscriptions
     */
    protected void onDestroyPresenter() {
    }

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //callback permission
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsGranted: " + requestCode);
        for (String perm : perms) {
            Log.d(TAG, "onPermissionsGranted: perm " + perm);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        for (String perm : perms) {
            Log.d(TAG, "onPermissionsDenied: " + perm);
        }
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
    //end callback permission

    @AfterPermissionGranted(REQUEST_ALL)
    public void requestPermissions() {
        if (EasyPermissions.hasPermissions(this, PERMISSIONS_ALL)) {
            // Have permission, do the thing!
            Toast.makeText(this, "TODO: Camera things", Toast.LENGTH_LONG).show();
        } else {
            // Ask for one permission
            EasyPermissions.requestPermissions(this, "check",
                    REQUEST_ALL, PERMISSIONS_ALL);
        }
    }

    public static Fragment openFragment(FragmentManager manager,
                                        FragmentTransaction transaction,
                                        Class<? extends BaseFragment> clazz,
                                        Bundle bundle,
                                        boolean hasAddbackstack,
                                        boolean hasCommitTransaction,
                                        AnimationSwitchScreen animations,
                                        int fragmentContent) {
        String tag = clazz.getName();
        Fragment fragment;
        try {
            //if added backstack
            fragment = manager.findFragmentByTag(tag);
            if (hasAddbackstack) {
                if (fragment == null || !fragment.isAdded()) {
                    fragment = clazz.newInstance();
                    fragment.setArguments(bundle);
                    if (animations != null) {
                        transaction.setCustomAnimations(animations.getOpenEnter(),
                                animations.getOpenExit(),
                                animations.getCloseEnter(),
                                animations.getCloseExit());
                    }
                    transaction.add(fragmentContent, fragment, tag);
                } else {
                    transaction.show(fragment);
                }
                transaction.addToBackStack(tag);

            } else {
                if (fragment != null) {
                    if (animations != null) {
                        transaction.setCustomAnimations(animations.getOpenEnter(),
                                animations.getOpenExit(),
                                animations.getCloseEnter(),
                                animations.getCloseExit());
                    }
                    transaction.show(fragment);
                } else {
                    fragment = clazz.newInstance();
                    fragment.setArguments(bundle);
                    if (animations != null) {
                        transaction.setCustomAnimations(animations.getOpenEnter(),
                                animations.getOpenExit(),
                                animations.getCloseEnter(),
                                animations.getCloseExit());
                    }
                    transaction.add(fragmentContent, fragment, tag);
                }
            }

            if (hasCommitTransaction) {
                commitTransactionFragment(transaction);
            }
            return fragment;

        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void hideFragment(FragmentManager manager,
                                    FragmentTransaction transaction,
                                    AnimationSwitchScreen animation,
                                    boolean hasAddBackstack,
                                    boolean hasCommit,
                                    String tag) {
        BaseFragment fragment = (BaseFragment) manager.findFragmentByTag(tag);
        if (fragment != null && fragment.isVisible()) {
            transaction.setCustomAnimations(animation.getOpenEnter(),
                    animation.getOpenExit(),
                    animation.getCloseEnter(),
                    animation.getCloseExit());
            transaction.hide(fragment);
            if (hasAddBackstack) {
                transaction.addToBackStack(tag);
            }
            if (hasCommit) {
                commitTransactionFragment(transaction);
            }
        }
    }

    public static void removeFragment(FragmentManager manager,
                                      FragmentTransaction transaction,
                                      AnimationSwitchScreen animation,
                                      boolean hasAddBackStack,
                                      boolean hasCommit,
                                      String tag) {
        BaseFragment fragment = (BaseFragment) manager.findFragmentByTag(tag);
        if (fragment != null) {
            if (fragment.isVisible()) {
                transaction.setCustomAnimations(animation.getOpenEnter(),
                        animation.getOpenExit(),
                        animation.getCloseEnter(),
                        animation.getCloseExit());
            }
            transaction.remove(fragment);
            if (hasAddBackStack) {
                transaction.addToBackStack(tag);
            }
            if (hasCommit) {
                commitTransactionFragment(transaction);
            }
        }
    }

    public static void commitTransactionFragment(FragmentTransaction transaction) {
        transaction.commit();
    }
}
