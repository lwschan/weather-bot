package dev.lewischan.weatherbot.infrastructure

import io.micrometer.context.ContextSnapshotFactory
import kotlinx.coroutines.CoroutineDispatcher
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

class ContextPropagatingDispatcher(
    private val contextSnapshotFactory: ContextSnapshotFactory = ContextSnapshotFactory.builder()
        .build()
) : CoroutineDispatcher() {

    private val executor = Executors.newVirtualThreadPerTaskExecutor()

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        val snapshot = contextSnapshotFactory.captureAll()
        executor.execute {
            snapshot.setThreadLocals().use { block.run() }
        }
    }

    fun shutdown() {
        executor.shutdown()
    }

}
