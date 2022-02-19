package ru.student.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.student.dto.PostDTO;
import ru.student.entity.Comment;
import ru.student.repository.CommentRepository;
import ru.student.repository.PostRepository;
import ru.student.repository.UserRepository;
import ru.student.util.SecurityUtils;

@Controller
public class CommentController {

        private final CommentRepository commentRepository;
        private final PostRepository postRepository;
        private final UserRepository userRepository;
        @Autowired
        public  CommentController(CommentRepository commentRepository,
                                  PostRepository postRepository,
                                  UserRepository userRepository){
            this.commentRepository = commentRepository;
            this.postRepository = postRepository;
            this.userRepository = userRepository;
        }
        @PostMapping("/comment/create")
        @PreAuthorize("hasRole('USER')")
        public String commentAdd(@PathVariable String content, PostDTO post){
            Comment comment = new Comment();
            comment.setContent(content);
            comment.setPost(postRepository.getById(post.getPostId()));
            comment.setUser(userRepository.findByUsername(SecurityUtils.getCurrentUserDetails().getUsername()).orElseThrow());
            return "redirect:/post/" + post.getPostId();
        }
    }

