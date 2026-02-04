package com.MSInnovation.nestify.views.dashboard.orderTracker

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.MSInnovation.nestify.data.models.OrderData
import com.MSInnovation.nestify.databinding.ItemCustomerInfoBinding
import com.MSInnovation.nestify.views.dashboard.orderTracker.customerDetails.CustomerDetailsActivity
import kotlin.apply

class OrderTrackerAdapter(private val dataList: MutableList<OrderData>) :
    RecyclerView.Adapter<OrderTrackerAdapter.OrderTrackerViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : OrderTrackerViewHolder {

        return OrderTrackerViewHolder(
            ItemCustomerInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OrderTrackerViewHolder, position: Int) {
        val item = dataList[position]

        holder.binding.apply {
            tvSlNo.text = item.orderNo
            tvCName.text = item.cName
            tvPID.text = "#${item.pid}"
            tvStatus.text = "  ${item.status}  "

            when (item.status.lowercase()) {
                "delivered" -> {
                    tvStatus.setTextColor(android.graphics.Color.parseColor("#00B670"))
                    tvStatus.setBackgroundColor(android.graphics.Color.parseColor("#D7FFDA"))
                }

                "pending" -> {
                    tvStatus.setTextColor(android.graphics.Color.parseColor("#856404"))
                    tvStatus.setBackgroundColor(android.graphics.Color.parseColor("#FFF3CD"))
                }

                "inreview" -> {
                    tvStatus.setTextColor(android.graphics.Color.parseColor("#303030"))
                    tvStatus.setBackgroundColor(android.graphics.Color.parseColor("#BABABA"))
                }
                "returned" -> {
                    tvStatus.setTextColor(android.graphics.Color.parseColor("#FFE8E8"))
                    tvStatus.setBackgroundColor(android.graphics.Color.parseColor("#D10000"))
                }
            }



            root.setOnLongClickListener {
                val context = root.context

                val clipboard =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("p_ID", item.pid)
                clipboard.setPrimaryClip(clip)
                true
            }

            root.setOnClickListener {
                val context = holder.itemView.context

                val intent = Intent(context, CustomerDetailsActivity::class.java).apply {
                    putExtra("slNo", item.orderNo)
                    putExtra("cName", item.cName)
                    putExtra("cPhone", item.cPhone)
                    putExtra("pID", item.pid)
                    putExtra("pName", item.pName)
                    putExtra("cLocation", item.cLocation)
                    putExtra("cDistrict", item.district)
                    putExtra("date", item.date)
                    putExtra("platform", item.platform)
                    putExtra("sellType", item.sellType)
                    putExtra("dStatus", item.status)
                    putExtra("buyP", item.bPrice)
                    putExtra("dCharge", item.dCharge)
                    putExtra("sellP", item.sPrice)
                    putExtra("takenDC", item.tCharge)
                    putExtra("packaging", item.packaging)
                    putExtra("adsCost", item.adsCost)
                    putExtra("profit", item.profit)
                    putExtra("pPercent", item.profitPercent)
                    putExtra("docId", item.docId)
                }
                context.startActivity(intent)
            }
            btnPhone.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = "tel:${item.cPhone}".toUri()
                }
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = dataList.size

    fun updateList(newList: List<OrderData>) {
        dataList.clear()
        dataList.addAll(newList)
        notifyDataSetChanged()
    }

    class OrderTrackerViewHolder(val binding: ItemCustomerInfoBinding) :
        RecyclerView.ViewHolder(binding.root)
}