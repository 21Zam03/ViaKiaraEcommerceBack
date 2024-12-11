package com.zam.security.services;

import com.zam.security.payload.response.FileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FireBaseStorageService {

    public List<FileResponse> uploadFile(List<MultipartFile> file) throws Exception;

}
