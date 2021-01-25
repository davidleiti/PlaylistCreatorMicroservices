package com.ubb.david.playlistcreator.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ubb.david.playlistcreator.databinding.TrackItemBinding
import com.ubb.david.playlistcreator.domain.TrackDto

class TracksListAdapter(
    private val onOpenExternalClicked: (spotifyUrl: String) -> Unit,
    private val onDeleteClicked: (trackDto: TrackDto) -> Unit
) : RecyclerView.Adapter<TracksListAdapter.TrackViewHolder>() {

    private val _items = mutableListOf<TrackDto>()
    var items: List<TrackDto>
        get() = _items
        set(value) {
            _items.clear()
            _items.addAll(value)
            notifyDataSetChanged()
        }

    fun addTracks(tracks: List<TrackDto>) {
        _items.addAll(tracks)
        notifyDataSetChanged()
    }

    fun removeTrack(index: Int) {
        _items.removeAt(index)
        notifyItemChanged(index)
    }

    inner class TrackViewHolder(private val binding: TrackItemBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.openTrackPageButton.setOnClickListener {
                items.getOrNull(adapterPosition)?.let { trackDto -> onOpenExternalClicked(trackDto.spotifyUrl) }
            }
            binding.deleteTrackButton.setOnClickListener {
                items.getOrNull(adapterPosition)?.let { trackDto -> onDeleteClicked(trackDto) }
            }
        }

        fun bind(trackDto: TrackDto) {
            binding.track = trackDto
            Glide.with(binding.root)
                .load(trackDto.thumbnailUrl)
                .into(binding.albumCoverImage)
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TrackItemBinding.inflate(inflater, parent, false)
        return TrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        items.getOrNull(position)?.let { offer -> holder.bind(offer) }
    }
}