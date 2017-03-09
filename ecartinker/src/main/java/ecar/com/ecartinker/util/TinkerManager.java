package ecar.com.ecartinker.util;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.tencent.tinker.lib.patch.AbstractPatch;
import com.tencent.tinker.lib.patch.UpgradePatch;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.lib.util.TinkerLog;
import com.tencent.tinker.loader.app.ApplicationLike;

import ecar.com.ecartinker.Log.MyLogImp;
import ecar.com.ecartinker.crash.UncaughtExceptionHandler;
import ecar.com.ecartinker.reporter.LoadReporter;
import ecar.com.ecartinker.reporter.PatchListener;
import ecar.com.ecartinker.reporter.PatchReporter;
import ecar.com.ecartinker.service.PatchResultService;

import static ecar.com.ecartinker.util.ApplicationContext.context;


/****************************************
 方法描述：
 @param
 @return
 ****************************************/
public class TinkerManager {
    private static final String TAG = "TinkerManager";
    private static final String isLoaded = "isloaded";    //是否加载过补丁
    public static final String pathName = "patch_";
    public static boolean isTinkerDebug = false;


    private static ApplicationLike applicationLike;
    private static Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
    private static boolean isInstalled = false;
    private static TinkerSpUtil spUtil;


    public static void setTinkerApplicationLike(ApplicationLike appLike) {
        applicationLike = appLike;

    }

    //初始化Tinker  tinkerDebug true为开发模式

    public static Tinker initTinker(ApplicationLike appLike, boolean tinkerDebug) {
        if (applicationLike == null) {
            isTinkerDebug = tinkerDebug;
            MultiDex.install(appLike.getApplication());
            TinkerManager.setTinkerApplicationLike(appLike);
//        TinkerManager.initFastCrashProtect();
            TinkerManager.setUpgradeRetryEnable(true);
            TinkerInstaller.setLogIml(new MyLogImp());
            TinkerManager.setUpgradeRetryEnable(true);
            TinkerManager.installTinker(appLike);
            return Tinker.with(appLike.getApplication());  //初始化tinker参数
        } else {
            return null;
        }
    }

    public static ApplicationLike getTinkerApplicationLike() {
        return applicationLike;
    }

    public static void initFastCrashProtect() {
        if (uncaughtExceptionHandler == null) {
            uncaughtExceptionHandler = new UncaughtExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
        }
    }

    public static void setUpgradeRetryEnable(boolean enable) {
        UpgradePatchRetry.getInstance(applicationLike.getApplication()).setRetryEnable(enable);
    }

    /****************************************
     方法描述：加载补丁
     @param  versionCode app版本号
     @return
     ****************************************/
    public static void onReceiveUpgradePatch(Context context, int versionCode) {
        TinkerInstaller.onReceiveUpgradePatch(context,
                TinkerFileUtil.getAllPatch().concat(pathName).concat(String.valueOf(versionCode)));
    }


    /**
     * all use default class, simply Tinker install method
     */
    public static void sampleInstallTinker(ApplicationLike appLike) {
        if (isInstalled) {
            TinkerLog.w(TAG, "install tinker, but has installed, ignore");
            return;
        }
        TinkerInstaller.install(appLike);
        isInstalled = true;
    }

    /**
     * you can specify all class you want.
     * sometimes, you can only install tinker in some process you want!
     *
     * @param appLike
     */
    public static void installTinker(ApplicationLike appLike) {
        if (isInstalled) {
            TinkerLog.w(TAG, "install tinker, but has installed, ignore");
            return;
        }
        //初始化补丁保存路径
        TinkerFileUtil.createDir(TinkerFileUtil.getPatchDir(), context);
        //初始化sp文件
        spUtil = new TinkerSpUtil(applicationLike.getApplication(), TAG);

        //or you can just use DefaultLoadReporter
        LoadReporter loadReporter = new LoadReporter(appLike.getApplication());
        //or you can just use DefaultPatchReporter
        PatchReporter patchReporter = new PatchReporter(appLike.getApplication());
        //or you can just use DefaultPatchListener
        PatchListener patchListener = new PatchListener(appLike.getApplication());
        //you can set your own upgrade patch if you need
        AbstractPatch upgradePatchProcessor = new UpgradePatch();

        TinkerInstaller.install(appLike,
                loadReporter, patchReporter, patchListener,
                PatchResultService.class, upgradePatchProcessor);

        isInstalled = true;
    }

    //清除patch
    public static void cleanPatch(Context context) {
        TinkerInstaller.cleanPatch(context);

    }

    //获取补丁路径
    public static String getPathDir(Context context) {
        if (TextUtils.isEmpty(TinkerFileUtil.getAllPatch()))
            TinkerFileUtil.createDir(TinkerFileUtil.getPatchDir(), context);
        return TinkerFileUtil.getAllPatch();
    }

    // 判断是否安装过补丁
    public static boolean isLoaded() {
        return (boolean) spUtil.getData(isLoaded, Boolean.class, false);
    }

    // 判断是否安装过补丁     state true 加载成功
    public static void saveLoadState(boolean state) {
        spUtil.save(isLoaded, state);
    }
}
