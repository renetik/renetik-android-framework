package renetik.android.event.registration

import androidx.annotation.AnyThread
import renetik.android.core.lang.Fun
import renetik.android.event.registration.CSRegistration.Companion.CSRegistration

fun CSRegistration.registerTo(registrations: CSHasRegistrations?): CSRegistration =
    registrations?.register(this) ?: this

@AnyThread
fun CSHasRegistrations.register(registration: CSRegistration): CSRegistration =
    registrations.register(registration)

operator fun CSHasRegistrations.plus(registration: CSRegistration): CSRegistration =
    register(registration)

@JvmName("registerNullable")
fun CSHasRegistrations.register(registration: CSRegistration?): CSRegistration? =
    registration?.also(::register)

@JvmName("plusNullable")
operator fun <T : CSRegistration> CSHasRegistrations.plus(registration: T?): T? =
    registration?.also(::register)

@JvmName("registerReplaceRegistrationNullable")
fun CSHasRegistrations.register(
    replace: CSRegistration?, registration: CSRegistration?
): CSRegistration? = registrations.register(replace, registration)

fun CSHasRegistrations.register(
    replace: CSRegistration?, registration: CSRegistration
): CSRegistration = registrations.register(replace, registration)!!

@JvmName("plusRegistrationReplace")
operator fun CSHasRegistrations.plus(
    registration: Pair<CSRegistration?, CSRegistration>
): CSRegistration = register(registration.first, registration.second)

@JvmName("registerKeyRegistrationNullable")
fun CSHasRegistrations.register(
    key: String, registration: CSRegistration?
): CSRegistration? = registrations.register(key, registration)

fun CSHasRegistrations.register(
    key: String, registration: CSRegistration
): CSRegistration = registrations.register(key, registration)!!

operator fun CSHasRegistrations.plus(
    registration: Pair<String, CSRegistration>
): CSRegistration = register(registration.first, registration.second)

@JvmName("plusPairNullable")
operator fun CSHasRegistrations.plus(
    registration: Pair<String, CSRegistration?>
): CSRegistration? = register(registration.first, registration.second)

fun CSHasRegistrations.onCancel(function: Fun): CSRegistration =
    register(CSRegistration(onCancel = { function() }))