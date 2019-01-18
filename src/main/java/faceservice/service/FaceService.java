package faceservice.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
@Service
public class FaceService {
    public ResponseEntity<String> deleteFace(String id) {
        return new ResponseEntity("face_token was not found", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> queryFace(String id) {
        return new ResponseEntity("face_token was not found",HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> updateFace(String id, MultipartFile file) {
        return new ResponseEntity("face_token was not found",HttpStatus.BAD_REQUEST);
    }

    public List<String> queryGroup(String group) {
        List list=new ArrayList();
        return list ;
    }

    public int deleteGroup(String group) {
        return 1;
    }
}
