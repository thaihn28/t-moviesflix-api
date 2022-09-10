package com.example.tmovierestapi.service.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinaryConfig;


    public String uploadThumb(MultipartFile file) {
        try {
            File uploadedFile = convertMultiPartToFile(file);
            Map uploadResult = cloudinaryConfig.uploader().upload(uploadedFile, ObjectUtils.emptyMap());
            deleteFile(uploadedFile.getName());
            String publicID = uploadResult.get("public_id").toString();
            String format = "." + uploadResult.get("format").toString();
            String thumbURL = cloudinaryConfig.url().transformation(
                    new Transformation().width(360).height(480).crop("fill")).generate(publicID + format);
            return thumbURL;
        } catch (Exception e) {
            deleteFile(file.getOriginalFilename());
            throw new RuntimeException(e.getMessage());
        }
    }
    public String uploadPoster(MultipartFile file) {
        try {
            File uploadedFile = convertMultiPartToFile(file);
            Map uploadResult = cloudinaryConfig.uploader().upload(uploadedFile, ObjectUtils.emptyMap());
            deleteFile(uploadedFile.getName());
            String publicID = uploadResult.get("public_id").toString();
            String format = "." + uploadResult.get("format").toString();
            String posterURL = cloudinaryConfig.url().transformation(
                    new Transformation().width(720).height(480).crop("fill")).generate(publicID + format);
            return posterURL;
        } catch (Exception e) {
            deleteFile(file.getOriginalFilename());
            throw new RuntimeException(e.getMessage());
        }
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
    private void deleteFile(String fileName){
        File file = new File(fileName);
        file.delete();
    }
}
