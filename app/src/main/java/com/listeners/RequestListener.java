package com.listeners;

/**
 * Created by saadn on 10/16/2017.
 */

public interface RequestListener {
    public void onSuccess(String result,String token,String user_id,String name,String email,String dob,String phone,String cnic,String level,String ad_views);
    public void onError(String result);
}
