package com.payment.arthpay.utill;

import android.os.Environment;

import java.io.File;
import java.util.HashMap;


public class FolderManager {

    public static final String HIGH_RESOLUTION_IMAGE_PATH = "High";
    public static final String LOW_RESOLUTION_IMAGE_PATH = "Low";

    public static final String IMG_EXT = ".jpg";
    public static final String VID_EXT = ".mp4";
    public static final String HIGH_IMAGE_TAG = "high";

    public static File getTempFilePath(String filePath) {
        String imageDirPath = Environment.getExternalStorageDirectory() + File.separator + "Temp" + File.separator + "Rec";
        File imagefile = new File(imageDirPath);
        if (!imagefile.exists()) {
            imagefile.mkdirs();
        }
        return imagefile;
    }

    public static boolean deleteLastFromDCIM() {

        boolean success = false;
        try {
            File[] images = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "DCIM/Camera").listFiles();
            File latestSavedImage = images[0];
            for (int i = 1; i < images.length; ++i) {
                if (images[i].lastModified() > latestSavedImage.lastModified()) {
                    latestSavedImage = images[i];
                }
            }

            // OR JUST Use  success = latestSavedImage.delete();
            success = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "DCIM/Camera/"
                    + latestSavedImage.getAbsoluteFile()).delete();
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return success;
        }

    }

    public static HashMap<String, String> getImgDir(String formDirName, String imgName){

        HashMap<String, String> myDirectories = new HashMap<>();
        String highImagePath = "";
        String lowImagePath = "";

        String imageDirPath = Environment.getExternalStorageDirectory() + File.separator+ "itsmapy" + File.separator
                + formDirName + File.separator+"Image";
        File imagefile = new File(imageDirPath);
        if(!imagefile.exists()){
            imagefile.mkdirs();
        }
        if (imgName != null) {
            lowImagePath = imageDirPath+ File.separator+imgName+IMG_EXT;
            highImagePath = imageDirPath+ File.separator+imgName+"_"+HIGH_IMAGE_TAG+IMG_EXT;
        }
        myDirectories.put(HIGH_RESOLUTION_IMAGE_PATH,highImagePath);
        myDirectories.put(LOW_RESOLUTION_IMAGE_PATH,lowImagePath);

        return myDirectories;
    }

}
