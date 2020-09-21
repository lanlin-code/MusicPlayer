package com.example.music.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.music.R
import com.example.music.entity.User


class LoginFragment : Fragment(), LoginPresenter.LoginListener {

    private var listener: LoginCallback? = null
    private var phone: EditText? = null
    private var password: EditText? = null

    private val presenter = LoginPresenter()
    private val model = LoginModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        phone = view.findViewById(R.id.phone_number)
        password = view.findViewById(R.id.password_text)
        presenter.listener = this
        val b = view.findViewById<Button>(R.id.login_button)
        b.setOnClickListener { model.login(presenter) }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LoginCallback) {
            listener = context
        }

    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun getUsername(): String? {
        return phone?.text.toString()
    }

    override fun getPassword(): String? {
        return password?.text.toString()
    }

    override fun onSuccess(data: User) {
        listener?.onLoginSuccess(data)
    }

    override fun onError(message: String) {
        listener?.onLoginFail(message)
    }

    interface LoginCallback {
        fun onLoginSuccess(data: User)
        fun onLoginFail(message: String)
    }
}