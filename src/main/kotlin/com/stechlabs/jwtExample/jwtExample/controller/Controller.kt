package com.stechlabs.jwtExample.jwtExample.controller

import com.stechlabs.jwtExample.jwtExample.model.Content
import com.stechlabs.jwtExample.jwtExample.model.Request
import com.stechlabs.jwtExample.jwtExample.model.Response
import com.stechlabs.jwtExample.jwtExample.service.MyUserDetailsService
import com.stechlabs.jwtExample.jwtExample.service.UploadService
import com.stechlabs.jwtExample.jwtExample.utils.JWTUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile



@RestController
internal class Controller {
    @Autowired
    private val authenticationManager: AuthenticationManager? = null

    @Autowired
    private val jwtTokenUtil: JWTUtils? = null


    @Autowired
    private lateinit var uploadService: UploadService
    @Autowired
    private val userDetailsService: MyUserDetailsService? = null
    @RequestMapping("/hello",method = [RequestMethod.GET])
    fun home(): String {
        return "Hello World"
    }

    @RequestMapping(value = ["/login"], method = [RequestMethod.POST])
    fun createAuthenticationToken(@RequestBody authenticationRequest: Request): ResponseEntity<*> {
        try {
            authenticationManager!!.authenticate(
                    UsernamePasswordAuthenticationToken(authenticationRequest.username, authenticationRequest.password)
            )
        } catch (e: BadCredentialsException) {
            throw Exception("Incorrect username or password", e)
        }
        val userDetails = userDetailsService?.loadUserByUsername(authenticationRequest.username)
        val jwt: String = jwtTokenUtil!!.generateToken(userDetails!!)
        return ResponseEntity.ok<Any>(Response(jwt))
    }


    @RequestMapping(value = ["/upload"], method = [RequestMethod.POST])
     fun fileUpload(@RequestParam("file") file: MultipartFile): ResponseEntity<Content> {
        return try {
            val content=uploadService.uploadFile(file)
            ResponseEntity.ok(Content(content = content))
        }catch (e:Exception){
            ResponseEntity.ok(Content(content = "Not Available"))
        }
    }
}