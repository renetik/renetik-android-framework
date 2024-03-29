package renetik.android.network.okhttp3

import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.DownloadListener
import renetik.android.event.common.CSHasDestruct
import renetik.android.event.process.CSProcess
import java.io.File

class CSDownloadRequest(
    parent: CSHasDestruct, url: String, file: File)
    : CSProcess<File>(parent, file) {
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