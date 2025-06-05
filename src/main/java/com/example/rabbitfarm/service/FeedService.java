package com.example.rabbitfarm.service;

import com.example.rabbitfarm.model.Feed;
import com.example.rabbitfarm.repository.FeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FeedService {

    private final FeedRepository feedRepository;

    @Autowired
    public FeedService(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    public Feed saveFeed(Feed feed) {
        return feedRepository.save(feed);
    }

    public List<Feed> getAllFeeds() {
        return feedRepository.findAll();
    }

    public Optional<Feed> getFeedById(Long id) {
        return feedRepository.findById(id);
    }

    public Feed updateFeed(Long id, Feed feedDetails) {
        Feed feed = feedRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feed not found with id: " + id));
        feed.setName(feedDetails.getName());
        feed.setType(feedDetails.getType());
        feed.setQuantityKg(feedDetails.getQuantityKg());
        feed.setPurchaseDate(feedDetails.getPurchaseDate());
        feed.setExpiryDate(feedDetails.getExpiryDate());
        feed.setSupplier(feedDetails.getSupplier());
        return feedRepository.save(feed);
    }

    public void deleteFeed(Long id) {
        if (!feedRepository.existsById(id)) {
            throw new RuntimeException("Feed not found with id: " + id);
        }
        feedRepository.deleteById(id);
    }
}
