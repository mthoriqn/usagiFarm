package com.example.rabbitfarm.controller;

import com.example.rabbitfarm.model.Feed;
import com.example.rabbitfarm.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/feed")
public class FeedMvcController {

    private final FeedService feedService;

    @Autowired
    public FeedMvcController(FeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping
    public String listFeed(Model model) {
        List<Feed> feedItems = feedService.getAllFeeds();
        model.addAttribute("feedItems", feedItems);
        model.addAttribute("pageTitle", "Feed Stock Management");
        return "feed"; // src/main/resources/templates/feed.html
    }
}
