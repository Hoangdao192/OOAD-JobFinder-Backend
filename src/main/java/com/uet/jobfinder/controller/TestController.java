package com.uet.jobfinder.controller;

import com.uet.jobfinder.model.TestModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@RequestMapping(path = "test")
public class TestController {

    @PostMapping
    public ResponseEntity<String> test(@ModelAttribute TestModel testModel) throws IOException {
        System.out.println(testModel.getFile().getOriginalFilename());
        FileCopyUtils.copy(testModel.getFile().getBytes(), new File("C:\\Users\\Administrator\\.jobfinder-server\\" +
                testModel.getFile().getOriginalFilename()));
        return ResponseEntity.ok("");
    }

    @GetMapping(
            produces = MediaType.IMAGE_JPEG_VALUE
    )
    public @ResponseBody byte[] getFile() throws IOException {
        File file = new File("C:\\Users\\Administrator\\.jobfinder-server\\2.png");
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bytes = fileInputStream.readAllBytes();
        fileInputStream.close();
        return bytes;
    }

}
