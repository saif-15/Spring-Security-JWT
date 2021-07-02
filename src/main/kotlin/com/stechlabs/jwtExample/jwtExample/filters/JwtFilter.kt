package com.stechlabs.jwtExample.jwtExample.filters

import com.stechlabs.jwtExample.jwtExample.service.MyUserDetailsService
import com.stechlabs.jwtExample.jwtExample.utils.JWTUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class JwtRequestFilter : OncePerRequestFilter() {
    @Autowired
    private val userDetailsService: MyUserDetailsService? = null

    @Autowired
    private val jwtUtil: JWTUtils? = null

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val authorizationHeader = request.getHeader("Authorization")
        var username: String? = null
        var jwt: String? = null

        if (authorizationHeader != null && authorizationHeader.subSequence(0,7)=="Bearer ") {
            jwt = authorizationHeader.subSequence(7,authorizationHeader.length) as String
            username = jwtUtil?.getUserName(jwt)
        }
        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            val userDetails = userDetailsService!!.loadUserByUsername(username)
            if (jwtUtil!!.validateToken(jwt!!, userDetails)) {
                val usernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.authorities)
                usernamePasswordAuthenticationToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = usernamePasswordAuthenticationToken
            }
        }
        chain.doFilter(request, response)
    }
}