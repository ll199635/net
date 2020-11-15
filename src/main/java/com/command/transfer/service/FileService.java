package com.command.transfer.service;

import com.command.transfer.common.Response;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


public interface FileService {
    Response getFile();

    Response upload(MultipartFile files) throws IOException, NoSuchMethodException;

    void download(String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException;
}
