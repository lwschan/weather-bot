package dev.lewischan.weatherbot.infrastructure

import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.AssertionMode
import io.kotest.matchers.shouldBe
import io.micrometer.context.ContextRegistry
import io.micrometer.context.ContextSnapshotFactory
import io.micrometer.context.ThreadLocalAccessor
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class ContextPropagatingDispatcherTest : FunSpec({

    val testThreadLocal = ThreadLocal<String?>()
    val testKey = "test.context.value"

    val testRegistry = ContextRegistry().registerThreadLocalAccessor(
        object : ThreadLocalAccessor<String> {
            override fun key(): Any = testKey
            override fun getValue(): String? = testThreadLocal.get()
            override fun setValue(value: String) { testThreadLocal.set(value) }
            override fun setValue() { testThreadLocal.remove() }
        }
    )

    val dispatcher = ContextPropagatingDispatcher(
        ContextSnapshotFactory.builder().contextRegistry(testRegistry).build()
    )

    afterEach {
        testThreadLocal.remove()
    }

    test("should propagate context value from dispatch thread to handler thread") {
        testThreadLocal.set("test-value")

        var capturedValue: String? = null

        coroutineScope {
            launch(dispatcher) {
                capturedValue = testThreadLocal.get()
            }
        }

        capturedValue shouldBe "test-value"
    }

    test("should not propagate context value when none is set on dispatch thread") {
        testThreadLocal.remove()

        var capturedValue: String? = "should-be-null"

        coroutineScope {
            launch(dispatcher) {
                capturedValue = testThreadLocal.get()
            }
        }

        capturedValue shouldBe null
    }

    test("should not leak context value set inside handler to subsequent dispatches") {
        testThreadLocal.remove()

        coroutineScope {
            launch(dispatcher) {
                testThreadLocal.set("handler-only-value")
            }
        }

        var capturedValueAfter: String? = "should-be-null"

        coroutineScope {
            launch(dispatcher) {
                capturedValueAfter = testThreadLocal.get()
            }
        }

        capturedValueAfter shouldBe null
    }

}) {
    override fun assertionMode(): AssertionMode {
        return AssertionMode.None
    }
}
