package com.example.androidvideopicker.ui;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidvideopicker.MainActivity;
import com.example.androidvideopicker.R;
import com.example.androidvideopicker.data.entity.VideoAlbumNameRvItem;
import com.example.androidvideopicker.data.entity.VideoRvItem;
import com.example.androidvideopicker.databinding.FragmentVideoPickerBinding;
import com.example.androidvideopicker.ui.bottomSheet.VideoAlbumDialogFragment;
import com.example.androidvideopicker.ui.recycerViewAdapter.SelectLocalVideoAdapter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class VideoPickerFragment extends Fragment {


    private FragmentVideoPickerBinding binding;

    private VideoHeaderViewModel videoHeaderViewModel = new VideoHeaderViewModel();

    private SelectLocalVideoAdapter selectLocalVideoAdapter;

    private VideoViewModel videoViewModel;

    private VideoPlayerLocalViewModel videoPlayerLocalViewModel;

    public VideoPickerFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_video_picker, container, false);
        View view = binding.getRoot();

        setRecyclerView();
        setVideoHeaderView();
        initViewModel();

        binding.headerBiding.forwardImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResultFragment resultFragment = new ResultFragment();
                Bundle bundle = new Bundle();
                bundle.putString("contentUri", selectLocalVideoAdapter.filteredList
                        .get(videoViewModel.selectedPositionAtRecyclerView.getValue())
                        .getVideoMedia().getContentUri()
                );
                resultFragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction()
                        .add(R.id.fragmentContainerView, resultFragment)
                        .hide(VideoPickerFragment.this)
                        .show(resultFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            videoPlayerLocalViewModel.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initViewModel() {
        videoPlayerLocalViewModel = new ViewModelProvider(this).get(VideoPlayerLocalViewModel.class);
        binding.setVideoPlayerLocal(videoPlayerLocalViewModel);
        videoPlayerLocalViewModel.init(requireActivity(), binding.thumbnailExoVideoView);

        selectLocalVideoAdapter.setVideoPlayerLocalPicker(videoPlayerLocalViewModel.videoPlayerLocalPicker);
        getLifecycle().addObserver(videoPlayerLocalViewModel);

        videoViewModel = new ViewModelProvider(
                this
                , new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())
//                , new SavedStateViewModelFactory(getActivity().getApplication(), getActivity())
        ).get(VideoViewModel.class);
        binding.setVideoViewModel(videoViewModel);
        videoViewModel.init();

        setObserver();
    }

    private void setObserver() {

        videoViewModel.singleLiveEvent.observe(VideoPickerFragment.this, data -> {
            if (data instanceof HashMap) {

                HashMap<String, Object> returnData = (HashMap<String, Object>) data;
                String flag = (String) returnData.get("flag");

                switch (flag) {
                    case "videoInformation":

                        ArrayList<VideoRvItem> videoItemList = (ArrayList<VideoRvItem>) videoViewModel.videoItemLiveData.getValue();

                        videoHeaderViewModel.setTitle(videoViewModel.selectedAlbumDirNameAtView.getValue());

                        selectLocalVideoAdapter.setItemList(
                                videoItemList
                        );

                        selectLocalVideoAdapter.setSelectedPosition(
                                videoViewModel.selectedPositionAtRecyclerView.getValue()
                        );

                        selectLocalVideoAdapter.getFilter().filter(
                                videoViewModel.selectedAlbumBucketIdAtView.getValue()
                        );

                        binding.videoSelectGridRecyclerView.smoothScrollToPosition(
                                videoViewModel.selectedPositionAtRecyclerView.getValue()
                        );

                        break;

                    case "albumNameBottomSheetClick":

                        videoHeaderViewModel.setTitle(videoViewModel.selectedAlbumDirNameAtView.getValue());

                        int prevItemPosition = (int) returnData.get("prevPosition");

                        selectLocalVideoAdapter.cancelClickedItem(
                                prevItemPosition
                        );

                        selectLocalVideoAdapter.setSelectedPosition(
                                0 /*앨범을 선택하면 무조건 첫번째 영상이 선택되어 보이도록함 */
                        );

                        selectLocalVideoAdapter.getFilter().filter(
                                videoViewModel.selectedAlbumBucketIdAtView.getValue()
                        );

                        binding.videoSelectGridRecyclerView.smoothScrollToPosition(0/*앨범을 선택하면 무조건 첫번째 영상이 선택되어 보이도록함 */);

                        break;

                    case "videoRecyclerViewClick":

                        int prevPosition = (int) returnData.get("prevPosition");
                        int newPosition = (int) returnData.get("newPosition");

                        selectLocalVideoAdapter.userClickChangeInFilterList(
                                prevPosition, newPosition
                        );

                        break;

                    case "videoInformationNotExist":

                        new MaterialAlertDialogBuilder(requireActivity())
                                .setTitle("오류")
                                .setMessage("선택가능한 동영상이 없습니다")
                                .setCancelable(false)
                                .setPositiveButton("확인", /* listener = */ new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        requireActivity().onBackPressed();
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                        break;
                }
            }
        });
    }

    private void setRecyclerView() {

        if (selectLocalVideoAdapter == null) {
            selectLocalVideoAdapter = new SelectLocalVideoAdapter();
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
            binding.videoSelectGridRecyclerView.setLayoutManager(gridLayoutManager);
            binding.videoSelectGridRecyclerView.setAdapter(selectLocalVideoAdapter);

        } else {
            selectLocalVideoAdapter.notifyDataSetChanged();
        }

    }

    /**
     * < 앨범명▼ >  처럼 생긴 헤더뷰와 관련된 코드
     * video_ui_header.xml
     */
    private void setVideoHeaderView() {
        binding.setVideoHeaderViewModel(videoHeaderViewModel);

        videoHeaderViewModel.setTitle("전체");
        videoHeaderViewModel.setOnBackImageViewClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getActivity().getSupportFragmentManager().beginTransaction().remove(VideoPickerFragment.this).commit();
                requireActivity().onBackPressed();

            }
        });

        videoHeaderViewModel.setOnAlbumClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoViewModel != null) {
                    if (videoViewModel.videoAlbumLiveData != null) {
                        new VideoAlbumDialogFragment((ArrayList<VideoAlbumNameRvItem>) videoViewModel.videoAlbumLiveData.getValue())
                                .show(requireActivity().getSupportFragmentManager(), "show");
                    }
                }
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}