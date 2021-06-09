package tech.danielwaiguru.dscfirebaseauth.data.mappers

import tech.danielwaiguru.dscfirebaseauth.data.model.User
import tech.danielwaiguru.dscfirebaseauth.domain.model.UserDomain

fun User.toDomain(): UserDomain = UserDomain(this.firstName, this.lastName, this.email)