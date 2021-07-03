package com.stechlabs.jwtExample.jwtExample.service

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File

@Service
class UploadService {

    fun uploadFile(file:MultipartFile):String{

        val content=file.bytes.toString()
        file.transferTo(File("C:\\Users\\Saif\\Desktop\\jwtExample\\uploads\\${file.originalFilename}"))
        return content
    }
}