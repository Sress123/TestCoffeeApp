package np.com.sress.coffeetest.activity

import android.os.Bundle
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import np.com.sress.coffeetest.R
import np.com.sress.coffeetest.adapter.CartAdapter
import np.com.sress.coffeetest.databinding.ActivityCartBinding
import np.com.sress.coffeetest.helper.ChangeNumberItemsListener
import np.com.sress.coffeetest.helper.ManagementCart

class CartActivity : AppCompatActivity() {
    lateinit var binding: ActivityCartBinding
    lateinit var managementCart: ManagementCart
    private var tax: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managementCart = ManagementCart(this)

        calculateCart()
        setVeriable( )
        initCartList()
    }

    private fun initCartList() {
        binding.apply {
            cartView.layoutManager = LinearLayoutManager(this@CartActivity, LinearLayoutManager. VERTICAL, false)
            cartView.adapter = CartAdapter (
                managementCart.getListCart(),
                this@CartActivity,
                object :ChangeNumberItemsListener {
                    override fun onChanged() {
                        calculateCart()
                    }
                }
            )
        }
    }

    private fun setVeriable() {
        binding.backBtn.setOnClickListener { finish() }
    }

    private fun calculateCart() {
        val percentTax = 0.02
        val delivery = 15
        tax = Math.round((managementCart.getTotalFee()*percentTax)*100)/100.0
        val total = Math.round((managementCart.getTotalFee()+tax+delivery)*100)/100
        val itemTotal = Math.round(managementCart.getTotalFee()*100)/100
        binding.apply {
            totalFeeTxt.text = "$$itemTotal"
            taxTxt.text = "$$tax"
            deliveryTxt.text = "$$delivery"
            totalTxt.text = "$${total}"
        }
    }
}