package np.com.sress.coffeetest.model
import java.io.Serializable

class PopularModel : Serializable {
    var id: Int = 0
    var title: String = ""
    var numberInCart: Int = 0
    var description: String = ""
    var extra: String = ""
    var picUrl: String = ""
    var price: Double = 0.0
    var rating: Double = 0.0
}
