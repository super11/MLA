package com.mla.newsapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.mla.newsapp.R;
import com.mla.newsapp.model.VideosItem;
import com.mla.newsapp.utils.DashRendererBuilder;
import com.mla.newsapp.utils.DemoPlayer;
import com.mla.newsapp.utils.DemoUtil;
import com.mla.newsapp.utils.EventLogger;
import com.mla.newsapp.utils.ExtractorRendererBuilder;
import com.mla.newsapp.utils.HlsRendererBuilder;
import com.mla.newsapp.utils.SmoothStreamingRendererBuilder;
import com.mla.newsapp.utils.SmoothStreamingTestMediaDrmCallback;
import com.mla.newsapp.utils.UnsupportedDrmException;
import com.mla.newsapp.utils.WidevineTestMediaDrmCallback;
import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.VideoSurfaceView;
import com.google.android.exoplayer.audio.AudioCapabilities;
import com.google.android.exoplayer.audio.AudioCapabilitiesReceiver;
import com.google.android.exoplayer.extractor.mp3.Mp3Extractor;
import com.google.android.exoplayer.extractor.mp4.Mp4Extractor;
import com.google.android.exoplayer.extractor.ts.AdtsExtractor;
import com.google.android.exoplayer.extractor.ts.TsExtractor;
import com.google.android.exoplayer.extractor.webm.WebmExtractor;
import com.google.android.exoplayer.metadata.GeobMetadata;
import com.google.android.exoplayer.metadata.PrivMetadata;
import com.google.android.exoplayer.metadata.TxxxMetadata;
import com.google.android.exoplayer.text.SubtitleView;
import com.google.android.exoplayer.util.Util;
import com.google.android.exoplayer.util.VerboseLogUtil;

import java.util.ArrayList;
import java.util.Map;

