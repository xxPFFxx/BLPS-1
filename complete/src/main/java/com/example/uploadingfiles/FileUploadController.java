package com.example.uploadingfiles;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import com.example.uploadingfiles.model.User;
import com.example.uploadingfiles.model.VideoInfo;
import com.example.uploadingfiles.security.UserPrincipal;
import com.example.uploadingfiles.services.UserService;
import com.example.uploadingfiles.services.VideoInfoService;
import com.example.uploadingfiles.exceptions.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.example.uploadingfiles.exceptions.StorageFileNotFoundException;
import com.example.uploadingfiles.util.StorageService;

@RestController
public class FileUploadController {

	private final StorageService storageService;
	private final UserService userService;
	private final VideoInfoService videoInfoService;

	@Autowired
	public FileUploadController(StorageService storageService, UserService userService, VideoInfoService videoInfoService) {
		this.storageService = storageService;
		this.userService = userService;
		this.videoInfoService = videoInfoService;
	}
	@Value("errortext") String errorText;

	@GetMapping("/")
	public String listUploadedFiles(Model model) throws IOException {

		model.addAttribute("files", storageService.loadAll().map(
				path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
						"serveFile", path.getFileName().toString()).build().toUri().toString())
				.collect(Collectors.toList()));

		//return "infoAboutVideo";
		return "uploadForm";
	}

	@GetMapping("/infoAboutVideo")
	public String showInfoAboutVideo(Model model) throws IOException {

		model.addAttribute("files", storageService.loadAll().map(
				path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
						"serveFile", path.getFileName().toString()).build().toUri().toString())
				.collect(Collectors.toList()));

		//return "infoAboutVideo";
		return "infoAboutVideo";
	}

	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

		Resource file = storageService.loadAsResource(filename);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}
	@PostMapping("/uploadVideo")
	public ResponseEntity<?> handleFileUpload(Principal principal, @RequestParam("file") MultipartFile file
									  ) throws IOException {
			storageService.store(file);
			String generatedString = generateLink(10);
			//TODO проверка, нет ли такой линки уже

			return new ResponseEntity<>(videoInfoService.saveVideoInfo(null, null, null, null, null, generatedString, principal.getName()),HttpStatus.OK);
		}

	public String generateLink(int length){
		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		Random random = new Random();
		String generatedString = random.ints(leftLimit, rightLimit + 1)
				.limit(length)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
				.toString();
		return generatedString;
	}
	@GetMapping(value = "/checkUser", produces = "application/json")
	public Map<String, String> checkUser(@RequestParam String login, Model model){
			HashMap<String, String> map = new HashMap<>();
			if (userService.checkUser(login)){
				map.put("message", "Пользователь зарегистрирован");
			}
			else{
				map.put("message", "Пользователь не зарегистрирован");
			}
		return map;
		}



	@ExceptionHandler(StorageFileNotFoundException.class)
	public String handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return "uploadForm";
	}

	@ExceptionHandler(StorageException.class)
	public Map<String, String> handleStorageException(StorageException storageException, Model model){
		HashMap<String, String> map = new HashMap<>();
		map.put("error", storageException.getMessage());
		return map;
	}

}
