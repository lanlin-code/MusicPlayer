package com.example.music.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.music.*
import com.example.music.entity.User


class LoginFragment : BaseFragment(), ResponseCallback<User> {

    private var listener: LoginCallback? = null
    private var phone: EditText? = null
    private var password: EditText? = null
    private var loading = false
//    private var fragmentChangeListener: FragmentChangeListener? = null

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
        b.setOnClickListener {
            if (!loading) {
                loading = true
                model.login(presenter, phone?.text.toString(), password?.text.toString())
            }

        }
        val back = view.findViewById<ImageButton>(R.id.login_back_button)
        back.setOnClickListener { fragmentChangeListener?.onBackHome() }
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


    override fun onSuccess(data: User) {
        loading = false
        listener?.onLoginSuccess(data)
    }

    override fun onError(message: String) {
        loading = false
        listener?.onLoginFail(message)
    }


}