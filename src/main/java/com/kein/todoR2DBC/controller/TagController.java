package com.kein.todoR2DBC.controller;

import com.kein.todoR2DBC.domain.Tag;
import com.kein.todoR2DBC.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin
public class TagController {
    @Autowired
    TagService tagService;

    @GetMapping(value = "/tags",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Tag> getTags(){
        return tagService.getTags();
    }

    @PostMapping("/tags")
    public Mono<Tag> createTag(@RequestBody Tag tag){
        return tagService.createTag(tag);
    }
    @DeleteMapping("/tags/{id}")
    public Mono<Void> deleteTagById(@PathVariable("id") Long id){
        return tagService.deleteTagById(id);
    }
}
