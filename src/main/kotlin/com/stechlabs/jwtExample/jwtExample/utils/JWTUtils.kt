package com.stechlabs.jwtExample.jwtExample.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*
import java.util.function.Function

@Service
class JWTUtils {

    private val SECRET_KEY="qwertyuiop123456789"


    fun getUserName(token:String):String{
        return extractClaims(token){obj:Claims?->obj!!.subject}
    }

    fun getExpirationDate(token: String?): Date? {
        return extractClaims(token) { obj: Claims? -> obj!!.expiration }
    }

    fun <T> extractClaims(token: String?, resolver: Function<Claims?, T>): T {
        val claims: Claims = extractAllClaims(token)
        return resolver.apply(claims)
    }

    fun extractAllClaims(token:String?):Claims{
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).body
    }

    fun isTokenExpired(token:String):Boolean{
        return getExpirationDate(token)!!.before(Date())
    }

    fun generateToken(userDetails:UserDetails):String{
        val claims=HashMap<String,Any>()
        return createToken(claims,userDetails.username)
    }

    fun createToken(claims:Map<String,Any>,subject:String):String{
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(Date(System.currentTimeMillis()))
                .setExpiration(Date(System.currentTimeMillis()+1000*60*60*10))
                .signWith(SignatureAlgorithm.HS256,SECRET_KEY).compact()
    }

    fun validateToken(token:String,userDetails:UserDetails):Boolean{
        val user=getUserName(token)
        return !isTokenExpired(token) && (user==userDetails.username)
    }

}