package ru.student.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.student.entity.Post;
import ru.student.repository.PostRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> findSubstring(String content){
        List<Post> p = postRepository.findByContentContaining(content);
        List<Post> t = postRepository.findByTitleContaining(content);
        p.addAll(t);
        return  p;
    }

    }
