package com.uni.uni_erp.controller.ocr;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/file")
public class FileUploadController {

    @GetMapping("/test")
    public String fileUploadTestPage(){
        return "/model/image_upload";
    }



}
