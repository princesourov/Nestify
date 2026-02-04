package com.MSInnovation.nestify.views.dashboard.orderTracker.customerDetails

import android.R
import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import com.MSInnovation.nestify.core.DataState
import com.MSInnovation.nestify.core.extract
import com.MSInnovation.nestify.core.isEmpty
import com.MSInnovation.nestify.databinding.ActivityCustomerDetailsBinding
import com.MSInnovation.nestify.views.dashboard.addOrder.AddOrderViewModel
import com.MSInnovation.nestify.views.dashboard.addOrder.Data
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomerDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerDetailsBinding
    private val viewModel: AddOrderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCustomerDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnShow.setOnClickListener {
            binding.card.apply {
                alpha = 0f
                visibility = android.view.View.VISIBLE
                animate().alpha(1f).setDuration(300).start()
            }
        }


        val slNo = intent.getStringExtra("slNo")
        val cName = intent.getStringExtra("cName")
        val cPhone = intent.getStringExtra("cPhone")
        val pID = intent.getStringExtra("pID")
        val pName = intent.getStringExtra("pName")
        val cLocation = intent.getStringExtra("cLocation")
        val cDistrict = intent.getStringExtra("cDistrict")
        val date = intent.getStringExtra("date")
        val platform = intent.getStringExtra("platform")
        val sellType = intent.getStringExtra("sellType")
        val dStatus = intent.getStringExtra("dStatus")
        val buyP = intent.getStringExtra("buyP")
        val dCharge = intent.getStringExtra("dCharge")
        val sellP = intent.getStringExtra("sellP")
        val takenDC = intent.getStringExtra("takenDC")
        val packaging = intent.getStringExtra("packaging")
        val adsCost = intent.getStringExtra("adsCost")
        val profit = intent.getStringExtra("profit")
        val pPercent = intent.getStringExtra("pPercent")
        val docId = intent.getStringExtra("docId") ?: ""


        val total = (sellP?.toIntOrNull() ?: 0) + (takenDC?.toIntOrNull() ?: 0)
        val orderNumber = slNo?.toIntOrNull()?.let { 1000 + it } ?: 0

        val buyPrice = buyP?.toDoubleOrNull() ?: 0.0
        val deliveryCharge = dCharge?.toDoubleOrNull() ?: 0.0
        val packagingCost = packaging?.toDoubleOrNull() ?: 0.0
        val adsCostValue = adsCost?.toDoubleOrNull() ?: 0.0
        val sellPrice = sellP?.toDoubleOrNull() ?: 0.0
        val takenCharge = takenDC?.toDoubleOrNull() ?: 0.0

        val totalCost = buyPrice + deliveryCharge + packagingCost + adsCostValue
        val totalSell = sellPrice + takenCharge

        val netProfit = totalSell - totalCost
        val profitPercent = if (totalCost != 0.0) (netProfit / totalCost) * 100 else 0.0


        binding.apply {
            tvSlNo.text = "Order No: $slNo"
            tvCustomerName.text = "Name: $cName"
            tvCustomerPhone.text = "Phone: $cPhone"
            tvProductId.text = "Parcel ID: $pID"
            tvProductName.text = "Product Name: $pName"
            tvLocation.text = "Location: $cLocation"
            tvDistrict.text = "District: $cDistrict"
            tvDate.text = "Date: $date"
            tvPlatform.text = "Platform: $platform"
            tvSellType.text = "Sell Type: $sellType"
            tvDeliveryStatus.text = "Delivery Status: $dStatus"
            tvBuyPrice.text = "Buy Price: ৳$buyP"
            tvDeliveryCharge.text = "Delivery Charge: ৳$dCharge"
            tvSellPrice.text = "Sell Price: ৳$sellP"
            tvTakenCharge.text = "Taken Delivery Charge: ৳$takenDC"
            tvPackagingCost.text = "Packaging Cost: ৳$packaging"
            tvAdsCost.text = "Ads Cost: ৳$adsCost"
            tvProfit.text = "Profit: ৳${"%.2f".format(netProfit)}"
            tvProfitPercent.text = "Profit %: ${"%.2f".format(profitPercent)}%"

            etPID.setText(pID)
            spinnerStatus.setText(dStatus)
            etDelivaryCharge.setText(dCharge)
            etAdsCost.setText(adsCost)
        }


        binding.btnCopy.setOnClickListener {
            val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

            val copyText = """
                অর্ডার কনফার্মড – Nestify ✅  
                অর্ডার নম্বর: #$orderNumber

                👤 নাম: $cName
                📞 মোবাইল নম্বর: $cPhone
                🏠 বিস্তারিত ঠিকানা: $cLocation
                📦 পণ্য (Qty): $pName
                💰 পণ্যের মূল্য: ৳$sellP
                🚚 ডেলিভারি চার্জ: ৳$takenDC

                💳 সর্বমোট: ৳$total

                ⏰ ডেলিভারি টাইম: ২–৩ কর্মদিবসের মধ্যে। 
                আপনার অর্ডারটি সফলভাবে কনফার্ম করা হয়েছে। কোনো সমস্যা হলে ইনবক্সে জানাবেন।  

                — Nestify- এর সাথেই থাকুন, ধন্যবাদ 🥰
            """.trimIndent()

            val clip = ClipData.newPlainText("order_info", copyText)
            clipboard.setPrimaryClip(clip)
        }


        val adapter = ArrayAdapter(this, R.layout.simple_list_item_1, Data.deliveryStatus2)
        binding.spinnerStatus.setAdapter(adapter)


        with(binding) {
            btnUpdate.setOnClickListener {
                etPID.isEmpty()
                spinnerStatus.isEmpty()
                etDelivaryCharge.isEmpty()
                etAdsCost.isEmpty()
                if (!etPID.isEmpty() && !spinnerStatus.isEmpty() && !etDelivaryCharge.isEmpty() && !etAdsCost.isEmpty()){

                    val newPID = binding.etPID.extract()
                    val newStatus = binding.spinnerStatus.extract()
                    val newDeliveryCharge = binding.etDelivaryCharge.extract()
                    val newAdsCost = binding.etAdsCost.extract()

                    //profit & % update
                    val updateDeliveryCharge = binding.etDelivaryCharge.extract().toDoubleOrNull() ?: 0.0
                    val updateAdsCostValue = binding.etAdsCost.extract().toDoubleOrNull() ?: 0.0

                    val updateTotalCost = buyPrice + updateDeliveryCharge + packagingCost + updateAdsCostValue

                    val updateNetProfit = totalSell - updateTotalCost

                    val updateProfitPercent = if (updateTotalCost != 0.0) (updateNetProfit / updateTotalCost) * 100 else 0.0

                    val newNetProfit = updateNetProfit.toString()
                    val newProfitPercent = updateProfitPercent.toString()

                    viewModel.updateOrder(docId, newPID, newStatus,newDeliveryCharge,newAdsCost, newNetProfit, newProfitPercent)
                }
            }
        }

        viewModel.updateOrderResponse.observe(this) { state ->
            when (state) {
                is DataState.Loading -> {
                }
                is DataState.Success -> {
                    Toast.makeText(this, "Updated Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                is DataState.Error -> {
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
