package com.example.uploadingfiles;

import com.example.uploadingfiles.exceptions.VideoInfoNotFoundException;
import com.example.uploadingfiles.model.Comment;
import com.example.uploadingfiles.services.CommentService;
import com.example.uploadingfiles.services.UserService;
import com.example.uploadingfiles.services.VideoInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class CommentController {
    private VideoInfoService videoInfoService;
    private CommentService commentService;
    private UserService userService;

    @Autowired
    public CommentController(VideoInfoService videoInfoService, CommentService commentService, UserService userService) {
        this.videoInfoService = videoInfoService;
        this.commentService = commentService;
        this.userService = userService;
    }

    /*
    Метод getVideo() вызывается для проверки корректности ссылки, если она неправильная, то будет брошено
    VideoInfoNotFoundException, в ином случае будет выполняться как надо
     */
    @PostMapping(value = "/addComment", produces = "application/json")
    public Comment addComment(@RequestParam String text, @RequestParam String link, Principal principal) throws VideoInfoNotFoundException {
        videoInfoService.getVideo(link);
         return commentService.addComment(text, link, principal.getName());
        }


}

