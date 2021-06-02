package com.netacom.netalosdkandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import bolts.Task.delay
import com.google.android.material.snackbar.Snackbar
import com.netacom.base.chat.binding.clickDebounce
import com.netacom.base.chat.logger.Logger
import com.netacom.full.ui.sdk.NetAloSDK
import com.netacom.lite.config.EndPoint
import com.netacom.lite.define.ErrorCodeDefine
import com.netacom.lite.define.GalleryType
import com.netacom.lite.entity.ui.local.LocalFileModel
import com.netacom.lite.entity.ui.theme.NeTheme
import com.netacom.lite.entity.ui.user.NeUser
import com.netacom.lite.network.model.response.SettingResponse
import com.netacom.lite.util.CallbackResult
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

@ExperimentalCoroutinesApi
@FlowPreview
class MainActivity : AppCompatActivity() {

    private val user8 = NeUser(id = 281474976981354, token = "9c98a74053f60334758b9dd82d5e8dbe77b5d2b6", username = "Toan 0000")

    private val user9 = NeUser(id = 281474977755104, token = "28eccfda4d9c9cd78d2b775fce4464cb2a24a4ec", username = "Toan 1111")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        resultListener()
        findViewById<AppCompatButton>(R.id.btnSdkThemeGreen).clickDebounce {
            NetAloSDK.initTheme(
                NeTheme(
                    mainColor = "#00B14F",
                    subColorLight = "#D6F3E2",
                    subColorDark = "#683A00",
                    toolbarDrawable = "#00B14F"
                )
            )
            NetAloSDK.initSetting(
                settingResponse = SettingResponse(
                    apiEndpoint = EndPoint.URL_API,
                    cdnEndpoint = EndPoint.URL_CDN,
                    chatEndpoint = EndPoint.URL_SOCKET,
                    turnserverEndpoint = EndPoint.URL_TURN
                )
            )
        }
        findViewById<AppCompatButton>(R.id.btnSdkThemeOrange).clickDebounce {
            NetAloSDK.initTheme(
                NeTheme(
                    mainColor = "#f5783f",
                    subColorLight = "#4df5783f",
                    subColorDark = "#683A00",
                    toolbarDrawable = "#f5783f"
                )
            )
        }
        findViewById<AppCompatButton>(R.id.btnSdk).clickDebounce {
            NetAloSDK.setNetAloUser(user8)
        }
        findViewById<AppCompatButton>(R.id.btnSdkOpenChatChange).clickDebounce {
            NetAloSDK.setNetAloUser(user9)
        }
        findViewById<AppCompatButton>(R.id.btnSdkOpen).clickDebounce {
            NetAloSDK.openNetAloSDK(this)
            MainScope().launch {
                delay(5000)
                // NetAloSDK.getListContactLocal()
            }
        }
        findViewById<AppCompatButton>(R.id.btnSdkOpenChat).clickDebounce {
            NetAloSDK.openNetAloSDK(this, user9)
        }

        findViewById<AppCompatButton>(R.id.btnBlockUser).clickDebounce {
            NetAloSDK.blockUser(userId = user9.id, isBlock = true, callbackResult = object : CallbackResult<Boolean> {
                override fun callBackSuccess(result: Boolean) {
                    Logger.e("btnBlockUser:callBackSuccess=")
                }

                override fun callBackError(error: String?) {
                    Logger.e("btnBlockUser:callBackError=")
                }

            })
        }

        findViewById<AppCompatButton>(R.id.btnCustomUser).clickDebounce {
            val userId = findViewById<AppCompatEditText>(R.id.editId).text.toString().toLongOrNull()
            val token = findViewById<AppCompatEditText>(R.id.editToken).text.toString()
            if (userId == null || token.isEmpty()) {
                Snackbar.make(findViewById(R.id.view), "Mời nhập ID và token để set User custom!", Snackbar.LENGTH_LONG).show()
                return@clickDebounce
            }
            NetAloSDK.setNetAloUser(
                neUser = NeUser(
                    id = userId,
                    token = token,
                    username = findViewById<AppCompatEditText>(R.id.editName).text.toString(),
                    avatar = findViewById<AppCompatEditText>(R.id.editAvatar).text.toString(),
                    email = findViewById<AppCompatEditText>(R.id.editEmail).text.toString(),
                )
            )
        }

        findViewById<AppCompatButton>(R.id.btnSdkLogOut).clickDebounce {
            //NetAloSDK.netAloEvent?.send(LocalFileModel(filePath = ""))
            val map: MutableMap<String, String> = mutableMapOf()
            map["test"] = "data"
            NetAloSDK.eventFireBase(map)
        }

        findViewById<AppCompatButton>(R.id.btnSdkListContact).clickDebounce {
            NetAloSDK.getListContactFromServer { listContact ->
                Logger.e("listContact=" + listContact.map { it })
            }
        }

        findViewById<AppCompatButton>(R.id.btnStartActivitySdk).clickDebounce {
            NetAloSDK.openGallery(context = this, maxSelections = 1, autoDismissOnMaxSelections = false, galleryType = GalleryType.GALLERY_ALL)
        }
    }

    private fun resultListener() {
        CoroutineScope(Dispatchers.Default).launch {
            NetAloSDK.netAloEvent?.receive<ArrayList<LocalFileModel>>()?.collect { listPhoto ->
                Logger.e("SELECT_PHOTO_VIDEO==$listPhoto")
            }
        }

        CoroutineScope(Dispatchers.Default).launch {
            NetAloSDK.netAloEvent?.receive<LocalFileModel>()?.collect { document ->
                Logger.e("SELECT_FILE==$document")
            }

        }

        CoroutineScope(Dispatchers.Default).launch {
            NetAloSDK.netAloEvent?.receive<Map<String, String>>()?.collect { notification ->
                Logger.e("Notification:data==$notification")
            }
        }

        CoroutineScope(Dispatchers.Default).launch {
            NetAloSDK.netAloEvent?.receive<Int>()?.collect { errorEvent ->
                Logger.e("Event:==$errorEvent")
                when (errorEvent) {
                    ErrorCodeDefine.ERRORCODE_FAILED_VALUE -> {
                        Logger.e("Event:Socket error")
                    }
                    ErrorCodeDefine.ERRORCODE_EXPIRED_VALUE -> {
                        Logger.e("Event:Session expired")
                    }
                    ErrorCodeDefine.ERRORCODE_LOGIN_CONFLICT_VALUE -> {
                        Logger.e("Event:Login conflict")
                    }
                }
            }
        }
    }
}