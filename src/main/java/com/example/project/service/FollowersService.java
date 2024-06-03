package com.example.project.service;

import com.example.project.model.Followers;
import com.example.project.repository.FollowersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FollowersService {
    private final FollowersRepository followersRepository;

    @Autowired
    public FollowersService(FollowersRepository followersRepository) {
        this.followersRepository = followersRepository;
    }

    public List<Followers> getAllFollowers() {
        return followersRepository.findAll();
    }

    public Optional<Followers> getFollowById(Long id) {
        return followersRepository.findById(id);
    }

    public Boolean deleteFollowById(Long id) {
        Optional<Followers> follower = followersRepository.findById(id);
        if (follower.isEmpty()) {
            return false;
        }
        followersRepository.deleteById(id);
        return followersRepository.findById(id).isEmpty();
    }

    public Boolean createFollow(Followers followers) {
        Followers newFollower = new Followers();
        newFollower.setUserId(followers.getUserId());
        newFollower.setSubUserId(followers.getSubUserId());
        Followers savedFollow = followersRepository.save(newFollower);
        return getFollowById(savedFollow.getId()).isPresent();
    }

    public Boolean updateFollow(Followers followers) {
        Optional<Followers> followFromDBOptional = followersRepository.findById(followers.getId());
        if (followFromDBOptional.isPresent()) {
            Followers followFromDB = followFromDBOptional.get();
            if (followers.getUserId() != null){
                followFromDB.setUserId(followers.getUserId());
            } else return false;
            if (followers.getSubUserId() != null){
                followFromDB.setSubUserId(followers.getSubUserId());
            } else return false;
            Followers updatedFollow = followersRepository.saveAndFlush(followFromDB);
            return followFromDB.equals(updatedFollow);
        }
        return false;
    }
}
