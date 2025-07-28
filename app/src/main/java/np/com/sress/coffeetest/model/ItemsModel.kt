package np.com.sress.coffeetest.model

import java.io.Serializable

class ItemsModel : Serializable {
    var id: Int = 0
    var categoryId: Int = 0
    var numberInCart: Int = 0
    var description: String = ""
    var extra: String = ""
    var picUrl: List<String> = listOf()
    var price: Double = 0.0
    var rating: Double = 0.0
    var title: String = ""
}