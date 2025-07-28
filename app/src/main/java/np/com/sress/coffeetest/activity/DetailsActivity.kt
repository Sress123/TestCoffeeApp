package np.com.sress.coffeetest.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import np.com.sress.coffeetest.R
import np.com.sress.coffeetest.databinding.ActivityDetailsBinding
import np.com.sress.coffeetest.helper.ManagementCart
import np.com.sress.coffeetest.model.ItemsModel
import np.com.sress.coffeetest.model.PopularModel

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private var itemPopular: PopularModel? = null
    private var itemRegular: ItemsModel? = null
    private lateinit var managementCart: ManagementCart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managementCart = ManagementCart(this)

        handleIntentObject()
        initSizeList()
    }

    private fun handleIntentObject() {
        val obj = intent.getSerializableExtra("object")

        when (obj) {
            is PopularModel -> {
                itemPopular = obj
                showPopularData()
            }

            is ItemsModel -> {
                itemRegular = obj
                showItemsData()
            }
            else -> finish()
        }
    }

    private fun showPopularData() {
        itemPopular?.let { item ->
            Glide.with(this)
                .load(item.picUrl)
                .into(binding.picMain)

            binding.titleText.text = item.title
            binding.descriptionTxt.text = item.description
            binding.priceText.text = "$${item.price}"
            binding.ratingTxt.text = item.rating.toString()
            binding.numberItemTxt.text = item.numberInCart.toString()

            binding.addToCartBtn.setOnClickListener {
                item.numberInCart = binding.numberItemTxt.text.toString().toInt()
                val convertedItem = ItemsModel().apply {
                    id = item.id
                    title = item.title
                    description = item.description
                    extra = item.extra
                    picUrl = listOf(item.picUrl)
                    price = item.price
                    rating = item.rating
                    numberInCart = item.numberInCart
                }
                managementCart.insertItems(convertedItem)
            }

            binding.plusCart.setOnClickListener {
                item.numberInCart++
                binding.numberItemTxt.text = item.numberInCart.toString()
            }

            binding.minusCart.setOnClickListener {
                if (item.numberInCart > 0) {
                    item.numberInCart--
                    binding.numberItemTxt.text = item.numberInCart.toString()
                }
            }
        }

        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun showItemsData() {
        itemRegular?.let { item ->
            Glide.with(this)
                .load(item.picUrl.firstOrNull())
                .into(binding.picMain)

            binding.titleText.text = item.title
            binding.descriptionTxt.text = item.description
            binding.priceText.text = "$${item.price}"
            binding.ratingTxt.text = item.rating.toString()
            binding.numberItemTxt.text = item.numberInCart.toString()

            binding.addToCartBtn.setOnClickListener {
                item.numberInCart = binding.numberItemTxt.text.toString().toInt()
                managementCart.insertItems(item)
            }

            binding.plusCart.setOnClickListener {
                item.numberInCart++
                binding.numberItemTxt.text = item.numberInCart.toString()
            }

            binding.minusCart.setOnClickListener {
                if (item.numberInCart > 0) {
                    item.numberInCart--
                    binding.numberItemTxt.text = item.numberInCart.toString()
                }
            }
        }

        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun initSizeList() {
        binding.apply {
            smallBtn.setOnClickListener {
                smallBtn.setBackgroundResource(R.drawable.stroke_brown_bg)
                mediumBtn.setBackgroundResource(0)
                largeBtn.setBackgroundResource(0)
            }
            mediumBtn.setOnClickListener {
                smallBtn.setBackgroundResource(0)
                mediumBtn.setBackgroundResource(R.drawable.stroke_brown_bg)
                largeBtn.setBackgroundResource(0)
            }
            largeBtn.setOnClickListener {
                smallBtn.setBackgroundResource(0)
                mediumBtn.setBackgroundResource(0)
                largeBtn.setBackgroundResource(R.drawable.stroke_brown_bg)
            }
        }
    }
}