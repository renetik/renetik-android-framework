package renetik.android.client.request

open class CSHttpProcess<Data : Any>(url: String, data: Data) : CSProcessBase<Data>(data) {

    var url: String? = url

    override fun toString(): String {
        return super.toString() + url
    }
}