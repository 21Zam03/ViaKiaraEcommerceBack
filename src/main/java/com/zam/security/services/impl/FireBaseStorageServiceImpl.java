package com.zam.security.services.impl;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.firebase.cloud.StorageClient;
import com.zam.security.payload.response.FileResponse;
import com.zam.security.services.FireBaseStorageService;
import com.zam.security.utils.FileUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class FireBaseStorageServiceImpl implements FireBaseStorageService {

    @Override
    public List<FileResponse> uploadFile(List<MultipartFile> fileList) throws Exception {
        List<FileResponse> fileResponseList = new ArrayList<>();
        for (MultipartFile multipartFile : fileList) {
            InputStream inputStream = multipartFile.getInputStream();
            String fileName = FileUtil.getFileName(multipartFile);
            String filePath = "Products/" + fileName;
            BlobInfo blobInfo = BlobInfo.newBuilder(StorageClient.getInstance().bucket().getName(), filePath)
                    .setContentType(multipartFile.getContentType())
                    .build();
            Blob blob = StorageClient.getInstance().bucket().create(blobInfo.getName(), inputStream, blobInfo.getContentType());
            String encodedFilePath = URLEncoder.encode(filePath, StandardCharsets.UTF_8);
            String url = "https://firebasestorage.googleapis.com/v0/b/" + StorageClient.getInstance().bucket().getName() + "/o/" +
                    encodedFilePath + "?alt=media";
            FileResponse fileResponse = FileResponse.builder()
                    .mediaLink(blob.getMediaLink())
                    .url(url)
                    .size(blob.getSize())
                    .build();
            fileResponseList.add(fileResponse);
        }
        return fileResponseList;
    }

}
