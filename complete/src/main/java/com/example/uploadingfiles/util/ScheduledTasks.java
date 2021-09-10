package com.example.uploadingfiles.util;

import com.example.uploadingfiles.services.VideoInfoService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class ScheduledTasks {
    private final VideoInfoService videoInfoService;

    public ScheduledTasks(VideoInfoService videoInfoService) {
        this.videoInfoService = videoInfoService;
    }

    /*
    Каждую минуту (cron = "0 * * * * *") проверяет, не стало ли какое-то видео популярным (набрало хотя бы 10 просмотров),
    и обновляет статус для видео, выполнивших это условие
     */
    @Scheduled(cron = "0 * * * * *")
    public void updateStatusOnVideos() {
        videoInfoService.setPopularStatus();
    }
}
