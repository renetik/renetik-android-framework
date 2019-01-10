package renetik.android.client.request

open class CSMultiResponse<Data : Any>(data: Data? = null) : CSResponse<Data>(data) {

    protected var addedResponse: CSResponse<*>? = null

    fun addLast(response: CSResponse<Data>): CSResponse<Data> {
        response.onSuccess { success(it.data()) }
        return add(response)
    }

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
