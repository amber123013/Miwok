package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import static android.media.AudioManager.AUDIOFOCUS_GAIN;

public class ColorsActivity extends AppCompatActivity {

    private MediaPlayer mMediaPlayer;
    /**
     * 新建一个OnCompletionListener，在音乐播放完成后触发回调onCompletion
     * 保存在是由成员变量mCompletionListener以免一直创建
     * 此监听将在调用.start() 方法后设置
     */
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            // Now that the sound file has finished playing, release the media player resources.
            releaseMediaPlayer();
        }
    };
    //    系统的AudioManager服务，用于请求Audio Focus
    private AudioManager mAudioManager;
    /**
     * 当audio focus改变时此listener将被触发
     * (例如 来电时电话应用播放铃声将会获取audio focus使此应用失去focus).
     */
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                //  AUDIOFOCUS_LOSS_TRANSIENT：暂时失去Audio Focus，并会很快再次获得。
                // AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK：暂时失去AudioFocus，但是可以继续播放，不过要在降低音量。
                //在这两种情况下都暂停播放 并将时间设置回开头，以便重新获deAudioFocus时从头开始播放
                mMediaPlayer.pause();
                mMediaPlayer.seekTo(0);
            } else if (focusChange == AUDIOFOCUS_GAIN) {
                //AUDIOFOCUS_GAIN：获得了Audio Focus；
                //开始播放
                mMediaPlayer.start();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                //  AUDIOFOCUS_LOSS：失去了Audio Focus，并将会持续很长的时间。
                // 直接释放资源
                releaseMediaPlayer();
            }
        }
    };
    /**
     * 在应用离开次页面（相当于ColorsActivity）后
     * 清空mMediaPlayer使其停止播放
     */
    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);
        //初始化并设置mAudioManager 用于获取audio focus
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("red", "weṭeṭṭi",R.drawable.color_red,R.raw.color_red));
        words.add(new Word("mustard yellow", "chiwiiṭә",R.drawable.color_mustard_yellow,R.raw.color_mustard_yellow));
        words.add(new Word("dusty yellow", "ṭopiisә",R.drawable.color_dusty_yellow,R.raw.color_dusty_yellow));
        words.add(new Word("green", "chokokki",R.drawable.color_green,R.raw.color_green));
        words.add(new Word("brown", "ṭakaakki",R.drawable.color_brown,R.raw.color_brown));
        words.add(new Word("gray", "ṭopoppi",R.drawable.color_gray,R.raw.color_gray));
        words.add(new Word("black", "kululli",R.drawable.color_black,R.raw.color_black));
        words.add(new Word("white", "kelelli",R.drawable.color_white,R.raw.color_white));

        WordAdapter adapter = new WordAdapter(this, words,R.color.category_colors);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
        //添加列表项点击监听
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //The position of the view in the adapter. position是ListView中视图的编号
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//              获取被点击项所在的word
                Word word = words.get(position);
                //初始化ediaPlayer前清空MediaPlayer
                releaseMediaPlayer();

                //AUDIOFOCUS_GAIN_TRANSIENT 指示要申请的AudioFocus是暂时性的，会很快用完释放的；
                int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener,
AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
//              AUDIOFOCUS_REQUEST_GRANTED：申请成功；
//              AUDIOFOCUS_REQUEST_FAILED：申请失败。
//               成功获取audio focus是执行设置资源、播放操作
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    //设置点击视图Miwok语的MP3资源id
                    mMediaPlayer = MediaPlayer.create(ColorsActivity.this, word.getAudioResourceId());
                    mMediaPlayer.start();
                    //创建监听
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });
    }
    /**
     * Clean up the media player by releasing its resources.
     */
    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mMediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mMediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mMediaPlayer = null;

            // Regardless of whether or not we were granted audio focus, abandon it. This also
            // unregisters the AudioFocusChangeListener so we don't get anymore callbacks.
//            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
            // 当音乐播放结束时 audio focus将被释放
            mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        }
    }
}
