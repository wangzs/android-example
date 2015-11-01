package com.wangzs.android.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;
import com.wangzs.android.aidl.IAidlInterface;
import com.wangzs.android.aidl.IAidlListener;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by wangzs on 2015/11/1.
 */
public class AidlService extends Service {
    private IAidlListener mListener;

    private final IAidlInterface.Stub mBinder = new IAidlInterface.Stub() {
        @Override
        public void normalFunc() throws RemoteException {
            Logger.d("====> [server] normalFunc");
        }

        @Override
        public void setAidlListener(IAidlListener listener) throws RemoteException {
            mListener = listener;

            // 设置listener之后2秒，server端触发监听(根据实际逻辑触发)
            Observable.timer(2, TimeUnit.SECONDS)
                    .subscribe(new Subscriber<Long>() {
                        @Override
                        public void onCompleted() {
                            Logger.d("====> [server] onCompleted");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Logger.d("====> [server] onError");
                        }

                        @Override
                        public void onNext(Long aLong) {
                            Logger.d("====> [server] onNext");
                            try {
                                mListener.handleX();
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }

                            // mListener.handleY();
                        }
                    });
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Logger.d("====> [server] onBind server service");
        return mBinder;
    }
}
