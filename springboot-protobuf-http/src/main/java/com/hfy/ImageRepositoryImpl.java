package com.hfy;

import com.google.protobuf.ByteString;
import com.hfy.proto.ImageTest;
import org.springframework.stereotype.Service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by hfy on 2017/11/17.
 */
@Service
public class ImageRepositoryImpl implements ImageRepository {

    @Override
    public void uploadImage(ImageTest.Data data) {
        ByteString photoData = data.getPhotoData();
        if (photoData != null) {
            byte[] bytes = photoData.toByteArray();
            System.out.println(data.getName());
            System.out.println(bytes.length);
//                for (byte b : bytes) {
//                    System.out.println(b);
//                }
            writeToFile(bytes, data.getName());
        }
    }


    private void writeToFile(byte[] bytes, String name) {
        String path = "d:/" + name + ".jpg";
        BufferedOutputStream outputStream = null;
        try {
            File newFile = new File(path);
            if (newFile.exists()) {
                boolean delete = newFile.delete();
                if (!delete) {
                    System.out.println("删除文件失败!");
                }
            }
            outputStream = new BufferedOutputStream(new FileOutputStream(newFile));
            outputStream.write(bytes);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != outputStream) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
