package renetik.android.client.request

class CSMultiResponse<Data : Any>(data: Data) : CSResponse<Data>(data) {

    private var addedResponse: CSResponse<*>? = null

    fun <V : Any> add(response: CSResponse<V>, isLast: Boolean): CSResponse<V> {
        if (isLast) response.onSuccess { success() }
        return add(response)
    }

    fun <V : Any> add(response: CSResponse<V>): CSResponse<V> {
        addedResponse = response
        response.onFailed { failed(it) }
        return response
    }

    override fun cancel() {
        super.cancel()
        addedResponse?.cancel()
    }

}
