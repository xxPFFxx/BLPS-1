package com.example.uploadingfiles.repositories;


import com.example.uploadingfiles.model.VideoInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface VideoInfoRepository extends JpaRepository<VideoInfo, Long> {

    long countVideoInfosByLink(String link);

    @Transactional
    @Modifying
    @Query("update VideoInfo v set v.name=:#{#videoinfo.name}, v.desc=:#{#videoinfo.desc}, " +
            "v.category=:#{#videoinfo.category}, v.releasetime=:#{#videoinfo.releasetime}," +
            " v.releasedate=:#{#videoinfo.releasedate} where v.link= :#{#videoinfo.link}")
    int updateVideoInfo(@Param("videoinfo") VideoInfo videoInfo);

    VideoInfo findVideoInfoByLink(String link);

    @Transactional
    int deleteVideoInfoByLink(String link);

    @Transactional
    @Modifying
    @Query("update VideoInfo v set v.name=:videoName where v.link= :link")
    int renameVideoNameByLink(String link, String videoName);

    @Transactional
    @Modifying
    @Query("update VideoInfo v set v.desc=:videoDesc where v.link= :link")
    int renameVideoDescByLink(String link, String videoDesc);

    @Transactional
    @Modifying
    @Query("update VideoInfo v set v.category=:videoCategory where v.link= :link")
    int renameVideoCategoryByLink(String link, String videoCategory);

    @Transactional
    @Modifying
    @Query("update VideoInfo v set v.views=v.views+1 where v.link= :link")
    int countView(String link);

    @Query("select v from VideoInfo v where v.views>=10 and v.popular=false ")
    List<VideoInfo> selectPotentialPopularVideos();

    @Transactional
    @Modifying
    @Query("update VideoInfo v set v.name = concat(v.name, ' [POPULAR]'), v.popular=true where v.link=:link")
    int updatePopularStatus(String link);
}
