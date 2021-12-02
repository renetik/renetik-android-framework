package renetik.android.client.okhttp3

import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.DownloadListener
import renetik.android.client.request.CSProcessBase
import java.io.File

class CSDownloadRequest(url: String, file: File) : CSProcessBase<File>(file) {
    init {
        AndroidNetworking.download(url, file.parent, file.name).build()
            .startDownload(object : DownloadListener {
                override fun onDownloadComplete() {
                    success()
                }

                override fun onError(error: ANError) {
                    failed(error, "errorCode:${error.errorCode}," +
                            "errorDetail:${error.errorDetail},errorBody:${error.errorBody}")
                }
            })
    }
}