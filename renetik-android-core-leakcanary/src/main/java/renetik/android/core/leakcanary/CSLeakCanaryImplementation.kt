package renetik.android.core.leakcanary

import leakcanary.AppWatcher.objectWatcher
import leakcanary.LeakCanary.config
import leakcanary.LeakCanary.showLeakDisplayActivityLauncherIcon
import renetik.android.core.kotlin.then
import renetik.android.core.lang.CSEnvironment.isTestRunner
import renetik.android.core.lang.CSLeakCanaryInterface
import renetik.android.core.lang.variable.CSVariable.Companion.variable
import shark.IgnoredReferenceMatcher
import shark.ObjectInspector
import shark.ReferencePattern.InstanceFieldPattern
import shark.ReferencePattern.StaticFieldPattern

private val classPrefixes = listOf("renetik.android.", "com.renetik.")
private val fieldCandidates = listOf("id", "key", "name", "title", "text")
private val refFields = listOf("parent", "preset")

private val objectInspector = ObjectInspector { reporter ->
    val heapObject = reporter.heapObject
    val instance = heapObject.asInstance ?: return@ObjectInspector
    val instanceClassName = instance.instanceClassName
    if (!classPrefixes.any { instanceClassName.startsWith(it) }) return@ObjectInspector
    val found = mutableSetOf<String>()
    runCatching {
        instance.readFields().forEach { field ->
            val name = field.name
            if (name in fieldCandidates) {
                val value = runCatching { field.value.readAsJavaString() }.getOrNull()
                if (!value.isNullOrBlank()) found.add("$name=$value")
            }
        }
    }
    if (found.isEmpty()) {
        fieldCandidates.forEach { fieldName ->
            runCatching {
                val heapField = instance[instanceClassName, fieldName] ?: return@runCatching
                val value = heapField.value.readAsJavaString()
                if (!value.isNullOrBlank()) found.add("$fieldName=$value")
            }
        }
    }
    refFields.forEach { refField ->
        runCatching {
            val ref = instance[instanceClassName, refField]?.value?.asObject?.asInstance
            ref?.readFields()?.forEach { field ->
                val fieldName = field.name
                if (fieldName in fieldCandidates) {
                    val value = runCatching { field.value.readAsJavaString() }.getOrNull()
                    if (!value.isNullOrBlank()) found.add("$refField.$fieldName=$value")
                }
            }
        }
    }
    if (found.isNotEmpty()) found.forEach { reporter.labels.add(it) }
}

object CSLeakCanaryImplementation : CSLeakCanaryInterface {
    override var isEnabled: Boolean by variable(true, ::updateConfiguration)

    override fun Any.expectWeaklyReachable(description: () -> String) {
        if (isEnabled && !isTestRunner) objectWatcher
            .expectWeaklyReachable(this, description())
    }

    override fun enable() = then { isEnabled = true }

    override fun disable() = then { isEnabled = false }

    private val staticFields: List<Pair<String, String>> = listOf(
        "com.mediatek.SbeBoostFrameworkImpl" to "sInstance"
    )

    private val instanceFields: List<Pair<String, String>> = listOf()

    private fun updateConfiguration(isEnabled: Boolean) {
        if (isTestRunner) return
        config = config.copy(dumpHeap = isEnabled,
            referenceMatchers = config.referenceMatchers + staticFields.map {
                IgnoredReferenceMatcher(StaticFieldPattern(it.first, it.second))
            } + instanceFields.map {
                IgnoredReferenceMatcher(InstanceFieldPattern(it.first, it.second))
            },
            objectInspectors = config.objectInspectors + objectInspector
        )
        showLeakDisplayActivityLauncherIcon(isEnabled)
    }

    init {
        updateConfiguration(isEnabled)
    }
}
