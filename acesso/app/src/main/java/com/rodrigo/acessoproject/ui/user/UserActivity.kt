package com.rodrigo.acessoproject.ui.user

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.rodrigo.acessoproject.R
import com.rodrigo.acessoproject.data.model.User
import kotlinx.android.synthetic.main.activity_user.*
import kotlinx.android.synthetic.main.content_user.*
import android.view.WindowManager
import android.os.Build
import com.bumptech.glide.Glide


class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        configureView()

        if (intent.hasExtra("user")) {
            intent.extras.getSerializable("user")?.let { user ->
                inflateUser(user as User)
            }
        }

        setListeners()

    }

    /**
     * Methods to set listeners in views.
     */
    private fun setListeners() {
        user_exit.setOnClickListener {
            onBackPressed()
        }
    }

    /**
     * Method to configure status bar view.
     */
    private fun configureView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
    }

    /**
     * Method to inflate user.
     *
     * @param user the instance of user.
     */
    private fun inflateUser(user: User) {
        user_name.text = if (user.name.isNotEmpty()) user.name else getString(R.string.user_no_name)

        user_email.text = if (user.email.isNotEmpty()) user.email else getString(R.string.activity_no_email)

        user_phone.text = if (user.phone.isNotEmpty()) user.phone else getString(R.string.activity_no_phone)

        user_address.text = if (user.address.isNotEmpty()) user.address else getString(R.string.activity_no_address)

        Glide.with(this).load(user.photo).into(user_image)
    }

}
