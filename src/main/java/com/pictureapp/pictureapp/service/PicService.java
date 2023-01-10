package com.pictureapp.pictureapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pictureapp.pictureapp.models.Pic;
import com.pictureapp.pictureapp.models.PicCategory;
import com.pictureapp.pictureapp.models.PicStatus;
import com.pictureapp.pictureapp.models.User;
import com.pictureapp.pictureapp.repository.PicRepository;
import com.pictureapp.pictureapp.repository.UserRepository;



@Service
public class PicService {

    private final PicRepository repository;
    @Autowired
    UserRepository userRepository;


    @Autowired
    public PicService(PicRepository picRepository) {
        this.repository = picRepository;
    }

    
    public List<Pic> getAll() {
        return new ArrayList<>(repository.findAll());
    }

    
    public List<Pic> getAllByStatus(List<PicStatus> status) {
        return new ArrayList<>(repository.findAllByStatusIn(status));
    }

    
    public List<Pic> getAllByCategory(List<PicCategory> categories) {
        return new ArrayList<>(repository.findAllByCategoryIn(categories));
    }

    
    public List<Pic> getAllByUser(User user) {
        return new ArrayList<>(repository.findAllByUser(user));
    }

    
    public Optional<Pic> getById(Long id) {
        return repository.findById(id);
    }

    
    public Pic save(Pic pic) {
        return repository.save(pic);
    }

    
    public void delete(Pic pic) {
        repository.delete(pic);
    }
 
}
