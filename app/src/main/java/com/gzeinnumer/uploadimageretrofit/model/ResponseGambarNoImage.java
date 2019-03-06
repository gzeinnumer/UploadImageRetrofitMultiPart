package com.gzeinnumer.uploadimageretrofit.model;

import com.google.gson.annotations.SerializedName;

public class ResponseGambarNoImage{

	@SerializedName("sukses")
	private boolean sukses;

	@SerializedName("name")
	private String name;

	@SerializedName("url")
	private String url;

	public void setSukses(boolean sukses){
		this.sukses = sukses;
	}

	public boolean isSukses(){
		return sukses;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}
}