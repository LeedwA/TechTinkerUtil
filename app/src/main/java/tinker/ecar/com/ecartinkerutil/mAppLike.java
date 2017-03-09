package tinker.ecar.com.ecartinkerutil;

import android.app.Application;
import android.content.Intent;

import com.tencent.tinker.anno.DefaultLifeCycle;
import com.tencent.tinker.loader.app.DefaultApplicationLike;
import com.tencent.tinker.loader.shareutil.ShareConstants;

import ecar.com.ecartinker.util.TinkerManager;

/*************************************
 功能：
 创建者： kim_tony
 创建日期：2017/3/8
 版权所有：深圳市亿车科技有限公司
 *************************************/

@SuppressWarnings("unused")
@DefaultLifeCycle(application = "tinker.app.mApplication",
        flags = ShareConstants.TINKER_ENABLE_ALL,
        loadVerifyFlag = false)
public class mAppLike extends DefaultApplicationLike {
    public mAppLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        TinkerManager.initTinker(this, true);
    }
}
