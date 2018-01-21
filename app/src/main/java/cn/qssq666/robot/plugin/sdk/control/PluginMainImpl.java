package cn.qssq666.robot.plugin.sdk.control;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import cn.qssq666.plugin.demo.BuildConfig;
import cn.qssq666.robot.bean.MsgItem;
import cn.qssq666.robot.plugin.sdk.interfaces.PluginCtronolInterface;
import cn.qssq666.robot.plugin.sdk.interfaces.PluginInterface;


/**
 * Created by qssq on 2018/1/21 qssq666@foxmail.com
 * 具体逻辑在这里写
 * 本方法所有实现以及jarsdk包 不可以进行混淆，否则会导致识别出错.
 */

public class PluginMainImpl implements PluginInterface {


    private static final String SP_ENABLE = BuildConfig.APPLICATION_ID + "_SP_ENABLE";
    private boolean mEnable;
    private SharedPreferences defaultSharedPreferences;
    private PluginCtronolInterface apiInterface;

    @Override
    public String getAuthorName() {
        return "机器人测试插件";
    }

    @Override
    public void onReceiveControlApi(PluginCtronolInterface apiInterface) {
        this.apiInterface = apiInterface;
        /**
         * 收到之后自己存储起来方便进行控制
         */
    }

    @Override
    public int getVersionCode() {
        return BuildConfig.VERSION_CODE;
    }

    @Override
    public String getVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    @Override
    public String getPackageName() {
        return BuildConfig.APPLICATION_ID;
    }

    @Override
    public String getDescript() {
        return "情迁QQ机器人插件你值得为它开发";
    }

    @Override
    public String getPluginName() {
        return "测试Demo插件SDK";
    }

    @Override
    public boolean isDisable() {
        return mEnable;
    }

    @Override
    public void setDisable(boolean disable) {
        mEnable = disable;
        SharedPreferences.Editor edit = defaultSharedPreferences.edit();
        edit.putBoolean(SP_ENABLE, disable);
        edit.apply();
    }

    @Override
    public void onCreate(Context context) {

        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mEnable = defaultSharedPreferences.getBoolean(SP_ENABLE, true);
    }

    /**
     * 处理消息.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onReceiveMsgIsNeedIntercept(MsgItem item) {

        if (apiInterface.isPrivateMsg(item)) {
            if (item.getMessage().contains("拦截插件")) {
                item.setMessage("本消息已经被" + BuildConfig.APPLICATION_ID + "包插件拦截");
                apiInterface.sendQQMsg(item);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDestory() {

    }


}