public class VideoDetailViewPagerAdapter extends PagerAdapter implements SurfaceHolder.Callback,DemoPlayer.Listener, DemoPlayer.TextListener, DemoPlayer.Id3MetadataListener,
        AudioCapabilitiesReceiver.Listener, MediaController.MediaPlayerControl {
    private Context mContext;
    private int numberOfPages;


// Exoplayer

    public static final String CONTENT_TYPE_EXTRA = "content_type";
    public static final String CONTENT_ID_EXTRA = "content_id";

    private static final String TAG = "PlayerActivity";

    private static final float CAPTION_LINE_HEIGHT_RATIO = 0.0533f;
    private static final int MENU_GROUP_TRACKS = 1;
    private static final int ID_OFFSET = 2;

    private EventLogger eventLogger;
    private MediaController mediaController;
    private View debugRootView;
    private View shutterView;
    private VideoSurfaceView surfaceView;
    private TextView debugTextView;
    private TextView playerStateTextView;
    private SubtitleView subtitleView;
    private Button videoButton;
    private Button audioButton;
    private Button textButton;
    private Button retryButton;
    private Button verboseButton;

    private DemoPlayer player;
    private boolean playerNeedsPrepare;

    private long playerPosition;
    private boolean enableBackgroundAudio;

    private Uri contentUri;
    private int contentType;
    private String contentId;
    private ArrayList<VideosItem> video_list;

    private AudioCapabilitiesReceiver audioCapabilitiesReceiver;
    private AudioCapabilities audioCapabilities;


    public VideoDetailViewPagerAdapter(Context context , int numberOfPages, ArrayList<VideosItem> video_list) {
        mContext = context;
        this.numberOfPages = numberOfPages;
        this.video_list = video_list;
    }


    @Override
    public int getCount() {
        return numberOfPages;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {


        LayoutInflater inflater = (LayoutInflater) container.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.activity_video_details, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,
                        "Page " + position + " clicked",
                        Toast.LENGTH_LONG).show();

            }
        });

        contentUri = Uri.parse("http://html5demos.com/assets/dizzy.mp4");
        //contentUri = Uri.parse("https://www.youtube.com/watch?v=-Fe4dk0Jtcw");
        contentType = DemoUtil.TYPE_MP4;


        View root = view.findViewById(R.id.root);
        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    preparePlayer();
                    toggleControlsVisibility();

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    view.performClick();
                }
                return true;
            }
        });
        root.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE) {
                    return mediaController.dispatchKeyEvent(event);
                }
                return false;
            }
        });

        //Notifies a listener when the audio playback capabilities change. Call register() to start receiving notifications, and unregister() to stop.
        audioCapabilitiesReceiver = new AudioCapabilitiesReceiver(mContext, this);

        shutterView = view.findViewById(R.id.shutter);
        debugRootView = view.findViewById(R.id.controls_root);

        surfaceView = (VideoSurfaceView) view.findViewById(R.id.surface_view);

        //Return the SurfaceHolder ,providing access and control over this SurfaceView's underlying surface.
        surfaceView.getHolder().addCallback(this);
        debugTextView = (TextView) view.findViewById(R.id.debug_text_view);

        playerStateTextView = (TextView) view.findViewById(R.id.player_state_view);
        subtitleView = (SubtitleView) view.findViewById(R.id.subtitles);

        mediaController = new MediaController(mContext);
        mediaController.setAnchorView(root);

        retryButton = (Button) view.findViewById(R.id.retry_button);
        retryButton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        });
        videoButton = (Button) view.findViewById(R.id.video_controls);
        audioButton = (Button) view.findViewById(R.id.audio_controls);
        textButton = (Button) view.findViewById(R.id.text_controls);
        verboseButton=(Button) view.findViewById(R.id.verbose_log_controls);
        verboseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVerboseLogPopup(v);
            }
        });

        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVideoPopup(v);
            }
        });

        audioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAudioPopup(v);
            }
        });


        TextView textView =(TextView)view.findViewById(R.id.debug_sample_text);
        textView.setText(video_list.get(position).getTitle());
        textView.setTextColor(Color.GREEN);

        (container).addView(view, 0);

        return view;

    }

    //ViewPager adapter callbacks
    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }

    @Override
    public float getPageWidth(int position) {
        return super.getPageWidth(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout) object);
    }

    //Media Player control callbcks

    @Override
    public void start() {

    }

    @Override
    public void pause() {

    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public void seekTo(int pos) {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {

        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return false;
    }

    @Override
    public boolean canSeekForward() {
        return false;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }


    // AudioCapabilitiesReceiver.Listener methods

    @Override
    public void onAudioCapabilitiesChanged(AudioCapabilities audioCapabilities) {
        boolean audioCapabilitiesChanged = !audioCapabilities.equals(this.audioCapabilities);
        if (player == null || audioCapabilitiesChanged) {
            this.audioCapabilities = audioCapabilities;
            releasePlayer();
            preparePlayer();
        } else if (player != null) {
            player.setBackgrounded(false);
        }
    }


    // Internal methods

    private DemoPlayer.RendererBuilder getRendererBuilder() {
        String userAgent = Util.getUserAgent(mContext, "ExoPlayerDemo");
        switch (contentType) {
            case DemoUtil.TYPE_SS:
                return new SmoothStreamingRendererBuilder(mContext, userAgent, contentUri.toString(),
                        new SmoothStreamingTestMediaDrmCallback(), debugTextView);
            case DemoUtil.TYPE_DASH:
                return new DashRendererBuilder(mContext, userAgent, contentUri.toString(),
                        new WidevineTestMediaDrmCallback(contentId), debugTextView, audioCapabilities);
            case DemoUtil.TYPE_HLS:
                return new HlsRendererBuilder(mContext, userAgent, contentUri.toString(), debugTextView,
                        audioCapabilities);
            case DemoUtil.TYPE_M4A: // There are no file format differences between M4A and MP4.
            case DemoUtil.TYPE_MP4:
                return new ExtractorRendererBuilder(userAgent, contentUri, debugTextView,
                        new Mp4Extractor());
            case DemoUtil.TYPE_MP3:
                return new ExtractorRendererBuilder(userAgent, contentUri, debugTextView,
                        new Mp3Extractor());
            case DemoUtil.TYPE_TS:
                return new ExtractorRendererBuilder(userAgent, contentUri, debugTextView,
                        new TsExtractor(0, audioCapabilities));
            case DemoUtil.TYPE_AAC:
                return new ExtractorRendererBuilder(userAgent, contentUri, debugTextView,
                        new AdtsExtractor());
            case DemoUtil.TYPE_WEBM:
                return new ExtractorRendererBuilder(userAgent, contentUri, debugTextView,
                        new WebmExtractor());
            default:
                throw new IllegalStateException("Unsupported type: " + contentType);
        }
    }

    private void preparePlayer() {
        if (player == null) {
            player = new DemoPlayer(getRendererBuilder());
            player.addListener(this);
            player.setTextListener(this);
            player.setMetadataListener(this);
            player.seekTo(playerPosition);
            playerNeedsPrepare = true;
            mediaController.setMediaPlayer(player.getPlayerControl());
            mediaController.setEnabled(true);
            eventLogger = new EventLogger();
            eventLogger.startSession();
            player.addListener(eventLogger);
            player.setInfoListener(eventLogger);
            player.setInternalErrorListener(eventLogger);
        }
        if (playerNeedsPrepare) {
            player.prepare();
            playerNeedsPrepare = false;
            updateButtonVisibilities();
        }
        player.setSurface(surfaceView.getHolder().getSurface());
        player.setPlayWhenReady(true);
        audioCapabilitiesReceiver.register();
    }

    private void releasePlayer() {
        if (player != null) {
            playerPosition = player.getCurrentPosition();
            audioCapabilitiesReceiver.unregister();
            player.release();
            player = null;
            eventLogger.endSession();
            eventLogger = null;
        }
    }


    // DemoPlayer.Listener implementation

    @Override
    public void onStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == ExoPlayer.STATE_ENDED) {
            showControls();
        }
        String text = "playWhenReady=" + playWhenReady + ", playbackState=";
        switch(playbackState) {
            case ExoPlayer.STATE_BUFFERING:
                text += "buffering";
                break;
            case ExoPlayer.STATE_ENDED:
                text += "ended";
                break;
            case ExoPlayer.STATE_IDLE:
                text += "idle";
                break;
            case ExoPlayer.STATE_PREPARING:
                text += "preparing";
                break;
            case ExoPlayer.STATE_READY:
                text += "ready";
                break;
            default:
                text += "unknown";
                break;
        }
        playerStateTextView.setText(text);
        updateButtonVisibilities();
    }

    @Override
    public void onError(Exception e) {
        if (e instanceof UnsupportedDrmException) {
            // Special case DRM failures.
            UnsupportedDrmException unsupportedDrmException = (UnsupportedDrmException) e;
            int stringId = unsupportedDrmException.reason == UnsupportedDrmException.REASON_NO_DRM
                    ? R.string.drm_error_not_supported
                    : unsupportedDrmException.reason == UnsupportedDrmException.REASON_UNSUPPORTED_SCHEME
                    ? R.string.drm_error_unsupported_scheme
                    : R.string.drm_error_unknown;
            Toast.makeText(mContext, stringId, Toast.LENGTH_LONG).show();
        }
        playerNeedsPrepare = true;
        updateButtonVisibilities();
        showControls();
    }

    @Override
    public void onVideoSizeChanged(int width, int height, float pixelWidthAspectRatio) {
        shutterView.setVisibility(View.GONE);
        surfaceView.setVideoWidthHeightRatio(
                height == 0 ? 1 : (width * pixelWidthAspectRatio) / height);
    }


    // User controls

    private void updateButtonVisibilities() {
        retryButton.setVisibility(playerNeedsPrepare ? View.VISIBLE : View.GONE);
        videoButton.setVisibility(haveTracks(DemoPlayer.TYPE_VIDEO) ? View.VISIBLE : View.GONE);
        audioButton.setVisibility(haveTracks(DemoPlayer.TYPE_AUDIO) ? View.VISIBLE : View.GONE);
        textButton.setVisibility(haveTracks(DemoPlayer.TYPE_TEXT) ? View.VISIBLE : View.GONE);
    }

    private boolean haveTracks(int type) {
        return player != null && player.getTracks(type) != null;
    }

    public void showVideoPopup(View v) {
        PopupMenu popup = new PopupMenu(mContext, v);
        configurePopupWithTracks(popup, null, DemoPlayer.TYPE_VIDEO);
        popup.show();
    }

    public void showAudioPopup(View v) {
        PopupMenu popup = new PopupMenu(mContext, v);
        Menu menu = popup.getMenu();
        menu.add(Menu.NONE, Menu.NONE, Menu.NONE, R.string.enable_background_audio);
        final MenuItem backgroundAudioItem = menu.findItem(0);
        backgroundAudioItem.setCheckable(true);
        backgroundAudioItem.setChecked(enableBackgroundAudio);
        PopupMenu.OnMenuItemClickListener clickListener = new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item == backgroundAudioItem) {
                    enableBackgroundAudio = !item.isChecked();
                    return true;
                }
                return false;
            }
        };
        configurePopupWithTracks(popup, clickListener, DemoPlayer.TYPE_AUDIO);
        popup.show();
    }

    public void showTextPopup(View v) {
        PopupMenu popup = new PopupMenu(mContext, v);
        configurePopupWithTracks(popup, null, DemoPlayer.TYPE_TEXT);
        popup.show();
    }


    public void showVerboseLogPopup(View v) {
        PopupMenu popup = new PopupMenu(mContext, v);
        Menu menu = popup.getMenu();
        menu.add(Menu.NONE, 0, Menu.NONE, R.string.logging_normal);
        menu.add(Menu.NONE, 1, Menu.NONE, R.string.logging_verbose);
        menu.setGroupCheckable(Menu.NONE, true, true);
        menu.findItem((VerboseLogUtil.areAllTagsEnabled()) ? 1 : 0).setChecked(true);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == 0) {
                    VerboseLogUtil.setEnableAllTags(false);
                } else {
                    VerboseLogUtil.setEnableAllTags(true);
                }
                return true;
            }
        });
        popup.show();
    }

    private void configurePopupWithTracks(PopupMenu popup,
                                          final PopupMenu.OnMenuItemClickListener customActionClickListener,
                                          final int trackType) {
        if (player == null) {
            return;
        }
        String[] tracks = player.getTracks(trackType);
        if (tracks == null) {
            return;
        }
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return (customActionClickListener != null
                        && customActionClickListener.onMenuItemClick(item))
                        || onTrackItemClick(item, trackType);
            }
        });
        Menu menu = popup.getMenu();
        // ID_OFFSET ensures we avoid clashing with Menu.NONE (which equals 0)
        menu.add(MENU_GROUP_TRACKS, DemoPlayer.DISABLED_TRACK + ID_OFFSET, Menu.NONE, R.string.off);
        if (tracks.length == 1 && TextUtils.isEmpty(tracks[0])) {
            menu.add(MENU_GROUP_TRACKS, DemoPlayer.PRIMARY_TRACK + ID_OFFSET, Menu.NONE, R.string.on);
        } else {
            for (int i = 0; i < tracks.length; i++) {
                menu.add(MENU_GROUP_TRACKS, i + ID_OFFSET, Menu.NONE, tracks[i]);
            }
        }
        menu.setGroupCheckable(MENU_GROUP_TRACKS, true, true);
        menu.findItem(player.getSelectedTrackIndex(trackType) + ID_OFFSET).setChecked(true);
    }

    private boolean onTrackItemClick(MenuItem item, int type) {
        if (player == null || item.getGroupId() != MENU_GROUP_TRACKS) {
            return false;
        }
        player.selectTrack(type, item.getItemId() - ID_OFFSET);
        return true;
    }

    private void toggleControlsVisibility()  {
        if (mediaController.isShowing()) {
            mediaController.hide();
            debugRootView.setVisibility(View.GONE);
        } else {
            showControls();
        }
    }

    private void showControls() {
        mediaController.show(0);
        debugRootView.setVisibility(View.VISIBLE);
    }

    // DemoPlayer.TextListener implementation

    @Override
    public void onText(String text) {
        if (TextUtils.isEmpty(text)) {
            subtitleView.setVisibility(View.INVISIBLE);
        } else {
            subtitleView.setVisibility(View.VISIBLE);
            subtitleView.setText(text);
        }
    }

    // DemoPlayer.MetadataListener implementation

    @Override
    public void onId3Metadata(Map<String, Object> metadata) {
        for (Map.Entry<String, Object> entry : metadata.entrySet()) {
            if (TxxxMetadata.TYPE.equals(entry.getKey())) {
                TxxxMetadata txxxMetadata = (TxxxMetadata) entry.getValue();
                Log.i(TAG, String.format("ID3 TimedMetadata %s: description=%s, value=%s",
                        TxxxMetadata.TYPE, txxxMetadata.description, txxxMetadata.value));
            } else if (PrivMetadata.TYPE.equals(entry.getKey())) {
                PrivMetadata privMetadata = (PrivMetadata) entry.getValue();
                Log.i(TAG, String.format("ID3 TimedMetadata %s: owner=%s",
                        PrivMetadata.TYPE, privMetadata.owner));
            } else if (GeobMetadata.TYPE.equals(entry.getKey())) {
                GeobMetadata geobMetadata = (GeobMetadata) entry.getValue();
                Log.i(TAG, String.format("ID3 TimedMetadata %s: mimeType=%s, filename=%s, description=%s",
                        GeobMetadata.TYPE, geobMetadata.mimeType, geobMetadata.filename,
                        geobMetadata.description));
            } else {
                Log.i(TAG, String.format("ID3 TimedMetadata %s", entry.getKey()));
            }
        }
    }

    // SurfaceHolder.Callback implementation

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (player != null) {
            player.setSurface(holder.getSurface());
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Do nothing.
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (player != null) {
            player.blockingClearSurface();
        }
    }
}