package com.zuccessful.cleanwise.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.zuccessful.cleanwise.R;
import com.zuccessful.cleanwise.utilities.Utilities_MT17010;

import java.util.ArrayList;

/**
 * Created by spark on 16/3/18.
 */

public class ImageAdapter_MT17010 extends RecyclerView.Adapter<ImageAdapter_MT17010.MyViewHolder> {

    private static final int VIEW_TYPE_ADD_IMAGE = R.layout.image_add_item;
    private static final int VIEW_TYPE_IMAGE = R.layout.image_item;
    private ArrayList<Uri> images;
    private Context mContext;
    private ArrayList<String> imagesPath;
    private static ClickListener clickListener;

    public ImageAdapter_MT17010(Context context) {
        images = new ArrayList<>();
        imagesPath = new ArrayList<>();
        this.mContext = context;
    }

    public ImageAdapter_MT17010(Context context, ArrayList<Uri> images, ArrayList<String> imagesPath) {
        this.images = images;
        this.imagesPath = imagesPath;
        this.mContext = context;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_ADD_IMAGE) {
            view = LayoutInflater.from(mContext).inflate(R.layout.image_add_item, parent, false);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        }
        return new MyViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return (position == images.size() ? VIEW_TYPE_ADD_IMAGE : VIEW_TYPE_IMAGE);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        if (position != images.size())
            Utilities_MT17010.setPic(holder.washroomImage, imagesPath.get(position), 110, 110);
    }

    public void deleteImage(int position) {
        Toast.makeText(mContext, "Clicked", Toast.LENGTH_SHORT).show();
        images.remove(position);
        imagesPath.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return images.size() + 1;
    }

    public void updateImageList(ArrayList<Uri> image_list, ArrayList<String> imagesPath) {
        this.images = image_list;
        this.imagesPath = imagesPath;
        notifyDataSetChanged();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView washroomImage;
        private ImageView deleteImage;
        private ViewGroup addImage;

        MyViewHolder(View itemView) {
            super(itemView);

            washroomImage = itemView.findViewById(R.id.washroom_image);
            deleteImage = itemView.findViewById(R.id.delete_image);
            addImage = itemView.findViewById(R.id.add_image_item);

            if (deleteImage != null)
                deleteImage.setOnClickListener(this);
            if (addImage != null)
                addImage.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        ImageAdapter_MT17010.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View view);
    }
}
