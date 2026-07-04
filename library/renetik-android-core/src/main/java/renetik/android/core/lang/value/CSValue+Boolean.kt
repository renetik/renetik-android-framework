@file:Suppress("NOTHING_TO_INLINE")

package renetik.android.core.lang.value

import renetik.android.core.kotlin.primitives.isTrue
import renetik.android.core.kotlin.then
import renetik.android.core.lang.Fun

//─────────────────────────────────────────
// Properties: isTrue / isFalse
//─────────────────────────────────────────

inline val CSValue<Boolean>.isTrue: Boolean
    get() = value

@get:JvmName("CSValueBooleanOptionalIsTrue")
inline val CSValue<Boolean>?.isTrue: Boolean
    get() = this?.value == true

@get:JvmName("CSValueOptionalBooleanIsTrue")
inline val CSValue<Boolean?>.isTrue: Boolean
    get() = this.value == true

inline val CSValue<Boolean>.isFalse: Boolean
    get() = !value

@get:JvmName("CSValueBooleanOptionalIsFalse")
inline val CSValue<Boolean>?.isFalse: Boolean
    get() = this?.value == false

//─────────────────────────────────────────
// Null-safe isTrue() for CSValue<Boolean?>
//─────────────────────────────────────────

@JvmName("isTrueBooleanNullable")
inline fun CSValue<Boolean?>.isTrue(): Boolean = value == true

//─────────────────────────────────────────
// Branching helpers
//─────────────────────────────────────────

inline fun <R> CSValue<Boolean>.ifTrue(function: () -> R): R? =
    if (isTrue) function() else null

inline fun <R> CSValue<Boolean>.ifFalse(function: () -> R): R? =
    if (isFalse) function() else null

inline fun CSValue<Boolean>.isTrue(function: Fun) =
    then { if (isTrue) function() }

inline fun CSValue<Boolean>.isFalse(function: Fun) =
    then { if (isFalse) function() }

//─────────────────────────────────────────
// Infix OR operators
//─────────────────────────────────────────

// Boolean · CSValue<Boolean>
@JvmName("or_CSValueOfBoolean")
inline infix fun Boolean.or(boolean: CSValue<Boolean>) =
    this || boolean.value

// Boolean · CSValue<Boolean?>
@JvmName("or_CSValueOfNullableBoolean")
inline infix fun Boolean.or(boolean: CSValue<Boolean?>) =
    this || boolean.isTrue

// Boolean · CSValue<Boolean>?
@JvmName("or_NullableCSValueOfBoolean")
inline infix fun Boolean.or(boolean: CSValue<Boolean>?) =
    this || boolean.isTrue

// Boolean? · CSValue<Boolean>
@JvmName("orBoxed_CSValueOfBoolean")
inline infix fun Boolean?.or(boolean: CSValue<Boolean>) =
    isTrue || boolean.value

// Boolean? · CSValue<Boolean?>
@JvmName("orBoxed_CSValueOfNullableBoolean")
inline infix fun Boolean?.or(boolean: CSValue<Boolean?>) =
    isTrue || boolean.isTrue

// Boolean? · CSValue<Boolean>?
@JvmName("orBoxed_NullableCSValueOfBoolean")
inline infix fun Boolean?.or(boolean: CSValue<Boolean>?) =
    isTrue || boolean.isTrue

// CSValue<Boolean> · Boolean
inline infix fun CSValue<Boolean>.or(second: Boolean): Boolean =
    value || second

// CSValue<Boolean> · CSValue<Boolean>
inline infix fun CSValue<Boolean>.or(second: CSValue<Boolean>): Boolean =
    value || second.value

// CSValue<Boolean?> · Boolean
@JvmName("or_CSVNullable_left_boolean")
inline infix fun CSValue<Boolean?>.or(second: Boolean): Boolean =
    this.isTrue || second

