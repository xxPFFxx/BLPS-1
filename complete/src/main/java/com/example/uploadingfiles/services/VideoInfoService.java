package com.example.uploadingfiles.services;

import com.example.uploadingfiles.exceptions.VideoInfoNotFoundException;
import com.example.uploadingfiles.model.User;
import com.example.uploadingfiles.model.VideoInfo;
import com.example.uploadingfiles.repositories.UserRepository;
import com.example.uploadingfiles.repositories.VideoInfoRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class VideoInfoService {


    private final VideoInfoRepository videoInfoRepository;
    private final UserRepository userRepository;

    public VideoInfoService(VideoInfoRepository videoInfoRepository, UserRepository userRepository) {
        this.videoInfoRepository = videoInfoRepository;
        this.userRepository = userRepository;
    }

    public VideoInfo saveVideoInfo(VideoInfo videoInfo, String username){
        User user = userRepository.findByUsername(username);
        videoInfo.setUser(user);
        return videoInfoRepository.save(videoInfo);
    }

    public boolean checkVideoInfo(String link){
        return videoInfoRepository.countVideoInfosByLink(link) > 0;
    }

    public int updateVideoInfo(VideoInfo videoInfo){
        return videoInfoRepository.updateVideoInfo(videoInfo);
    }

    public VideoInfo getVideo(String link) throws VideoInfoNotFoundException {
        VideoInfo videoInfo = videoInfoRepository.findVideoInfoByLink(link);
        if (videoInfo == null) throw new VideoInfoNotFoundException(link);
        return videoInfo;
    }

    public int deleteVideo(String link){
        return videoInfoRepository.deleteVideoInfoByLink(link);
    }

    public int changeVideoName(String link, String videoName){
        return videoInfoRepository.renameVideoNameByLink(link, videoName);
    }

    public int changeVideoDesc(String link, String videoDesc){
        return videoInfoRepository.renameVideoDescByLink(link, videoDesc);
    }

    public int changeVideoCategory(String link, String category){
        return videoInfoRepository.renameVideoCategoryByLink(link, category);
    }

    @Transactional
    public int changeVideoInfo(String link, String videoName, String videoDesc, String videoCategory){
        if (!videoName.equals("")) videoInfoRepository.renameVideoNameByLink(link, videoName);
        if (!videoDesc.equals("")) videoInfoRepository.renameVideoDescByLink(link, videoDesc);
        if (!videoCategory.equals("")) videoInfoRepository.renameVideoCategoryByLink(link, videoCategory);
        return 1;
    }

    public VideoInfo countView(String link) throws VideoInfoNotFoundException {
        videoInfoRepository.countView(link);
        VideoInfo videoInfo = getVideo(link);
        return videoInfo;
    }

    public void setPopularStatus(){
        List<VideoInfo> videoInfos =  videoInfoRepository.selectPotentialPopularVideos();
        for (VideoInfo videoInfo : videoInfos){
            videoInfoRepository.updatePopularStatus(videoInfo.getLink());
        }
    }
}
