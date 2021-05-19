package com.example.uploadingfiles.services;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

//import it.sauronsoftware.jave.InputFormatException;
//import it.sauronsoftware.jave.MultimediaInfo;
import com.example.uploadingfiles.exceptions.StorageException;
import com.example.uploadingfiles.exceptions.StorageFileNotFoundException;
import com.example.uploadingfiles.util.StorageProperties;
import com.example.uploadingfiles.util.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
//import it.sauronsoftware.jave.Encoder;
//import it.sauronsoftware.jave.EncoderException;

@Service
public class FileSystemStorageService implements StorageService {

	private final Path rootLocation;

	@Autowired
	public FileSystemStorageService(StorageProperties properties) {
		this.rootLocation = Paths.get(properties.getLocation());
	}

	private File convertMultiPartToFile(MultipartFile file ) throws IOException
	{
		File convFile = new File( file.getOriginalFilename() );
		FileOutputStream fos = new FileOutputStream( convFile );
		fos.write( file.getBytes() );
		fos.close();
		return convFile;
	}

	@Override
	public void store(MultipartFile file) {
		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file.");
			}


			Path destinationFile = this.rootLocation.resolve(
					Paths.get(file.getOriginalFilename()))
					.normalize().toAbsolutePath();

			String mimeType = URLConnection.guessContentTypeFromName(String.valueOf(destinationFile));
			if(mimeType != null && !mimeType.startsWith("video")){
				throw new StorageException(
						"THIS IS NOT A VIDEO, PLZ CALL A DOCTOR");
			}


			if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
				// This is a security check
				throw new StorageException(
						"Cannot store file outside current directory.");
			}
			try (InputStream inputStream = file.getInputStream()) {
				try (
						BufferedInputStream inputStream2 = new BufferedInputStream(inputStream);
						BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(String.valueOf(destinationFile)));
				) {

					byte[] buffer = new byte[8192];

					while (inputStream2.read(buffer) != -1) {
						outputStream.write(buffer);

					}

				} catch (IOException ex) {
					ex.printStackTrace();
				}
				//Files.copy(inputStream, destinationFile,
				//	StandardCopyOption.REPLACE_EXISTING);
			}
		}
		catch (IOException e) {
			throw new StorageException("Failed to store file.", e);
		}
//		catch (InputFormatException e) {
//			e.printStackTrace();
//		} catch (EncoderException e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.rootLocation, 1)
				.filter(path -> !path.equals(this.rootLocation))
				.map(this.rootLocation::relativize);
		}
		catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}

	}

	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException(
						"Could not read file: " + filename);

			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	@Override
	public void init() {
		try {
			Files.createDirectories(rootLocation);
		}
		catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}
}
