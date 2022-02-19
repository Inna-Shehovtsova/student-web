package ru.student.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.student.dto.PostDTO;
import ru.student.entity.Post;
import ru.student.entity.Tag;
import ru.student.entity.User;
import ru.student.repository.PostRepository;
import ru.student.repository.TagRepository;
import ru.student.repository.UserRepository;
import ru.student.service.PostService;
import ru.student.service.TagService;
import ru.student.service.UserService;
import ru.student.util.SecurityUtils;

import javax.servlet.ServletContext;

@Controller

public class PostController {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PostService postService;
    private final ServletContext servletContext;
    private final TagRepository tagRepository;
    private  final TagService tagService;

    @Autowired
    public PostController(PostRepository postRepository,
                          UserRepository userRepository,
                          UserService userService,
                          PostService postService,
                          ServletContext servletContext,
                          TagRepository tagRepository,
                          TagService tagService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.postService = postService;
        this.servletContext = servletContext;
        this.tagRepository = tagRepository;
        this.tagService = tagService;
    }

    @GetMapping
    public String posts(@RequestParam(name = "q", required = false) String query,
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
    public String postsByUser(@PathVariable String username, ModelMap model) {
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


    @GetMapping("/post/{postId}/edit")
    @PreAuthorize("hasRole('USER')")
    public String postEdit(@PathVariable Long postId, ModelMap model){
        Post post = postService.findById(postId);
        SecurityUtils.checkAuthorityOnPost(post);

        model.put("post", new PostDTO(post));
        setCommonParams(model);
        return "post-edit";
    }

    @PostMapping("/post/edit")
    @PreAuthorize("hasRole('USER')")
    public String postEdit(PostDTO postDto){
        postService.update(postDto);
        return "redirect:/post/" + postDto.getPostId();
    }

    @GetMapping("/tag/{tagname}")
    public String postByTag(@PathVariable String tagname, ModelMap model){
        Tag t = tagService.getPostByTag(tagname);
        model.put("posts", t.getPosts());
        model.put("title", "Search by tag");
        model.put("subtitle", tagname);
        setCommonParams(model);
        return "blog";
    }
//    @GetMapping("/post/{postId}")
//    public String post(@PathVariable Long postId, ModelMap model){
//        model.put("post", postService.findById(postId));
//        setCommonParams(model);
//        return "post";
//    }

    @PostMapping("/post/{postId}/delete")
    @ResponseStatus(HttpStatus.OK)
    public void postDelete(@PathVariable Long postId){
        postService.delete(postId);
    }

    @GetMapping("/post/{postId}")
    public String postView(@PathVariable Long postId, ModelMap model){
        Post post = postService.findById(postId);
        model.put("post", post);

        setCommonParams(model);
        return "post";
    }



    private void setCommonParams(ModelMap model) {
        model.put("users", userRepository.findAll());
        model.put("contextPath", servletContext.getContextPath());
        model.put("tags", tagRepository.findAll());
    }

//    private final PostRepository postRepository;
//    private final UserRepository userRepository;
//    private final UserService userService;
//    private final PostService postService;
//    private final ServletContext servletContext;
//
//    @Autowired
//    public PostController(PostRepository postRepository,
//                          UserRepository userRepository,
//                          UserService userService,
//                          PostService postService,
//                          ServletContext servletContext) {
//        this.postRepository = postRepository;
//        this.userRepository = userRepository;
//        this.userService = userService;
//        this.postService = postService;
//        this.servletContext = servletContext;
//    }
//
//    @GetMapping
//    public String posts(@RequestParam(name = "q", required = false) String query,
//                        ModelMap model) {
//        if (StringUtils.hasText(query)) {
//            model.put("posts",
//                    postRepository.findByContentContainingIgnoreCase(query,
//                            Sort.by("dtCreated").descending()));
//            model.put("title", "Search by");
//            model.put("subtitle", query.length() < 20
//                    ? query
//                    : query.substring(0, 20) + "...");
//        } else {
//            model.put("posts", postRepository.findAll(Sort.by("dtCreated").descending()));
//            model.put("title", "All posts");
//        }
//
//        setCommonParams(model);
//        return "blog";
//    }
//
//    @GetMapping("/user/{username}")
//    public String postsByUser(@PathVariable String username, ModelMap model) {
//        User user = userService.findByUsername(username);
//        model.put("posts", user.getPosts());
//        model.put("title", "Search by");
//        model.put("subtitle", username);
//
//        setCommonParams(model);
//        return "blog";
//    }
//
//    @GetMapping("/post/new")
//    @PreAuthorize("hasRole('USER')")
//    public String postNew(ModelMap model){
//        setCommonParams(model);
//        return "post-new";
//    }
//
//    @PostMapping("/post/new")
//    @PreAuthorize("hasRole('USER')")
//    public String postNew(PostDto postDto){
//        Long postId = postService.create(postDto);
//        return "redirect:/post/" + postId;
//    }
//
//
//    @GetMapping("/post/{postId}/edit")
//    @PreAuthorize("hasRole('USER')")
//    public String postEdit(@PathVariable Long postId, ModelMap model){
//        Post post = postService.findById(postId);
//        SecurityUtils.checkAuthorityOnPost(post);
//
//        model.put("post", new PostDto(post));
//        setCommonParams(model);
//        return "post-edit";
//    }
//
//    @PostMapping("/post/edit")
//    @PreAuthorize("hasRole('USER')")
//    public String postEdit(PostDto postDto){
//        postService.update(postDto);
//        return "redirect:/post/" + postDto.getPostId();
//    }
//
//    @GetMapping("/post/{postId}")
//    public String post(@PathVariable Long postId, ModelMap model){
//        model.put("post", postService.findById(postId));
//        setCommonParams(model);
//        return "post";
//    }
//
//    @PostMapping("/post/{postId}/delete")
//    @ResponseStatus(HttpStatus.OK)
//    public void postDelete(@PathVariable Long postId){
//        postService.delete(postId);
//    }
//
//    @GetMapping("/post/{postId}")
//    public String postView(@PathVariable Long postId, ModelMap model){
//        Post post = postService.findById(postId);
//        model.put("post", post);
//        setCommonParams(model);
//        return "post";
//    }
//
//
//
//    private void setCommonParams(ModelMap model) {
//        model.put("users", userRepository.findAll());
//        model.put("contextPath", servletContext.getContextPath());
//    }
}
