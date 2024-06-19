package com.example.project.service;

import com.example.project.model.Followers;
import com.example.project.repository.FollowersRepository;
import com.example.project.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FollowersService {

    private final FollowersRepository followersRepository;
    private final UserRepository userRepository;

    @Autowired
    public FollowersService(FollowersRepository followersRepository, UserRepository userRepository) {
        this.followersRepository = followersRepository;
        this.userRepository = userRepository;
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
        if (followersRepository.existsAllByUserIdAndSubUserId(followers.getUserId(), followers.getSubUserId())) {
            return false;
        }
        if (userRepository.existsById(followers.getUserId())) {
            newFollower.setUserId(followers.getUserId());
        } else {
            log.error("There is no such user");
            return false;
        }
        if (userRepository.existsById(followers.getSubUserId())) {
            newFollower.setSubUserId(followers.getSubUserId());
        } else {
            log.error("There is no such user");
            return false;
        }
        Followers savedFollow = followersRepository.save(newFollower);
        return getFollowById(savedFollow.getId()).isPresent();
    }

    public Boolean updateFollow(Followers followers) {
        Optional<Followers> followFromDBOptional = followersRepository.findById(followers.getId());
        if (followFromDBOptional.isPresent()) {
            Followers followFromDB = followFromDBOptional.get();
            if (followersRepository.existsAllByUserIdAndSubUserId(followers.getUserId(), followers.getSubUserId())) {
                return false;
            }
            if (followers.getUserId() != null && userRepository.existsById(followers.getUserId())) {
                followFromDB.setUserId(followers.getUserId());
            } else return false;
            if (followers.getSubUserId() != null && userRepository.existsById(followers.getSubUserId())) {
                followFromDB.setSubUserId(followers.getSubUserId());
            } else return false;
            Followers updatedFollow = followersRepository.saveAndFlush(followFromDB);
            return followFromDB.equals(updatedFollow);
        }
        return false;
    }
}
