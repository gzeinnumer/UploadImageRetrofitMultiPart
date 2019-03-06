package com.gzeinnumer.uploadimageretrofit.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseGetData{

	@SerializedName("images")
	private List<ImagesItem> images;

	@SerializedName("sukses")
	private boolean sukses;

	public void setImages(List<ImagesItem> images){
		this.images = images;
	}

	public List<ImagesItem> getImages(){
		return images;
	}

	public void setSukses(boolean sukses){
		this.sukses = sukses;
	}

	public boolean isSukses(){
		return sukses;
	}
}