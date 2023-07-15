package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ImageController {

    @Autowired
    private ImageService imageService;

    @GetMapping(value = "/files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable("id") Long id){
        Image image = this.imageService.getImage(id);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(image.getType()))
                .body(image.getData());
    }
}
