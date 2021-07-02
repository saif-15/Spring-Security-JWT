package com.stechlabs.jwtExample.jwtExample.service

import kotlin.collections.*
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.*


@Service
class MyUserDetailsService : UserDetailsService{
    override fun loadUserByUsername(userName: String?): UserDetails {
      return  checkUsername(userName)
    }


    // Since storing the users in database is not the part of the Example
    // Hardcoding The Users
    private fun checkUsername(name:String?):User{
        val listOfUsers= HashMap<String,String>()
        listOfUsers.put("saif","12345")
        listOfUsers.put("ali","67890")
        listOfUsers.put("foobar","admin")
        if(listOfUsers.containsKey(name!!)){
            return User(name,listOfUsers.get(name),ArrayList())
        }
        return User(null,null,ArrayList())
    }
}