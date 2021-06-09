package tech.danielwaiguru.dscfirebaseauth.extensions

import android.util.Patterns

fun String.isValidEmail() = Patterns.EMAIL_ADDRESS.matcher(this).matches()