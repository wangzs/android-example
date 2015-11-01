// IAidlInterface.aidl
package com.wangzs.android.aidl;

// Declare any non-default types here with import statements
import com.wangzs.android.aidl.IAidlListener;

interface IAidlInterface {
    // client端调用server端的功能
    void normalFunc();

    // client端设置回调，server端因某事件触发回调
    void setAidlListener(IAidlListener listener);
}
