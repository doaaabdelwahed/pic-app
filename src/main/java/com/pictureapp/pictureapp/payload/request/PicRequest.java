package com.pictureapp.pictureapp.payload.request;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.pictureapp.pictureapp.models.PicCategory;

public class PicRequest {
	 private String description;

	    @Enumerated(EnumType.STRING)
	    private PicCategory category;

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public PicCategory getCategory() {
			return category;
		}

		public void setCategory(PicCategory category) {
			this.category = category;
		}
	    
}
