package com.gilbertohdz.android.politicalpreparedness.representative.adapter

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gilbertohdz.android.politicalpreparedness.R
import com.gilbertohdz.android.politicalpreparedness.databinding.ViewHolderRepresentativeBinding
import com.gilbertohdz.android.politicalpreparedness.network.models.Channel
import com.gilbertohdz.android.politicalpreparedness.representative.model.Representative

class RepresentativeListAdapter: ListAdapter<Representative, RepresentativeViewHolder>(RepresentativeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepresentativeViewHolder {
        return RepresentativeViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RepresentativeViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}

class RepresentativeViewHolder(val binding: ViewHolderRepresentativeBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Representative) {
        binding.representative = item
        binding.representativeItemImage.setImageResource(R.drawable.ic_profile)

        // DONE: Show social links ** Hint: Use provided helper methods
        // DONE: Show www link ** Hint: Use provided helper methods
        item.official.channels?.let { showSocialLinks(it) }
        item.official.urls?.let { showWWWLinks(it) }

        binding.executePendingBindings()
    }

    private fun showSocialLinks(channels: List<Channel>) {
        val facebookUrl = getFacebookUrl(channels)
        // DONE: Set social media facebook
        if (!facebookUrl.isNullOrBlank()) { enableLink(binding.representativeItemFacebookIcon, facebookUrl) }

        val twitterUrl = getTwitterUrl(channels)
        // DONE: Set social media twittr
        if (!twitterUrl.isNullOrBlank()) { enableLink(binding.representativeItemTwitterIcon, twitterUrl) }
    }

    private fun showWWWLinks(urls: List<String>) {
        // DONE: Set web intent icon
        enableLink(binding.representativeItemWwwIcon, urls.first())
    }

    private fun getFacebookUrl(channels: List<Channel>): String? {
        return channels.filter { channel -> channel.type == "Facebook" }
                .map { channel -> "https://www.facebook.com/${channel.id}" }
                .firstOrNull()
    }

    private fun getTwitterUrl(channels: List<Channel>): String? {
        return channels.filter { channel -> channel.type == "Twitter" }
                .map { channel -> "https://www.twitter.com/${channel.id}" }
                .firstOrNull()
    }

    private fun enableLink(view: ImageView, url: String) {
        view.visibility = View.VISIBLE
        view.setOnClickListener { setIntent(url) }
    }

    private fun setIntent(url: String) {
        val uri = Uri.parse(url)
        val intent = Intent(ACTION_VIEW, uri)
        itemView.context.startActivity(intent)
    }

    // DONE: Add companion object to inflate ViewHolder (from)
    companion object {
        fun from(parent: ViewGroup) : RepresentativeViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ViewHolderRepresentativeBinding.inflate(layoutInflater, parent, false)
            return RepresentativeViewHolder(binding)
        }
    }
}

// DINE: Create RepresentativeDiffCallback
class RepresentativeDiffCallback : DiffUtil.ItemCallback<Representative>() {
    override fun areItemsTheSame(oldItem: Representative, newItem: Representative): Boolean {
        return oldItem.office.name == newItem.office.name &&
                oldItem.official.name == newItem.official.name
    }

    override fun areContentsTheSame(oldItem: Representative, newItem: Representative): Boolean {
        return oldItem == newItem
    }
}

// DONE: Create RepresentativeListener
class RepresentativeListener(val block: (Representative) -> Unit) {
    fun onClick(representative: Representative) = block(representative)
}