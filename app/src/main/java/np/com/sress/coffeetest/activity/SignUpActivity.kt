package np.com.sress.coffeetest.activity

import android.content.ContentValues
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
import np.com.sress.coffeetest.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding
    private lateinit var dbHelper: InternalDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dbHelper = InternalDatabase(this)

        binding.signUpBtn.setOnClickListener {
            val fullName = binding.fullName.text.toString().trim()
            val phoneNumber = binding.phoneNumber.text.toString().trim()
            val password = binding.password.text.toString().trim()
            val confirmPassword = binding.confirmPassword.text.toString().trim()

            if (fullName.isEmpty()) {
                binding.fullName.requestFocus()
                binding.etFullNameLayout.error = "Full Name is required"
                return@setOnClickListener
            } else {
                binding.etFullNameLayout.error = null
            }

            if (phoneNumber.isEmpty()) {
                binding.phoneNumber.requestFocus()
                binding.etUsernameLayout.error = "Phone Number is required"
                return@setOnClickListener
            } else {
                binding.etUsernameLayout.error = null
            }

            if (password.isEmpty()) {
                binding.password.requestFocus()
                binding.etPasswordLayout.error = "Password is required"
                return@setOnClickListener
            } else {
                binding.etPasswordLayout.error = null
            }

            if (confirmPassword.isEmpty()) {
                binding.confirmPassword.requestFocus()
                binding.etConfirmPasswordLayout.error = "Confirm Password is required"
                return@setOnClickListener
            } else {
                binding.etConfirmPasswordLayout.error = null
            }

            if (password != confirmPassword) {
                binding.confirmPassword.requestFocus()
                binding.etConfirmPasswordLayout.error = "Passwords do not match"
                return@setOnClickListener
            } else {
                binding.etConfirmPasswordLayout.error = null
            }

            binding.signUpProgress.visibility = View.VISIBLE
            binding.signUpBtn.isEnabled = false
            binding.signUpBtn.text = "Signing up..."

            Handler(Looper.getMainLooper()).postDelayed({
                saveDetails(fullName, phoneNumber, password)

                binding.signUpProgress.visibility = View.GONE
                binding.signUpBtn.isEnabled = true
                binding.signUpBtn.text = "Sign Up"

                startActivity(Intent(this, SignInActivity::class.java))
                finish()
            }, 1500)
        }

        binding.tvSignIn.setOnClickListener {
            startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
            finish()
        }
    }

    private fun saveDetails(fullName: String, phoneNumber: String, password: String) {
        val db = dbHelper.writableDatabase
        val cv = ContentValues()
        cv.put("fullName", fullName)
        cv.put("phoneNumber", phoneNumber)
        cv.put("password", password)

        val result = db.insert(InternalDatabase.TABLE_USER, null, cv)
        if (result == -1L) {
            Toast.makeText(this, "Failed to save user", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
        }
    }
}
