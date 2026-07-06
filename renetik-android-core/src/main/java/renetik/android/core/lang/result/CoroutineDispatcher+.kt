package renetik.android.core.lang.result

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.currentCoroutineContext

//object EmptyDispatcher : CoroutineDispatcher() {
//    override fun isDispatchNeeded(context: CoroutineContext): Boolean = false
//    override fun dispatch(context: CoroutineContext, block: Runnable) = block.run()
//}
//

@OptIn(ExperimentalStdlibApi::class)
suspend fun currentDispatcher(): CoroutineDispatcher? =
    currentCoroutineContext()[CoroutineDispatcher]