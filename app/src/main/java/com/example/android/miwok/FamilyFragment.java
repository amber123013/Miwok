package com.example.android.miwok;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import static android.media.AudioManager.AUDIOFOCUS_GAIN;

/**
 * A simple {@link Fragment} subclass.
 */
public class FamilyFragment extends Fragment {

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
    public FamilyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);
        //初始化并设置mAudioManager 用于获取audio focus
        // Fragment 无法访问系统服务，而 Activity 可以
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("father", "әpә",R.drawable.family_father,R.raw.family_father));
        words.add(new Word("mother", "әṭa",R.drawable.family_mother,R.raw.family_mother));
        words.add(new Word("son", "angsi",R.drawable.family_son,R.raw.family_son));
        words.add(new Word("daughter", "tune",R.drawable.family_daughter,R.raw.family_daughter));
        words.add(new Word("older brother", "taachi",R.drawable.family_older_brother,R.raw.family_older_brother));
        words.add(new Word("younger brother", "chalitti",R.drawable.family_younger_brother,R.raw.family_younger_brother));
        words.add(new Word("older sister", "teṭe",R.drawable.family_older_sister,R.raw.family_older_sister));
        words.add(new Word("younger sister", "kolliti",R.drawable.family_younger_sister,R.raw.family_younger_sister));
        words.add(new Word("grandmother ", "ama",R.drawable.family_grandmother,R.raw.family_grandmother));
        words.add(new Word("grandfather", "paapa",R.drawable.family_grandfather,R.raw.family_grandfather));

        WordAdapter adapter = new WordAdapter(getActivity(), words,R.color.category_family);
        ListView listView = (ListView) rootView.findViewById(R.id.list);
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
                    mMediaPlayer = MediaPlayer.create(getActivity(), word.getAudioResourceId());
                    mMediaPlayer.start();
                    //创建监听
                    mMediaPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });
        return rootView;
    }
    /**
     * 在应用离开次页面（相当于ColorsActivity）后
     * 清空mMediaPlayer使其停止播放
     */
    @Override
    public void onStop() {
        super.onStop();
        releaseMediaPlayer();
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
