package com.example.androidvideopicker.ui.recycerViewAdapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidvideopicker.R;
import com.example.androidvideopicker.data.entity.VideoRvItem;
import com.example.androidvideopicker.databinding.RvGridVideoThumnailImgBinding;
import com.example.androidvideopicker.utils.VideoPlayerLocalPicker;

import java.util.ArrayList;

public
class SelectLocalVideoAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements Filterable {

    private ArrayList<VideoRvItem> videoItemLiveData;
    public ArrayList<VideoRvItem> filteredList = new ArrayList<>();

    private VideoPlayerLocalPicker videoPlayerLocalPicker;


//    현재 클릭 되어져있는 filteredList의 포지션을 저장하는 값 VideoRvItem isSelected 가 true 인 포지션이다
    private int selectedPosition = 0;

    public SelectLocalVideoAdapter() {
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RvGridVideoThumnailImgBinding binding =
                DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.rv_grid_video_thumnail_img, parent, false);

        return new VideoGridHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SelectLocalVideoAdapter.VideoGridHolder videoHolder = (VideoGridHolder) holder;
        VideoRvItem videoRvItem = filteredList.get(position);
        videoHolder.binding.setItem(videoRvItem);
        videoHolder.binding.setPosition(position);

    }

    @Override
    public int getItemCount() {
        if (filteredList != null) {
            return filteredList.size();
        } else {
            return 0;
        }
    }


    static class VideoGridHolder extends RecyclerView.ViewHolder {

        RvGridVideoThumnailImgBinding binding;

        public VideoGridHolder(@NonNull RvGridVideoThumnailImgBinding rvGridVideoThumnailImgBinding) {
            super(rvGridVideoThumnailImgBinding.getRoot());
            this.binding = rvGridVideoThumnailImgBinding;
        }
    }


    public void setItemList(ArrayList<VideoRvItem> itemList) {
        this.videoItemLiveData = itemList;
    }

    /**
     * 필터링된 아이템 포지션의 변경시켜주며
     * 새로선택한 동영상이 재생되도록함
     * @param prevPosition
     * @param newPostiion
     */
    public void userClickChangeInFilterList(int prevPosition, int newPostiion) {
        filteredList.get(prevPosition).setSelected(false);
        filteredList.get(newPostiion).setSelected(true);
        selectedPosition = newPostiion;
        videoPlayerLocalPicker.play(filteredList.get(newPostiion).getVideoMedia().getContentUri());
    }

    /**
     * 앨범을 바꾸었을떄 선택되어져 있는 포지션의 동영상을 처음상태로 초기화시켜줌
     * @param cancelPosition - 선택이 되어져잇던 포지션
     */
    public void cancelClickedItem(int cancelPosition) {
        filteredList.get(cancelPosition).setSelected(false);
    }


    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                filteredList.clear();

                String bucketId = constraint.toString();

                if (bucketId.equals("all")) {
                    filteredList = (ArrayList<VideoRvItem>) videoItemLiveData.clone();

                } else {
                    for (VideoRvItem i : videoItemLiveData) {
                        if (i.getVideoMedia().getBucketId().equals(bucketId)) {
                            filteredList.add(i);
                        }
                    }
                }

                if (filteredList.size() > 0) {
                    filteredList.get(selectedPosition).setSelected(true);
                }

                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
                videoPlayerLocalPicker.play(filteredList.get(selectedPosition).getVideoMedia().getContentUri());
            }
        };
    }


    public VideoPlayerLocalPicker getVideoPlayerLocalPicker() {
        return videoPlayerLocalPicker;
    }

    public void setVideoPlayerLocalPicker(VideoPlayerLocalPicker videoPlayerLocalPicker) {
        this.videoPlayerLocalPicker = videoPlayerLocalPicker;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }
}
