package com.pictureapp.pictureapp.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pictureapp.pictureapp.models.Pic;
import com.pictureapp.pictureapp.models.PicCategory;
import com.pictureapp.pictureapp.models.PicStatus;
import com.pictureapp.pictureapp.models.User;

public interface PicRepository extends JpaRepository<Pic,Long> {

    List<Pic>findAllByStatusIn(List<PicStatus>  status);

    @Query(value="update Pic m set m.status='DELETED' where m.id=?1")
    void delete(Long id);

    List<Pic>findAllByUser(User user);

    List<Pic>findAllByCategoryIn(List<PicCategory>categories);
}
