package com.example.android.miwok;

/**
 * Created by ASUS on 2017-02-08.
 */

public class Word {

    //Miwok单词翻译
    private String mDefaultTranslation;
    //Miwok单词
    private String mMiwokTranslation;
    //该单词的图片资源id
    private int mImageResourceId = NO_IMAGE_PROVIDED;
    //无图片常量
    private static final int NO_IMAGE_PROVIDED = -1;
    //声音资源id
    private int mAudioResourceId;
    public Word(String defaultTranslation, String miwokTranslation,int imageResourceId,int audioResourceId) {
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mImageResourceId = imageResourceId;
        mAudioResourceId = audioResourceId;
    }
    public Word(String defaultTranslation, String miwokTranslation,int audioResourceId) {
        mDefaultTranslation = defaultTranslation;
        mMiwokTranslation = miwokTranslation;
        mAudioResourceId = audioResourceId;
    }

    public String getDefaultTranslation() {
        return mDefaultTranslation;
    }
    public String getMiwokTranslation() {
        return mMiwokTranslation;
    }
    public int getImageResourceId(){
        return mImageResourceId;
    }
    public int getAudioResourceId() {
        return mAudioResourceId;
    }

    /**
     * 是否存在图片
     * @return flag
     */
    public boolean hasImage(){
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }

    @Override
    public String toString() {
        return "Word{" +
                "mDefaultTranslation='" + mDefaultTranslation + '\'' +
                ", mMiwokTranslation='" + mMiwokTranslation + '\'' +
                ", mImageResourceId=" + mImageResourceId +
                ", mAudioResourceId=" + mAudioResourceId +
                '}';
    }
}
