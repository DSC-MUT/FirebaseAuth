package tech.danielwaiguru.dscfirebaseauth.ui.auth.register

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import cn.pedant.SweetAlert.SweetAlertDialog
import tech.danielwaiguru.dscfirebaseauth.R
import tech.danielwaiguru.dscfirebaseauth.data.model.User
import tech.danielwaiguru.dscfirebaseauth.data.repository.AuthRepoImpl
import tech.danielwaiguru.dscfirebaseauth.databinding.FragmentRegisterBinding
import tech.danielwaiguru.dscfirebaseauth.extensions.showSnackBar
import tech.danielwaiguru.dscfirebaseauth.ui.view_model.AuthViewModel
import tech.danielwaiguru.dscfirebaseauth.ui.view_model.AuthViewModelFactory
import tech.danielwaiguru.dscfirebaseauth.util.ResultWrapper

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding: FragmentRegisterBinding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels { AuthViewModelFactory(AuthRepoImpl()) }
    private val sweetAlertDialog: SweetAlertDialog by lazy {
        SweetAlertDialog(requireContext(), SweetAlertDialog.PROGRESS_TYPE).apply {
            titleText = getString(R.string.creating)
            progressHelper?.barColor = Color.parseColor("#863B96")
            setCancelable(false)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        liveDataObservers()
    }

    private fun initListeners() {
        with(binding){
            navToLogin.setOnClickListener { navToLoginUi() }
            registerBtn.setOnClickListener { registerUser() }
        }
    }

    private fun registerUser() {
        with(binding) {
            val user = User(
                firstname.text.toString(),
                lastname.text.toString(),
                userEmail.text.toString(),
                password.text.toString(),
                cPassword.text.toString()
            )
            authViewModel.signUp(user)
        }
    }

    private fun liveDataObservers() {
        authViewModel.registerStatus.observe(viewLifecycleOwner, { result ->
            when (result) {
                is ResultWrapper.Loading -> {
                    sweetAlertDialog.show()
                }
                is ResultWrapper.Error -> {
                    sweetAlertDialog.dismiss()
                    showSnackBar(result.message.toString())
                }
                is ResultWrapper.Success -> {
                    sweetAlertDialog.dismiss()
                    showSnackBar(getString(R.string.register_sucess))
                    navToLoginUi()
                }
            }
        })
    }

    private fun navToLoginUi() {
        findNavController().navigate(
            RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}