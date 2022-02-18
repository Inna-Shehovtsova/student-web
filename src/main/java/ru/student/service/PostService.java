package ru.student.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.student.dto.PostDTO;
import ru.student.entity.Post;
import ru.student.entity.Tag;
import ru.student.repository.PostRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.student.repository.TagRepository;
import ru.student.repository.UserRepository;
import ru.student.util.SecurityUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.student.util.SecurityUtils.checkAuthorityOnPostOrUserIsAdmin;
import static ru.student.util.SecurityUtils.getCurrentUserDetails;

@Service
@Transactional
public class PostService {
    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Autowired
    public PostService(TagRepository tagRepository,
                       UserRepository userRepository,
                       PostRepository postRepository) {
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }


    public List<Post> findSubstring(String content){
        List<Post> t = postRepository.findByContentContainingIgnoreCase(content, Sort.by("dtCreated").descending());
        // postRepository.findByTitleContainingIgnoreCase(content);
        //;
        return  t;
    }
    @PreAuthorize("hasRole('USER')")
    public Long create(PostDTO postDto) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setTags(parseTags(postDto.getTags()));
        post.setUser(userRepository.findByUsername(getCurrentUserDetails()
                        .getUsername())
                .orElseThrow());
        post.setDtCreated(LocalDateTime.now());
        return postRepository.save(post).getPostId();
    }

    private List<Tag> parseTags(String tags) {
        if (tags == null) {
            return Collections.emptyList();
        }

        return Arrays.stream(tags.split(" "))
                .map(tag -> tagRepository.findByName(tag).orElseGet(
                        () -> tagRepository.save(new Tag(tag))))
                .collect(Collectors.toList());
    }

    public Post findById(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow();
        post.getTags().size();
        return post;
    }

    @PreAuthorize("hasRole('USER')")
    public void update(PostDTO postDto) {
        Post post = postRepository.findById(postDto.getPostId()).orElseThrow();
        SecurityUtils.checkAuthorityOnPost(post);

        if (postDto.getTitle() != null) {
            post.setTitle(StringUtils.hasText(postDto.getTitle())
                    ? postDto.getTitle()
                    : "");
        }

        if (postDto.getContent() != null) {
            post.setContent(StringUtils.hasText(postDto.getContent())
                    ? postDto.getContent()
                    : "");
        }

        if (postDto.getTags() != null) {
            post.setTags(StringUtils.hasText(postDto.getTags())
                    ? parseTags(postDto.getTags())
                    : Collections.emptyList());
        }

        post.setDtUpdated(LocalDateTime.now());
        postRepository.save(post);
    }

    public void delete(Long postId) {
        checkAuthorityOnPostOrUserIsAdmin(
                postRepository.findById(postId).orElseThrow());
        postRepository.deleteById(postId);
    }
    }
