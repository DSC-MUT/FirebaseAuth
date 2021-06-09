package tech.danielwaiguru.dscfirebaseauth.ui.auth.login

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
import tech.danielwaiguru.dscfirebaseauth.data.repository.AuthRepoImpl
import tech.danielwaiguru.dscfirebaseauth.databinding.FragmentLoginBinding
import tech.danielwaiguru.dscfirebaseauth.extensions.showSnackBar
import tech.danielwaiguru.dscfirebaseauth.ui.view_model.AuthViewModel
import tech.danielwaiguru.dscfirebaseauth.ui.view_model.AuthViewModelFactory
import tech.danielwaiguru.dscfirebaseauth.util.ResultWrapper

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding get() = _binding!!
    private val authViewModel: AuthViewModel by viewModels { AuthViewModelFactory(AuthRepoImpl()) }
    private val sweetAlertDialog: SweetAlertDialog by lazy {
        SweetAlertDialog(requireContext(), SweetAlertDialog.PROGRESS_TYPE).apply {
            titleText = getString(R.string.authenticating)
            progressHelper?.barColor = Color.parseColor("#863B96")
            setCancelable(false)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        liveDataObservers()
    }

    private fun liveDataObservers() {
        authViewModel.loginStatus.observe(viewLifecycleOwner, { result ->
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
                    showSnackBar(getString(R.string.login_sucess))
                    navToDashboard()
                }
            }
        })
    }

    private fun navToDashboard() {
        findNavController().navigate(
            LoginFragmentDirections.actionLoginFragmentToDashBoardFragment()
        )
    }

    private fun setupListeners() {
        with(binding) {
            navToRegister.setOnClickListener { navToRegistrationUi() }
            forgotPasswordTxt.setOnClickListener { navToResetPassword() }
            loginBtn.setOnClickListener { loginUser() }
        }
    }

    private fun loginUser() {
        with(binding) {
            authViewModel.signIn(userEmail.text.toString(), password.text.toString())
        }
    }

    private fun navToResetPassword() {
        findNavController().navigate(
            LoginFragmentDirections.actionLoginFragmentToResetPasswordFragment()
        )
    }

    private fun navToRegistrationUi() {
        findNavController().navigate(
            LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}