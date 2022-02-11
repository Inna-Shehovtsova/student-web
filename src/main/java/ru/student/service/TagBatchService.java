package ru.student.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TagBatchService {

    private final TagService tagService;

    @Autowired
    public TagBatchService(TagService tagService) {
        this.tagService = tagService;
    }

    public void createTags(List<String> tags){
        tags.forEach(tagService::create);
    }

}
