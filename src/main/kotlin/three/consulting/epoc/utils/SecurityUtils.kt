package three.consulting.epoc.utils

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import three.consulting.epoc.common.Role

fun isAuthorizedToGetTimesheetEntries(email: String?): Boolean {
    val authentication: Authentication = SecurityContextHolder.getContext().authentication
    val jwt = authentication.credentials as Jwt
    val role: String = jwt.getClaim("role")
    val tokenEmail: String = jwt.getClaim("email")
    return if (role === Role.ADMIN.name) {
        true
    } else {
        (email != null && email == tokenEmail)
    }
}
