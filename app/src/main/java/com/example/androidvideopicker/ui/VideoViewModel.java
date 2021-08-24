package com.example.androidvideopicker.ui;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.SavedStateHandle;

import com.example.androidvideopicker.data.entity.VideoAlbumName;
import com.example.androidvideopicker.data.entity.VideoAlbumNameRvItem;
import com.example.androidvideopicker.data.entity.VideoMedia;
import com.example.androidvideopicker.data.entity.VideoRvItem;
import com.example.androidvideopicker.data.repository.VideoRepository;
import com.example.androidvideopicker.data.local.LocalMediaVideoSource;
import com.example.androidvideopicker.utils.DeviceVersionCheck;
import com.example.androidvideopicker.data.repository.RepositoryReturnDataListener;
import com.example.androidvideopicker.utils.SingleLiveEvent;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public
class VideoViewModel extends AndroidViewModel
        implements
        RepositoryReturnDataListener,
        VideoRvItem.OnVideoListItemClickListener,
        VideoAlbumNameRvItem.AlbumNameOnClickListener {

    Application application;

    public VideoViewModel(@NonNull Application application ) {
        super(application);
        this.application = application;
        setLiveData();
    }

    private  LocalMediaVideoSource localMediaVideoSource = new LocalMediaVideoSource();

    public MutableLiveData<List<VideoRvItem>> videoItemLiveData = new MutableLiveData<>();

    private  DeviceVersionCheck deviceVersionCheck = new DeviceVersionCheck();

    private VideoRepository videoRepository;

    private  HashMap<String, Object> returnMap = new HashMap<>();
    public SingleLiveEvent singleLiveEvent = new SingleLiveEvent();

    private VideoRvItem.OnVideoListItemClickListener videoListItemClickListener;

    private VideoAlbumNameRvItem.AlbumNameOnClickListener videoAlbumNameItemClickListener;

    public MutableLiveData<List<VideoAlbumNameRvItem>> videoAlbumLiveData = new MutableLiveData<>();

    public ObservableField<Boolean> isNotExistVideoFile = new ObservableField<>();


    public void init() {

        videoListItemClickListener = this;
        videoAlbumNameItemClickListener = this;

        videoRepository = new VideoRepository(localMediaVideoSource, application, this, deviceVersionCheck);
        getLocalMediaVideo();
        getLocalMediaVideoAlbumName();
    }

    public void getLocalMediaVideo() {
        videoRepository.getLocalMediaVideoInfomation();
    }

    public void getLocalMediaVideoAlbumName() {
        videoRepository.getLocalMediaVideoName();
    }


    @Override
    public void successReturn(String flag, Object object) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                switch (flag) {
                    case "videoInformation":
                        ArrayList<VideoMedia> returnedVideoList = (ArrayList<VideoMedia>) object;
                        ArrayList<VideoRvItem> videoRvItemList = new ArrayList<>();

                        for (VideoMedia i : returnedVideoList) {
                            videoRvItemList.add(
                                    new VideoRvItem(
                                            i, false, videoListItemClickListener
                                    )
                            );
                        }
                        videoItemLiveData.setValue(videoRvItemList);


                        returnMap.clear();
                        returnMap.put("flag", "videoInformation");
                        singleLiveEvent.setValue(returnMap);

                        isNotExistVideoFile.set(false);
                        break;

                    case "videoAlbumName":

                        ArrayList<VideoAlbumName> returnedVideoAlbumList = (ArrayList<VideoAlbumName>) object;
                        ArrayList<VideoAlbumNameRvItem> videoAlbumNameRvItemList = new ArrayList<>();
                        returnedVideoAlbumList.add(0/*앨범 첫번째는 전체를 보여주도록함*/, new VideoAlbumName("all", "전체"));

                        for (VideoAlbumName i : returnedVideoAlbumList) {
                            videoAlbumNameRvItemList.add(
                                    new VideoAlbumNameRvItem(
                                            i, videoAlbumNameItemClickListener
                                    )
                            );
                        }

                        videoAlbumLiveData.setValue(videoAlbumNameRvItemList);
                        break;
                }
            }
        });
    }

    @Override
    public void failReturn(String flag, Object object) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                switch (flag) {
                    case "videoInformation":
                        List<VideoRvItem> videoRvItemArrayList = new ArrayList<>();
                        videoItemLiveData.setValue(videoRvItemArrayList);
                        returnMap.clear();
                        returnMap.put("flag", "videoInformationNotExist");
                        singleLiveEvent.setValue(returnMap);
                        break;
                }
            }
        });
    }

//    아래서 부터 리사이클러뷰와 묶인곳
    public MutableLiveData<Integer> selectedPositionAtRecyclerView = new MutableLiveData<>();
    public MutableLiveData<String> selectedAlbumBucketIdAtView = new MutableLiveData<>();
    public MutableLiveData<String> selectedAlbumDirNameAtView = new MutableLiveData<>();

    @Override
    public void onItemClick(View view, VideoRvItem item, int position) {
        returnMap.clear();
        returnMap.put("flag", "videoRecyclerViewClick");
        returnMap.put("prevPosition", selectedPositionAtRecyclerView.getValue());
        returnMap.put("newPosition", position);
        selectedPositionAtRecyclerView.setValue(position);
        singleLiveEvent.setValue(returnMap);

    }

    @Override
    public void onAlbumItemClick(View view, VideoAlbumName item, BottomSheetDialogFragment bottomSheetDialogFragment) {

        String nowBucketId = selectedAlbumBucketIdAtView.getValue();
        String selectedBucketId = item.getEniquePathOrId();

//        현재 선택되어져있는 앨범을 선택할 경우 리턴해서 아무일도 일어나지않도록함
        if (Objects.requireNonNull(
                nowBucketId).equals(selectedBucketId)) {
            bottomSheetDialogFragment.dismiss();
            return;
        }

        returnMap.clear();
        returnMap.put("flag", "albumNameBottomSheetClick");
        returnMap.put("prevPosition", selectedPositionAtRecyclerView.getValue());

        selectedAlbumBucketIdAtView.setValue(selectedBucketId);
        selectedAlbumDirNameAtView.setValue(item.getAlbumName());
        selectedPositionAtRecyclerView.setValue(0/*앨범을 선택하면 무조건 첫번째 영상이 선택되어 보이도록함 */);

        singleLiveEvent.setValue(returnMap);

        bottomSheetDialogFragment.dismiss();
    }



    private void setLiveData(){
//        savedStateHandle로 값 보관하지않고 메모리로 부족으로 죽었다 살아나면 처음으로 초기화되도록함
        selectedPositionAtRecyclerView.setValue(0);
        selectedAlbumBucketIdAtView.setValue("all");
        selectedAlbumDirNameAtView.setValue("전체");
        isNotExistVideoFile.set(true);
    }
}
