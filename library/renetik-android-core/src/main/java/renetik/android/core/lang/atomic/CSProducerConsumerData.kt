package renetik.android.core.lang.atomic

import java.util.concurrent.atomic.AtomicReference

/**
 * A thread-safe data structure for producer-consumer scenarios.
 *
 * It maintains two versions of data: an 'active' one for consumers and a
 * 'prepared' one for the producer. The producer can prepare new data in the
 * background. The `swap()` method then atomically makes the prepared data
 * active for consumers.
 *
 * @param T The type of data being managed.
 * @property initial A function providing the initial value for the data.
 */
class CSProducerConsumerData<T : Any>(private val initial: () -> T) {
    private val consumerData = AtomicReference(initial())
    private var producerData: T = initial()
    val active: T get() = consumerData.get()
    val prepared: T get() = producerData

    fun swap() {
        val oldActive = consumerData.getAndSet(producerData)
        producerData = oldActive
    }

    fun clear() {
        consumerData.set(initial())
        producerData = initial()
    }

    fun prepare(value: T) {
        producerData = value
    }
}