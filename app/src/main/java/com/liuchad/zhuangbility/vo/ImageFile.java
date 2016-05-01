package com.liuchad.zhuangbility.vo;

public class ImageFile {
    public String path;
    public String name;
    public long time;

    public ImageFile(String path, String name, long time){
        this.path = path;
        this.name = name;
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        try {
            ImageFile other = (ImageFile) o;
            return this.path.equalsIgnoreCase(other.path);
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
