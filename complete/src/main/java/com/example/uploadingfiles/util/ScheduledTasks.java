package com.example.uploadingfiles.util;

import com.example.uploadingfiles.services.VideoInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class ScheduledTasks {
    private final VideoInfoService videoInfoService;
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

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
//        log.info("The time is now {}", dateFormat.format(new Date()));
    }
}
