package ru.student.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.student.entity.Post;
import ru.student.entity.Tag;
import ru.student.repository.TagRepository;

import java.util.List;

@Service
@Transactional
public class TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository repository) {
        this.tagRepository = repository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void create(String name){
        Tag tag = new Tag();
        tag.setName(name);
        tagRepository.save(tag);
    }

    @Transactional(readOnly = true)
    public long count(){
        return tagRepository.count();
    }

    @Transactional(readOnly = true)
    public Tag getPostByTag(String name){
        Tag t = tagRepository.findByName(name).orElseThrow();
        t.getPosts().size();
        return t;
    }

}
