package com.liuchad.zhuangbility.vo;

import java.util.List;

public class ImageFolder {
    public String name;
    public String path;
    public ImageFile cover;
    public List<ImageFile> images;

    @Override
    public boolean equals(Object o) {
        try {
            ImageFolder other = (ImageFolder) o;
            return this.path.equalsIgnoreCase(other.path);
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        return super.equals(o);
    }
}