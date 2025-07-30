package np.com.sress.coffeetest.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import np.com.sress.coffeetest.R
import np.com.sress.coffeetest.database.InternalDatabase
import np.com.sress.coffeetest.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {

    lateinit var binding : ActivitySignInBinding
    private lateinit var dbHelper: InternalDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbHelper = InternalDatabase(this)

        binding.signInBtn.setOnClickListener {
            val userName = binding.siPhoneNumber.text.toString().trim()
            val password = binding.siPassword.text.toString().trim()

            if (userName.isEmpty()) {
                binding.siPhoneNumber.requestFocus()
                binding.etUsernameLayout.error = "Phone Number is required"
                return@setOnClickListener
            } else {
                binding.etUsernameLayout.error = null
            }

            if (password.isEmpty()) {
                binding.siPassword.requestFocus()
                binding.etPasswordLayout.error = "Password is required"
                return@setOnClickListener
            } else {
                binding.etPasswordLayout.error = null
            }

            binding.signInProgress.visibility = View.VISIBLE
            binding.signInBtn.isEnabled = false
            binding.signInBtn.text = "Singning in..."

            Handler(Looper.getMainLooper()).postDelayed({
                logIn(userName, password)
                binding.signInProgress.visibility = View.GONE
                binding.signInBtn.isEnabled = true
                binding.signInBtn.text = "Sign In"
            }, 1500)
        }

        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
            finish()
        }
    }

    private fun logIn(userName: String, password: String) {
        val prefs = getSharedPreferences("init_prefs", MODE_PRIVATE)
        val users = dbHelper.getUserFromDatabase(InternalDatabase.TABLE_USER)
        val matchedUser = users.find { it.phoneNumber == userName && it.password == password }
        if (matchedUser != null) {
            prefs.edit().putBoolean("is_user_logged_in", true).apply()
            Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } else {
            Toast.makeText(this, "Invalid phone number or password", Toast.LENGTH_SHORT).show()
        }
    }
}