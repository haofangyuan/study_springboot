package com.hfy;

import com.hfy.proto.ImageTest;

/**
 * Created by hfy on 2017/11/17.
 */
public interface ImageRepository {

    void uploadImage(ImageTest.Data data);
}
