package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;

@Service
@Transactional
public class ImageService {
    @Autowired
    private ImageRepository imageRepository;

    @Transactional
    public Image save(MultipartFile file) throws IOException{
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Image image = new Image(fileName, file.getContentType(), file.getBytes());

        return this.imageRepository.save(image);
    }

    @Transactional
    public Image getImage(Long id){
        return this.imageRepository.findById(id).get();
    }
    @Transactional
    public Iterable<Image> getAllImages(){
        return this.imageRepository.findAll();
    }
    @Transactional
    public void delete(Image picture) {
        this.imageRepository.delete(picture);
    }
}
