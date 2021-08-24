package com.example.androidvideopicker.ui.bottomSheet;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.example.androidvideopicker.R;
import com.example.androidvideopicker.data.entity.VideoAlbumNameRvItem;
import com.example.androidvideopicker.databinding.RvOneContentsListDialogItemBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class VideoAlbumDialogFragment extends BottomSheetDialogFragment {


    ArrayList<VideoAlbumNameRvItem> albumNameArrayList;


    public VideoAlbumDialogFragment(ArrayList<VideoAlbumNameRvItem> albumNameArrayList) {
        this.albumNameArrayList = albumNameArrayList;
    }

    RecyclerView normalSelectRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_album_dialog_list_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        normalSelectRecyclerView = view.findViewById(R.id.normalSelectRecyclerView);
        normalSelectRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        normalSelectRecyclerView.setAdapter(new ItemAdapter());
    }


    private class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        ItemAdapter() {
        }

        @NonNull
        @Override
        public InnerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RvOneContentsListDialogItemBinding binding =
                    DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                            R.layout.rv_one_contents_list_dialog_item, parent, false);
            return new InnerViewHolder(
                    binding
            );
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            InnerViewHolder innerViewHolder = (InnerViewHolder) holder;
            innerViewHolder.binding.setItem(albumNameArrayList.get(position));
            innerViewHolder.binding.setBottomSheetDialogFragment(VideoAlbumDialogFragment.this);
        }

        private class InnerViewHolder extends RecyclerView.ViewHolder {

            RvOneContentsListDialogItemBinding binding;

            InnerViewHolder(RvOneContentsListDialogItemBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

        }

        @Override
        public int getItemCount() {
            return albumNameArrayList.size();
        }

    }


    @Override
    public int getTheme() {
//        상단 꼭지점 둥글게 하는용
        return R.style.CustomBottomSheetDialog;
    }
}