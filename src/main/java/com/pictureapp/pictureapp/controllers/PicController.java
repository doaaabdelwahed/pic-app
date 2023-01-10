package com.pictureapp.pictureapp.controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.io.Files;
import com.pictureapp.pictureapp.models.Pic;
import com.pictureapp.pictureapp.models.PicCategory;
import com.pictureapp.pictureapp.models.PicStatus;
import com.pictureapp.pictureapp.models.User;
import com.pictureapp.pictureapp.service.PicService;
import com.pictureapp.pictureapp.service.UserService;
import com.pictureapp.pictureapp.utils.ImageMapper;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/pic")
public class PicController {
	private final PicService picService;
	private final ImageMapper imageMapper;
	private final UserService userService;
	@Value("${server.file-server.location}")
	String FILE_SERVER_PATH;

	@Autowired
	public PicController(PicService picService, ImageMapper imageMapper, UserService userService) {
		this.picService = picService;
		this.imageMapper = imageMapper;
		this.userService = userService;
	}

	@GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Pic>> getAllApprpved() {

		List<Pic> list = picService.getAllByStatus(List.of(PicStatus.APPROVED));

		return new ResponseEntity<List<Pic>>(list, HttpStatus.OK);
	}

	@GetMapping(path = "/pending", produces = MediaType.APPLICATION_JSON_VALUE)
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<Pic>> getAllPending() {

		List<Pic> list = picService.getAllByStatus(List.of(PicStatus.PENDING));

		return new ResponseEntity<List<Pic>>(list, HttpStatus.OK);
	}

	@GetMapping(path = "onepic/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Pic> getPic(@PathVariable("id") Long id) {

		Pic pic = picService.getById(id).orElseThrow(() -> new UsernameNotFoundException("picture not found"));

		return new ResponseEntity<Pic>(pic, HttpStatus.OK);
	}

	@PostMapping(value = "/add")
	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	public ResponseEntity<String> addPic(@RequestParam("image") MultipartFile multipartFile,
			@RequestParam("descreption") String descreption, @RequestParam("category") PicCategory category,
			Authentication authentication) throws IOException {
		System.out.println("auth:::" + authentication.getName());
		User user = null;
		String fileName = null;
		String extension = null;
		List<String> imageTypes = Arrays.asList("PNG", "GIF", "JPG");
		if (authentication != null) {
			user = userService.findByUsername(authentication.getName()).orElseThrow(
					() -> new UsernameNotFoundException("User Not Found with username: " + authentication.getName()));
		} else {
			return ResponseEntity.badRequest().body("authentcation error");
		}

		if (multipartFile.getOriginalFilename() != null) {
			fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			extension = Files.getFileExtension(fileName);

		}
		if (extension == null || !imageTypes.contains(extension.toUpperCase())) {
			return ResponseEntity.badRequest().body("file format not allowed");
		}
		imageMapper.saveFile(FILE_SERVER_PATH, fileName, multipartFile);
		Pic pic = new Pic();
		pic.setCategory(category);
		pic.setDescription(descreption);

		pic.setStatus(PicStatus.PENDING);
		pic.setPath(fileName);

		pic.setUser(user);

		picService.save(pic);
		return new ResponseEntity<String>("image saved", HttpStatus.OK);
	}

	@PostMapping("/approve/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> approvePic(@PathVariable("id") Long id) {

		Pic pic = picService.getById(id).orElseThrow(() -> new UsernameNotFoundException("picture not found"));

		pic.setStatus(PicStatus.APPROVED);
		picService.save(pic);

		return new ResponseEntity<String>("image approved", HttpStatus.OK);

	}

	@PostMapping("/decline/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> declinePic(@PathVariable("id") Long id) throws IOException {

		Pic pic = picService.getById(id).orElseThrow(() -> new UsernameNotFoundException("picture not found"));

		pic.setStatus(PicStatus.DECLINED);
		picService.save(pic);
		imageMapper.deleteFile(FILE_SERVER_PATH, pic.getPath());
		return new ResponseEntity<String>("image declined", HttpStatus.OK);

	}
}
