package com.stechlabs.jwtExample.jwtExample.service

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File

@Service
class UploadService {

    fun uploadFile(file:MultipartFile){
        file.transferTo(File("C:\\Users\\Saif\\Desktop\\jwtExample\\uploads\\${file.originalFilename}"))
    }
}