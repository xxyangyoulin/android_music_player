package com.mnnyang.starmusicapp.view;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.mnnyang.starmusicapp.R;
import com.mnnyang.starmusicapp.helper.MusicScaner;
import com.mnnyang.starmusicapp.helper.PermissionRequest;
import com.mnnyang.starmusicapp.model.Music;

import java.util.ArrayList;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity {
    @BindView(R.id.fff)
    TextView tv;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle state) {

        PermissionRequest.with(this).permissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                .request(new PermissionRequest.Callback() {
                    @Override
                    public void onGranted() {
                        Observable.create(new ObservableOnSubscribe<Music>() {
                            @Override
                            public void subscribe(ObservableEmitter<Music> e) throws Exception {
                                ArrayList<Music> list = new MusicScaner().scanLocalMusic(MainActivity.this);
                                for (Music music : list) {
                                    e.onNext(music);
                                }
                            }
                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Music>() {
                                    @Override
                                    public void accept(Music music) throws Exception {
                                        Log.e(TAG, "收到:" + music);
                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {
                                        Log.e(TAG, "错误" + throwable.getMessage());
                                    }
                                });
                    }

                    @Override
                    public void onDenied() {
                        Toast.makeText(MainActivity.this, "权限被拒绝", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionRequest.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
}
