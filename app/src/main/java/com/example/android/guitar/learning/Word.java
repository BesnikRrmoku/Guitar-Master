package com.example.android.guitar.learning;
public class Word {

    private int mDefaultTranslationId;
    private int mDescTranslationId;
    private int mAudioResourceId;
    private int mImageResourceId = NO_IMAGE_PROVIDED;
    private static final int NO_IMAGE_PROVIDED = -1;

    public Word(int defaultTranslationId, int descTranslationId, int audioResourceId) {
        mDefaultTranslationId = defaultTranslationId;
        mDescTranslationId = descTranslationId;
        mAudioResourceId = audioResourceId;
    }
    public Word(int defaultTranslationId, int descTranslationId, int imageResourceId,
                int audioResourceId) {
        mDefaultTranslationId = defaultTranslationId;
        mDescTranslationId = descTranslationId;
        mImageResourceId = imageResourceId;
        mAudioResourceId = audioResourceId;
    }
    public int getDefaultTranslationId() {
        return mDefaultTranslationId;
    }
    public int getTranslationId() {
        return mDescTranslationId;
    }
    public int getImageResourceId() {
        return mImageResourceId;
    }
    public boolean hasImage() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }
    public int getAudioResourceId() {
        return mAudioResourceId;
    }
}