// CSValue<Boolean?> · CSValue<Boolean>
@JvmName("or_CSVNullable_left_CSVBoolean")
inline infix fun CSValue<Boolean?>.or(second: CSValue<Boolean>): Boolean =
    this.isTrue || second.value

// CSValue<Boolean?> · CSValue<Boolean?>
@JvmName("or_CSVNullable_left_CSVNullableBoolean")
inline infix fun CSValue<Boolean?>.or(second: CSValue<Boolean?>): Boolean =
    this.isTrue || second.isTrue

// CSValue<Boolean>? · Boolean
@JvmName("or_nullableCSV_left_boolean")
inline infix fun CSValue<Boolean>?.or(second: Boolean): Boolean =
    this.isTrue || second

// CSValue<Boolean>? · CSValue<Boolean>
@JvmName("or_nullableCSV_left_CSVBoolean")
inline infix fun CSValue<Boolean>?.or(second: CSValue<Boolean>): Boolean =
    this.isTrue || second.value

// CSValue<Boolean>? · CSValue<Boolean?>
@JvmName("or_nullableCSV_left_CSVNullableBoolean")
inline infix fun CSValue<Boolean>?.or(second: CSValue<Boolean?>): Boolean =
    this.isTrue || second.isTrue

// CSValue<Boolean>? · CSValue<Boolean>?
@JvmName("or_nullableCSV_left_nullableCSV")
inline infix fun CSValue<Boolean>?.or(second: CSValue<Boolean>?): Boolean =
    this.isTrue || second.isTrue

// CSValue<Boolean> · Boolean?
@JvmName("or_CSValueOfBoolean_nullableBoolean")
inline infix fun CSValue<Boolean>.or(second: Boolean?): Boolean =
    value || (second == true)

// CSValue<Boolean> · CSValue<Boolean?>
@JvmName("or_CSValueOfBoolean_CSVNullableBoolean")
inline infix fun CSValue<Boolean>.or(second: CSValue<Boolean?>): Boolean =
    value || second.isTrue

// CSValue<Boolean> · CSValue<Boolean>?
@JvmName("or_CSValueOfBoolean_nullableCSVBoolean")
inline infix fun CSValue<Boolean>.or(second: CSValue<Boolean>?): Boolean =
    value || second.isTrue

// CSValue<Boolean?> · Boolean?
@JvmName("or_CSVNullableBoolean_nullableBoolean")
inline infix fun CSValue<Boolean?>.or(second: Boolean?): Boolean =
    isTrue || (second == true)

// CSValue<Boolean?> · CSValue<Boolean>?
@JvmName("or_CSVNullableBoolean_nullableCSVBoolean")
inline infix fun CSValue<Boolean?>.or(second: CSValue<Boolean>?): Boolean =
    isTrue || second.isTrue

// CSValue<Boolean>? · Boolean?
@JvmName("or_nullableCSVBoolean_nullableBoolean")
inline infix fun CSValue<Boolean>?.or(second: Boolean?): Boolean =
    isTrue || (second == true)


//─────────────────────────────────────────
// Infix AND operators
//─────────────────────────────────────────

// Boolean · CSValue<Boolean>
@JvmName("and_CSValueOfBoolean")
inline infix fun Boolean.and(boolean: CSValue<Boolean>) =
    this && boolean.value


// Boolean · CSValue<Boolean?>
@JvmName("and_CSValueOfNullableBoolean")
inline infix fun Boolean.and(boolean: CSValue<Boolean?>) =
    this && boolean.isTrue

// Boolean · CSValue<Boolean>?
@JvmName("and_NullableCSValueOfBoolean")
inline infix fun Boolean.and(boolean: CSValue<Boolean>?) =
    this && boolean.isTrue

// Boolean? · CSValue<Boolean>
@JvmName("andBoxed_CSValueOfBoolean")
inline infix fun Boolean?.and(boolean: CSValue<Boolean>) =
    isTrue && boolean.value

// Boolean? · CSValue<Boolean?>
@JvmName("andBoxed_CSValueOfNullableBoolean")
inline infix fun Boolean?.and(boolean: CSValue<Boolean?>) =
    isTrue && boolean.isTrue

