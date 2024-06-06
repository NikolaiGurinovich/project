package com.example.project.service;

import com.example.project.model.Likes;
import com.example.project.repository.LikesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikesService {
    private LikesRepository likesRepository;

    @Autowired
    public LikesService(LikesRepository likesRepository) {
        this.likesRepository = likesRepository;
    }

    public List<Likes> getAllLikes() {
        return likesRepository.findAll();
    }

    public Optional<Likes> getLikeById(Long id) {
        return likesRepository.findById(id);
    }

    public Boolean deleteLikeById(Long id) {
        Optional<Likes> like = likesRepository.findById(id);
        if (like.isEmpty()) {
            return false;
        }
        likesRepository.deleteById(id);
        return likesRepository.findById(id).isEmpty();
    }

    public Boolean createLike(Likes likes) {
        if (likesRepository.existsAllByUserIdAndWorkoutId(likes.getUserId(), likes.getWorkoutId())) {
            return false;
        }
        Likes like = new Likes();
        like.setUserId(likes.getUserId());
        like.setWorkoutId(likes.getWorkoutId());
        Likes savedLike = likesRepository.save(like);
        return getLikeById(savedLike.getId()).isPresent();
    }

    public Boolean updateLike(Likes like) {
        Optional<Likes> likeFromDBOptional = likesRepository.findById(like.getId());
        if (likeFromDBOptional.isPresent()) {
            Likes likeFromDB = likeFromDBOptional.get();
            if (likesRepository.existsAllByUserIdAndWorkoutId(like.getUserId(), like.getWorkoutId())) {
                return false;
            }
            if (like.getUserId() != null){
                likeFromDB.setUserId(like.getUserId());
            } else return false;
            if (like.getWorkoutId() != null){
                likeFromDB.setWorkoutId(like.getWorkoutId());
            } else return false;
            Likes updatedLike = likesRepository.saveAndFlush(likeFromDB);
            return likeFromDB.equals(updatedLike);
        }
        return false;
    }

    public Boolean likeWorkout(Long workoutId, Long userId ) {
        if (likesRepository.existsAllByUserIdAndWorkoutId(userId, workoutId)) {
            return false;
        }
        Likes likes = new Likes();
        if(workoutId != null){
            likes.setWorkoutId(workoutId);
        } else return false;
        if(userId != null){
            likes.setUserId(userId);
        } else return false;
        Likes savedLikes = likesRepository.save(likes);
        return getLikeById(savedLikes.getId()).isPresent();
    }
}
