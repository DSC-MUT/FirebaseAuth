package tech.danielwaiguru.dscfirebaseauth.ui.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import tech.danielwaiguru.dscfirebaseauth.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding get() = _binding!!
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
    }
    private fun setupListeners() {
        with(binding) {
            navToRegister.setOnClickListener { navToRegistrationUi() }
            forgotPasswordTxt.setOnClickListener { navToResetPassword() }
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