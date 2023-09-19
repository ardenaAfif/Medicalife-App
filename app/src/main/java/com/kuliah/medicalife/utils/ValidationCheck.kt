package com.kuliah.medicalife.utils

import android.util.Patterns

fun validateEmail(email: String): RegisterValidation{
    if (email.isEmpty())
        return RegisterValidation.Failed("Email tidak boleh kosong")
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return RegisterValidation.Failed("Format email salah")

    return RegisterValidation.Success
}

fun validatePassword(password: String): RegisterValidation {
    if (password.isEmpty())
        return RegisterValidation.Failed("Password tidak boleh kosong")

    if (password.length < 6)
        return RegisterValidation.Failed("Password minimal 6 karakter")

    return RegisterValidation.Success
}

fun validateName(name: String): RegisterValidation{
    if (name.isEmpty())
        return RegisterValidation.Failed("Nama tidak boleh kosong")

    return RegisterValidation.Success
}

fun validateEmpty(name: String, password: String, email: String): RegisterValidation{
    if (name.isEmpty() && email.isEmpty() && password.isEmpty())
        return RegisterValidation.Failed("Harap diisi dengan lengkap!")

    return RegisterValidation.Success
}