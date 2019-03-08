package com.gzeinnumer.uploadimageretrofit.model;

import com.google.gson.annotations.SerializedName;

//todo 52.1
public class ResponseDelete{

	@SerializedName("result")
	private String result;

	@SerializedName("msg")
	private String msg;

	public void setResult(String result){
		this.result = result;
	}

	public String getResult(){
		return result;
	}

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}
}