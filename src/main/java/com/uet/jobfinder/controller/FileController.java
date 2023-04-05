package com.uet.jobfinder.controller;

import com.uet.jobfinder.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "file")
public class FileController {

    private FileService fileService;

    @GetMapping(
            path = "image/{id}",
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody byte[] getImage(@PathVariable Long id) {
        return fileService.getImageFile(id);
    }

    @GetMapping(value = "pdf/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] getPdf(@PathVariable Long id) {
        return fileService.getPdfFile(id);
    }

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }
}
