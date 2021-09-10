package com.example.uploadingfiles;

import com.example.uploadingfiles.exceptions.VideoInfoNotFoundException;
import com.example.uploadingfiles.model.VideoInfo;
import com.example.uploadingfiles.services.VideoInfoService;
import com.example.uploadingfiles.util.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class VideoController {

    private VideoInfoService videoInfoService;
    private StorageService storageService;

    @Autowired
    public void FileUploadController(StorageService storageService, VideoInfoService videoInfoService) {
        this.storageService = storageService;
        this.videoInfoService = videoInfoService;
    }

    // http://localhost:8080/addVideoInfo?videoName=hi&videoDesc=desc&category=cat&releaseTime=timeee&releaseDate=2021-03-03


    @GetMapping(value = "/getVideo", produces = "application/json")
    public VideoInfo getVideo(@RequestParam String link) throws VideoInfoNotFoundException {
            return videoInfoService.getVideo(link);
    }

    @PostMapping(value = "/addVideoInfo", produces = "application/json")
    public ResponseEntity<?> uploadVideoInfo(@RequestParam String videoName, @RequestParam String videoDesc,
                                             @RequestParam String category, @RequestParam String releaseTime,
                                             @RequestParam String releaseDate, @RequestParam String link, Principal principal){
        VideoInfo videoInfo = new VideoInfo(videoName, videoDesc, category, releaseTime, releaseDate, link);
        if (videoInfoService.checkVideoInfo(link)){
            return new ResponseEntity<>(videoInfoService.updateVideoInfo(videoInfo),HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("Видео не найдено", HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping(value = "/admin/deleteVideo", produces = "application/json")
    public int deleteVideo(@RequestParam String link){
        return videoInfoService.deleteVideo(link);
    }

    @PostMapping(value = "/moderator/changeVideoName", produces = "application/json")
    public int changeVideoName(@RequestParam String link, @RequestParam String videoName){
        return videoInfoService.changeVideoName(link, videoName);
    }

    @PostMapping(value = "/moderator/changeVideoDesc", produces = "application/json")
    public int changeVideoDesc(@RequestParam String link, @RequestParam String videoDesc){
        return videoInfoService.changeVideoDesc(link, videoDesc);
    }

    @PostMapping(value = "/moderator/changeVideoCategory", produces = "application/json")
    public int changeVideoCategory(@RequestParam String link, @RequestParam String videoCategory){
        return videoInfoService.changeVideoCategory(link, videoCategory);
    }

    @PostMapping(value = "/moderator/changeVideoInfo", produces = "application/json")
    public int changeVideoInfo(@RequestParam String link,@RequestParam String videoName, @RequestParam String videoDesc, @RequestParam String videoCategory ){
        System.out.println(videoName);
        System.out.println(videoDesc);
        System.out.println(videoCategory);
        return videoInfoService.changeVideoInfo(link, videoName, videoDesc, videoCategory);
    }

    @PostMapping(value = "/view/{link}", produces = "application/json")
    public VideoInfo countView(@PathVariable String link) throws VideoInfoNotFoundException {
        return videoInfoService.countView(link);
    }
}


