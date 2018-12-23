package renetik.android.client.okhttp3

import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.DownloadListener
import renetik.android.client.okhttp3.extensions.infoString
import renetik.android.client.request.CSResponse
import java.io.File

class DownloadRequest(url: String, file: File) : CSResponse<File>() {
    init {
        AndroidNetworking.download(url, file.parent, file.name).build().startDownload(object : DownloadListener {
            override fun onDownloadComplete() {
                success(file)
            }

            override fun onError(error: ANError) {
                failed(error.infoString())
            }
        })
    }
}