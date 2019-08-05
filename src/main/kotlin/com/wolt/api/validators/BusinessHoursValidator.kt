package com.wolt.api.validators

/**
 * Validator interface used in validations
 */
interface BusinessHoursValidator<T> {
    fun validate(t : T)
}
