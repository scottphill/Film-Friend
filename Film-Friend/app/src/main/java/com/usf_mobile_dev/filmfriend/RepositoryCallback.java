package com.usf_mobile_dev.filmfriend;

public interface RepositoryCallback<T> {
    void onComplete(ThreadResult<T> result);
}
