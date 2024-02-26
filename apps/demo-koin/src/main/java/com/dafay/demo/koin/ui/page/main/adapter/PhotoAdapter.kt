package com.dafay.demo.coroutine.pages.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dafay.demo.coroutine.data.model.Photo
import com.dafay.demo.koin.databinding.ItemPhotoCardBinding
import com.dafay.demo.lib.base.ui.adapter.BaseAdapter

/**
 * @Des
 * @Author lipengfei
 * @Date 2024/2/20
 */
class PhotoAdapter : BaseAdapter<Photo>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return PhotoCardViewHolder(ItemPhotoCardBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when {
            holder is PhotoCardViewHolder -> {
                holder.onBindViewHolder(position, datas[position])
            }
        }
    }

    class PhotoCardViewHolder : RecyclerView.ViewHolder {
        val binding: ItemPhotoCardBinding

        constructor(itemView: ItemPhotoCardBinding) : super(itemView.root) {
            binding = itemView
        }

        fun onBindViewHolder(position: Int, photo: Photo) {
            Glide.with(binding.root.context)
                .load(photo.src.medium)
                .transition(DrawableTransitionOptions.withCrossFade(240))
                .placeholder(ColorDrawable(Color.parseColor(photo.avg_color)))
                .into(binding.ivImg)
            binding.tvDes.text = photo.alt
            binding.mcvCard.setOnClickListener {
            }
        }
    }
}