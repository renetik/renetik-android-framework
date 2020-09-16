package renetik.android.java.common

interface CSValidator {
    fun validate(): Boolean?
}

fun CSValidator(function: () -> Boolean?): CSValidator = CSValidatorImplementation(function)

class CSValidatorImplementation(val function: () -> Boolean?) : CSValidator {
    override fun validate() = function()
}

class CSPropertyValidator<T>(val property: CSProperty<T>,
                             val validate: (T) -> Boolean) : CSValidator {
    override fun validate() = validate(property.value)
}