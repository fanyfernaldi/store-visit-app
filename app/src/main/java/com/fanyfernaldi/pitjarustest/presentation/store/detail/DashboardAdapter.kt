package com.fanyfernaldi.pitjarustest.presentation.store.detail

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fanyfernaldi.pitjarustest.databinding.ItemDashboardCardBinding
import com.fanyfernaldi.pitjarustest.domain.Dashboard

class DashboardAdapter(private val dashboardList: List<Dashboard>) :
    RecyclerView.Adapter<DashboardAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding =
            ItemDashboardCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    @SuppressLint("Range")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val dashboard = dashboardList[position]
        holder.binding.apply {
            tvTitle.text = dashboard.title
            tvDate.text = dashboard.date
            tvPercentage.text = dashboard.percentage
            tvTargetValue.text = dashboard.target
            tvAchievementValue.text = dashboard.achievement
            cvCard.setCardBackgroundColor(Color.parseColor(dashboard.color))
            root.setOnClickListener {
                onItemClickCallback.onItemClicked(dashboard)
            }
        }
    }

    override fun getItemCount(): Int = dashboardList.size

    class ListViewHolder(var binding: ItemDashboardCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Dashboard)
    }
}