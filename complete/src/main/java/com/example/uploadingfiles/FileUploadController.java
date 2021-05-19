package com.example.uploadingfiles;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import com.example.uploadingfiles.services.UserService;
import com.example.uploadingfiles.services.VideoInfoService;
import com.example.uploadingfiles.exceptions.StorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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
	public Map<String, String> handleFileUpload(@RequestParam String login, @RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes) throws IOException {
		HashMap<String, String> map = new HashMap<>();
		if (userService.checkUser(login)){
			System.out.println("Залогинен");
//			String uploadPath = "C:\\Users\\Daniil\\IdeaProjects\\BLPS-1\\upload-dir";
//			file.transferTo(new File(uploadPath + "/" + file.getOriginalFilename()));
			storageService.store(file);
			int leftLimit = 97; // letter 'a'
			int rightLimit = 122; // letter 'z'
			int targetStringLength = 10;
			Random random = new Random();

			String generatedString = random.ints(leftLimit, rightLimit + 1)
					.limit(targetStringLength)
					.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
					.toString();
			//TODO проверка, нет ли такой линки уже
			videoInfoService.saveVideoInfo(null, null, null, null, null, generatedString);


			map.put("message", "Все пучком");
			map.put("link", generatedString);
			return map;
			//return "infoAboutVideo";
		}
		else {
				map.put("message", "Пользователь не зарегистрирован");
			}
		return map;
//			System.out.println("Не залогинен");
//			redirectAttributes.addFlashAttribute("message",
//					"Пользователя с ником " + login + " не существует. Проверьте правильность ввода или зарегистрируйтесь.");
			//return "redirect:/";
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
