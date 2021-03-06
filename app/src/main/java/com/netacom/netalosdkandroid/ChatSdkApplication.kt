/*
 * *Created by NetaloTeamAndroid on 2020
 * Company: Netacom.
 *  *
 */

package com.netacom.netalosdkandroid

import android.app.Application
import android.content.Context
import androidx.work.Configuration
import com.netacom.full.ui.sdk.NetAloSDK
import com.netacom.full.ui.sdk.NetAloSdkCore
import com.netacom.lite.entity.ui.theme.NeTheme
import com.netacom.lite.sdk.AccountKey
import com.netacom.lite.sdk.AppID
import com.netacom.lite.sdk.AppKey
import com.netacom.lite.sdk.SdkConfig
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm
import javax.inject.Inject

/**Created by vantoan on 23,July,2020
Company: Netacom.
Email: huynhvantoan.itc@gmail.com
 */

@HiltAndroidApp
class ChatSdkApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var netAloSdkCore: NetAloSdkCore

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(netAloSdkCore.workerFactory)
            .build()

    private var neTheme = NeTheme(
        mainColor = "#f5783f",
        subColorLight = "#F9D9C9",
        subColorDark = "#683A00",
        toolbarDrawable = "#f5783f",
    )

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        Realm.init(this)
    }

    override fun onCreate() {
        super.onCreate()
        NetAloSDK.initNetAloSDK(
            context = this,
            netAloSdkCore = netAloSdkCore,
            sdkConfig = SdkConfig(
                appId = AppID.NETALO_DEV,
                appKey = AppKey.NETALO_DEV,
                accountKey = AccountKey.NETALO_DEV,
                isSyncContact = false,
                hidePhone = true,
                hideCreateGroup = true,
                hideAddInfoInChat = true,
                hideInfoInChat = true,
                hideCallInChat = true,
                classMainActivity = MainActivity::class.java.name
            ),
            neTheme = neTheme
        )
    }
}
