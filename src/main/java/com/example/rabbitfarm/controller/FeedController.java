package com.example.rabbitfarm.controller;

import com.example.rabbitfarm.model.Feed;
import com.example.rabbitfarm.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/feed")
public class FeedController {

    private final FeedService feedService;

    @Autowired
    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @PostMapping
    public ResponseEntity<Feed> createFeed(@RequestBody Feed feed) {
        Feed savedFeed = feedService.saveFeed(feed);
        return new ResponseEntity<>(savedFeed, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Feed>> getAllFeeds() {
        List<Feed> feeds = feedService.getAllFeeds();
        return ResponseEntity.ok(feeds);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Feed> getFeedById(@PathVariable Long id) {
        Optional<Feed> feed = feedService.getFeedById(id);
        return feed.map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Feed> updateFeed(@PathVariable Long id, @RequestBody Feed feedDetails) {
        try {
            Feed updatedFeed = feedService.updateFeed(id, feedDetails);
            return ResponseEntity.ok(updatedFeed);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteFeed(@PathVariable Long id) {
        try {
            feedService.deleteFeed(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
