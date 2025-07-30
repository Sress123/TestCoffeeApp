package np.com.sress.coffeetest.model
import java.io.Serializable

class UserModel: Serializable {
    var id: Int = 0
    var fullName: String = ""
    var phoneNumber: String = ""
    var password: String = ""
}