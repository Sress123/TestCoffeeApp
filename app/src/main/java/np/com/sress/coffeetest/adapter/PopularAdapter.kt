package np.com.sress.coffeetest.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import np.com.sress.coffeetest.activity.DetailsActivity
import np.com.sress.coffeetest.databinding.ViewHolderPopularBinding
import np.com.sress.coffeetest.model.PopularModel

class PopularAdapter(val items: MutableList<PopularModel>) :
RecyclerView.Adapter<PopularAdapter.Viewholder>(){
    lateinit var context: Context

    class Viewholder(val  binding: ViewHolderPopularBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularAdapter.Viewholder {
        context = parent.context
        val binding = ViewHolderPopularBinding.inflate(LayoutInflater.from(context), parent, false)
        return Viewholder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: Viewholder, position: Int) {
        holder.binding.titleTxt.text = items[position].title
        holder.binding.priceTxt.text = "$" + items[position].price.toString()
        Glide.with(context)
            .load(items[position].picUrl)
            .into(holder.binding.pic)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailsActivity::class.java)
            intent.putExtra("object", items[position])
            context.startActivity(intent)
        }
    }
}