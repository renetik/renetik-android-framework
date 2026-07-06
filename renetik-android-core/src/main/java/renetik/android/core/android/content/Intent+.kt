package renetik.android.core.android.content

import android.content.Context
import android.content.Intent
import renetik.android.core.lang.CSEnvironment.app
import kotlin.reflect.KClass

val Intent.asString: String
    get() {
        val string = StringBuilder(toString())
        extras?.let {
            string.append(" Extras: ")
            for (key in it.keySet()) {
                val info = it.getString(key)
                string.append("$key $info (${info?.javaClass?.name ?: ""})")
            }
        }
        return string.toString()
    }

fun Intent(context: Context, kClass: KClass<*>) = Intent(context, kClass.java)
fun Intent(context: Context, kClass: KClass<*>, action: String) =
    Intent(context, kClass.java).also { it.action = action }

inline fun <reified T> Intent(context: Context) = Intent(context, T::class.java)
inline fun <reified T> Intent(context: Context, action: String): Intent =
    Intent<T>(context).also { it.action = action }

inline fun <reified T> Intent() = Intent(app, T::class.java)

fun Intent(action: String, type: String) = Intent(action).also { it.type = type }
fun Intent(action: String, category: String, type: String) = Intent(action).also {
    it.addCategory(category)
    it.type = type
}