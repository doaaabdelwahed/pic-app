package com.pictureapp.pictureapp.utils;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * user.home :User home directory user.dir :User's current working directory
 **/
@Component
public class ImageMapper {

	@Value("${server.file-server.location}")
	String FILE_SERVER_PATH;

	public void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {

		File file = new File(System.getProperty("user.dir") + FILE_SERVER_PATH + fileName);

		if (!file.exists()) {
			multipartFile.transferTo(file);
		} else {
			throw new IOException("File already exists with the name: " + fileName);
		}
	}

	public void deleteFile(String uploadDir, String fileName) throws IOException {

		File file = new File(System.getProperty("user.dir") + FILE_SERVER_PATH + fileName);

		if (file.exists()) {
			file.delete();
		} else {
			throw new IOException("File not exist with the name: " + fileName);
		}
	}
}
