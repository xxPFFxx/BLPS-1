package com.example.uploadingfiles;

import com.example.uploadingfiles.exceptions.VideoInfoNotFoundException;
import com.example.uploadingfiles.model.Comment;
import com.example.uploadingfiles.model.Reaction;
import com.example.uploadingfiles.model.ReactionType;
import com.example.uploadingfiles.model.VideoInfo;
import com.example.uploadingfiles.services.CommentService;
import com.example.uploadingfiles.services.ReactionService;
import com.example.uploadingfiles.services.UserService;
import com.example.uploadingfiles.services.VideoInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class ReactionController {
    private VideoInfoService videoInfoService;
    private CommentService commentService;
    private UserService userService;
    private ReactionService reactionService;

    @Autowired
    public ReactionController(VideoInfoService videoInfoService, CommentService commentService, UserService userService, ReactionService reactionService) {
        this.videoInfoService = videoInfoService;
        this.commentService = commentService;
        this.userService = userService;
        this.reactionService = reactionService;
    }

    /*
    Объект videoInfo создается для проверки корректности ссылки, если она неправильная, то будет брошено
    VideoInfoNotFoundException, в ином случае будет выполняться как надо
     */
    @PostMapping(value = "/like/{link}", produces = "application/json")
    public Reaction likeVideo(@PathVariable String link, Principal principal) throws VideoInfoNotFoundException {
        VideoInfo videoInfo = videoInfoService.getVideo(link);
        return reactionService.handleReaction(principal.getName(), link, ReactionType.LIKE);
    }

    /*
    Объект videoInfo создается для проверки корректности ссылки, если она неправильная, то будет брошено
    VideoInfoNotFoundException, в ином случае будет выполняться как надо
     */
    @PostMapping(value = "/dislike/{link}", produces = "application/json")
    public Reaction dislikeVideo(@PathVariable String link, Principal principal) throws VideoInfoNotFoundException {
        VideoInfo videoInfo = videoInfoService.getVideo(link);
        return reactionService.handleReaction(principal.getName(), link, ReactionType.DISLIKE);
    }
}