// Boolean? · CSValue<Boolean>?
@JvmName("andBoxed_NullableCSValueOfBoolean")
inline infix fun Boolean?.and(boolean: CSValue<Boolean>?) =
    isTrue && boolean.isTrue

// CSValue<Boolean> · Boolean
inline infix fun CSValue<Boolean>.and(second: Boolean): Boolean =
    value && second

// CSValue<Boolean?> · Boolean
@JvmName("and_CSVNullable_left_boolean")
inline infix fun CSValue<Boolean?>.and(second: Boolean): Boolean =
    this.isTrue && second

// CSValue<Boolean?> · CSValue<Boolean>
@JvmName("and_CSVNullable_left_CSVBoolean")
inline infix fun CSValue<Boolean?>.and(second: CSValue<Boolean>): Boolean =
    this.isTrue && second.value

// CSValue<Boolean?> · CSValue<Boolean?>
@JvmName("and_CSVNullable_left_CSVNullableBoolean")
inline infix fun CSValue<Boolean?>.and(second: CSValue<Boolean?>): Boolean =
    this.isTrue && second.isTrue

// CSValue<Boolean>? · Boolean
@JvmName("and_nullableCSV_left_boolean")
inline infix fun CSValue<Boolean>?.and(second: Boolean): Boolean =
    this.isTrue && second

// CSValue<Boolean>? · CSValue<Boolean>
@JvmName("and_nullableCSV_left_CSVBoolean")
inline infix fun CSValue<Boolean>?.and(second: CSValue<Boolean>): Boolean =
    this.isTrue && second.value

// CSValue<Boolean>? · CSValue<Boolean?>
@JvmName("and_nullableCSV_left_CSVNullableBoolean")
inline infix fun CSValue<Boolean>?.and(second: CSValue<Boolean?>): Boolean =
    this.isTrue && second.isTrue

// CSValue<Boolean>? · CSValue<Boolean>?
@JvmName("and_nullableCSV_left_nullableCSV")
inline infix fun CSValue<Boolean>?.and(second: CSValue<Boolean>?): Boolean =
    this.isTrue && second.isTrue

// CSValue<Boolean> · Boolean?
@JvmName("and_CSValueOfBoolean_nullableBoolean")
inline infix fun CSValue<Boolean>.and(second: Boolean?): Boolean =
    value && (second == true)

// CSValue<Boolean> · CSValue<Boolean>
@JvmName("and_CSValueOfBoolean_CSValueOfBoolean")
inline infix fun CSValue<Boolean>.and(second: CSValue<Boolean>): Boolean =
    value && second.value

// CSValue<Boolean> · CSValue<Boolean?>
@JvmName("and_CSValueOfBoolean_CSVNullableBoolean")
inline infix fun CSValue<Boolean>.and(second: CSValue<Boolean?>): Boolean =
    value && second.isTrue

// CSValue<Boolean> · CSValue<Boolean>?
@JvmName("and_CSValueOfBoolean_nullableCSVBoolean")
inline infix fun CSValue<Boolean>.and(second: CSValue<Boolean>?): Boolean =
    value && second.isTrue

// CSValue<Boolean?> · Boolean?
@JvmName("and_CSVNullableBoolean_nullableBoolean")
inline infix fun CSValue<Boolean?>.and(second: Boolean?): Boolean =
    isTrue && (second == true)

// CSValue<Boolean?> · CSValue<Boolean>?
@JvmName("and_CSVNullableBoolean_nullableCSVBoolean")
inline infix fun CSValue<Boolean?>.and(second: CSValue<Boolean>?): Boolean =
    isTrue && second.isTrue

// CSValue<Boolean>? · Boolean?
@JvmName("and_nullableCSVBoolean_nullableBoolean")
inline infix fun CSValue<Boolean>?.and(second: Boolean?): Boolean =
    isTrue && (second == true)
