package renetik.android.core.lang

import renetik.android.core.lang.variable.CSVariable

interface CSValidator {
    fun validate(): Boolean?
}

fun CSValidator(function: () -> Boolean?): CSValidator = CSValidatorImplementation(function)

class CSValidatorImplementation(val function: () -> Boolean?) : CSValidator {
    override fun validate() = function()
}

class CSPropertyValidator<T>(val property: CSVariable<T>,
                             val validate: (T) -> Boolean) : CSValidator {
    override fun validate() = validate(property.value)
}