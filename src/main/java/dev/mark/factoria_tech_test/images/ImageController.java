package dev.mark.factoria_tech_test.images;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import dev.mark.factoria_tech_test.messages.ResponseMessage;

@RestController
@RequestMapping(path = "${api-endpoint}")
public class ImageController {

    @Autowired
    IStorageService storageService;

    @Autowired
    ImageService imageService;

    @GetMapping(path = "/any/images/getCurrentUserImages")
    public ResponseEntity<List<Image>> getImages() throws Exception{
        List<Image> images = imageService.getCurrentUserImages();
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(images);
    }

    @PostMapping(path = "/any/images/uploadImage")
    ResponseEntity<Image> uploadImage(@RequestParam(name = "imageTitle", required = true) String imageTitle,
            @RequestParam(name = "file", required = true) MultipartFile file) {

        String uniqueImageName = storageService.createUniqueName(file);

        storageService.saveImage(file, uniqueImageName);
        Image savedImage = imageService.saveImage(file, imageTitle, uniqueImageName);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedImage);
    }

    @PutMapping(path = "/any/images/updateImage/{id}")
    ResponseEntity<Image> updateImage(@PathVariable Long id, 
        @RequestParam(name = "imageTitle", required = true) String imageTitle,
            @RequestParam(name = "file", required = false) Optional<MultipartFile> file) {

        Image updatedImage = imageService.updateImage(file, id, imageTitle);

        return ResponseEntity.status(HttpStatus.OK).body(updatedImage);
    }

    @GetMapping("/any/images/getAsResource/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);

        if (file == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @DeleteMapping("/any/images/{filename:.+}")
    public ResponseEntity<ResponseMessage> deleteFile(@PathVariable String filename) {
        String message = "";

        try {
            imageService.delete(filename);
            boolean existed = storageService.delete(filename);

            if (existed) {
                message = "Deleted the file: " + filename + "succesfully";
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            }

            message = "The file does not exist!";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not delete the file: " + filename + ". Error: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
        }
    }
}