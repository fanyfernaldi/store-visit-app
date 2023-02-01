package com.fanyfernaldi.pitjarustest.presentation.store.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.fanyfernaldi.pitjarustest.R
import com.fanyfernaldi.pitjarustest.databinding.ItemStoreBinding
import com.fanyfernaldi.pitjarustest.domain.Store

class StoreListAdapter(private val storeList: List<Store>) :
    RecyclerView.Adapter<StoreListAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding = ItemStoreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val store = storeList[position]
        holder.binding.apply {
            llCheckedStore.isVisible = store.isChecked
            tvStoreName.text = store.storeName
            if (store.distance.toDouble().toInt() >= 1000) {
                val distanceInKm = (store.distance.toDouble().toInt() / 1000).toString()
                tvDistance.text =
                    root.context.getString(R.string.label_kilo_meter_value, distanceInKm)
            } else {
                tvDistance.text = root.context.getString(R.string.label_meter_value, store.distance)
            }
            // TODO: get data from database if the data is provided in database
            tvCluster.text = root.context.getString(R.string.label_cluster_value, "Small")
            tvDescription.text = "TT Regular - IRTM Big Store"
            root.setOnClickListener {
                onItemClickCallback.onItemClicked(store)
            }
        }
    }

    override fun getItemCount(): Int = storeList.size

    class ListViewHolder(var binding: ItemStoreBinding) : RecyclerView.ViewHolder(binding.root)

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Store)
    }
}
