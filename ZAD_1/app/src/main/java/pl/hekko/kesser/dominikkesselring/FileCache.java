package pl.hekko.kesser.dominikkesselring;/*
 * Created by kesser on 24.01.16.
 */

import java.io.File;
import android.content.Context;

class FileCache {

    private final File cacheDir;

    public FileCache(Context context){
        //Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"LazyAdapter");
        else
            cacheDir=context.getCacheDir();
        if(!cacheDir.exists())
            cacheDir.mkdirs();
    }

    public File getFile(String url){
        String filename=String.valueOf(url.hashCode());
        return new File(cacheDir, filename);

    }

}