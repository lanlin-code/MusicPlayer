package com.example.music.autoLogin

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.music.AutoLoginCallback
import com.example.music.LoginCallback
import com.example.music.R
import com.example.music.ResponseCallback
import com.example.music.entity.User
import com.example.music.login.LoginPresenter
import com.squareup.picasso.Picasso

class AutoLoginFragment: Fragment(), ResponseCallback<User> {

    private var listener: AutoLoginCallback? = null
    private var presenter: LoginPresenter = LoginPresenter()
    private var model: AutoLoginModel = AutoLoginModel()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AutoLoginCallback) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_auto_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.listener = this
        model.autoLogin(presenter)
    }

    override fun onSuccess(data: User) {
        listener?.onAutoLoginSuccess(data)
    }

    override fun onError(message: String) {
        listener?.onAutoLoginFail(message)
    }
}