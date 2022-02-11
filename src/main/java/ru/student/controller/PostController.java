package ru.student.controller;

import net.bytebuddy.matcher.StringMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.student.entity.Post;
import ru.student.entity.User;
import ru.student.repository.PostRepository;
import ru.student.repository.UserRepository;
import ru.student.service.PostService;
import ru.student.service.UserService;

import java.util.List;

@Controller
//@RequestMapping("/post")
public class PostController {

    private final PostRepository postRepository;
    private final PostService postService;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public PostController(PostRepository postRepository,
                          PostService postService,
                          UserRepository userRepository,
                          UserService userService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping("/")
    public String posts(ModelMap model){
        model.put("posts", postRepository.findAll());
        setCommonParams(model);
        return "blog";
    }

    @GetMapping(value="/", params = {"search"})
    public String searchByContent(@RequestParam final String search, ModelMap model){
        List<Post> p = postService.findSubstring(search);
        model.put("posts", p);
        setCommonParams(model);
        return("blog");
    }

    @GetMapping("/user/{username}")
    public String postsByUser(@PathVariable String username, ModelMap model){
        User user = userService.findByUsername(username);
        model.put("posts", user.getPostList());
        setCommonParams(model);
        return "blog";
    }

    private void setCommonParams(ModelMap model){
        model.put("users", userRepository.findAll());
    }
}
