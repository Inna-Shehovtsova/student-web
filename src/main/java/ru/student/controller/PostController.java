package ru.student.controller;

import net.bytebuddy.matcher.StringMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.student.dto.PostDTO;
import ru.student.entity.Post;
import ru.student.entity.User;
import ru.student.repository.PostRepository;
import ru.student.repository.UserRepository;
import ru.student.service.PostService;
import ru.student.service.UserService;

import javax.servlet.ServletContext;
import java.util.List;

@Controller

public class PostController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PostService postService;
    private final ServletContext servletContext;

    @Autowired
    public PostController(PostRepository postRepository,
                          UserRepository userRepository,
                          UserService userService,
                          PostService postService,
                          ServletContext servletContext) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.postService = postService;
        this.servletContext = servletContext;
    }


    @GetMapping
    public String posts(@RequestParam(name = "search", required = false) String query,
                        ModelMap model) {
        if (StringUtils.hasText(query)) {
            model.put("posts",
                    postRepository.findByContentContainingIgnoreCase(query,
                            Sort.by("dtCreated").descending()));
            model.put("title", "Search by");
            model.put("subtitle", query.length() < 20
                    ? query
                    : query.substring(0, 20) + "...");
        } else {
            model.put("posts", postRepository.findAll(Sort.by("dtCreated").descending()));
            model.put("title", "All posts");
        }

        setCommonParams(model);
        return "blog";
    }

    @GetMapping("/user/{username}")
    public String postsByUser(@PathVariable String username, ModelMap model){
        User user = userService.findByUsername(username);
        model.put("posts", user.getPostList());
        model.put("title", "Search by");
        model.put("subtitle", username);
        setCommonParams(model);
        return "blog";
    }

    @GetMapping("/post/new")
    @PreAuthorize("hasRole('USER')")
    public String postNew(ModelMap model){
        setCommonParams(model);
        return "post-new";
    }

    @PostMapping("/post/new")
    @PreAuthorize("hasRole('USER')")
    public String postNew(PostDTO postDto){
        Long postId = postService.create(postDto);
        return "redirect:/post/" + postId;
    }

    private void setCommonParams(ModelMap model) {
        model.put("users", userRepository.findAll());
        model.put("contextPath", servletContext.getContextPath());
    }
}
