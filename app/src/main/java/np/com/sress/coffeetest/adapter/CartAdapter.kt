package np.com.sress.coffeetest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import np.com.sress.coffeetest.databinding.ViewholderCartBinding
import np.com.sress.coffeetest.helper.ChangeNumberItemsListener
import np.com.sress.coffeetest.helper.ManagementCart
import np.com.sress.coffeetest.model.ItemsModel

class CartAdapter(
    private val listItemSelected: ArrayList<ItemsModel>,
    context: Context,
    var changeNumberItemsListener: ChangeNumberItemsListener? = null
): RecyclerView.Adapter<CartAdapter.Viewholder>() {

    class Viewholder (val binding: ViewholderCartBinding): RecyclerView.ViewHolder(binding.root)
    private val managementCart = ManagementCart(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartAdapter.Viewholder {
        val binding = ViewholderCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: CartAdapter.Viewholder, position: Int) {
        val items = listItemSelected[position]

        holder.binding.titleTxt.text = items.title
        holder.binding.feeEachItem.text = "$${items.price}"
        holder.binding.totalEachItem.text = "$${Math.round(items.numberInCart * items.price)}"
        holder.binding.numberItemTxt.text = items.numberInCart.toString()

        Glide.with(holder.itemView.context)
            .load(items.picUrl[0])
            .apply(RequestOptions().transform(CenterCrop()))
            .into(holder.binding.picArt)

        holder.binding.plusEachItem.setOnClickListener {
            managementCart.plusItem(listItemSelected, position, object : ChangeNumberItemsListener {
                override fun onChanged() {
                    notifyDataSetChanged()
                    changeNumberItemsListener?.onChanged()
                }
            })
        }

        holder.binding.minusEachItem.setOnClickListener {
            managementCart.minusItem(listItemSelected, position, object : ChangeNumberItemsListener {
                override fun onChanged() {
                    notifyDataSetChanged()
                    changeNumberItemsListener?.onChanged()
                }
            })
        }

        holder.binding.removeItemBtn.setOnClickListener {
            managementCart.romveItem(listItemSelected, position, object : ChangeNumberItemsListener {
                override fun onChanged() {
                    notifyDataSetChanged()
                    changeNumberItemsListener?.onChanged()
                }
            })
        }
    }

    override fun getItemCount(): Int = listItemSelected.size
}