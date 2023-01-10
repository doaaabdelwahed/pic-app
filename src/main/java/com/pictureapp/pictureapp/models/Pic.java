package com.pictureapp.pictureapp.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;





@Setter
@Getter
@Entity
@Table(name = "pic")
public class Pic  implements Serializable{

    private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
  
    @ManyToOne
    @JoinColumn (name = "user_id", referencedColumnName = "id")
    private User user;

    private String description;

    @Enumerated(EnumType.STRING)
    private PicCategory category;

    private String path;

    @Enumerated(EnumType.STRING)
    private PicStatus status;



}
