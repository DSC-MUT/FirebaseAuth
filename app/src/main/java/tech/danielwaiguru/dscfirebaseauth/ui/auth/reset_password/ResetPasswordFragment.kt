package tech.danielwaiguru.dscfirebaseauth.ui.auth.reset_password

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import cn.pedant.SweetAlert.SweetAlertDialog
import tech.danielwaiguru.dscfirebaseauth.R
import tech.danielwaiguru.dscfirebaseauth.data.repository.AuthRepoImpl
import tech.danielwaiguru.dscfirebaseauth.databinding.FragmentResetPasswordBinding
import tech.danielwaiguru.dscfirebaseauth.extensions.showSnackBar
import tech.danielwaiguru.dscfirebaseauth.ui.view_model.AuthViewModel
import tech.danielwaiguru.dscfirebaseauth.ui.view_model.AuthViewModelFactory
import tech.danielwaiguru.dscfirebaseauth.util.ResultWrapper

class ResetPasswordFragment : Fragment() {
    private var _binding: FragmentResetPasswordBinding? = null
    private val binding: FragmentResetPasswordBinding get() = _binding!!
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
        _binding = FragmentResetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        liveDataObservers()
        setupListeners()
    }

    private fun setupListeners() {
        binding.submitBtn.setOnClickListener { sendResetLink() }
    }

    private fun sendResetLink() {
        authViewModel.sendResetPasswordLink(binding.email.text.toString())
    }

    private fun liveDataObservers() {
        authViewModel.resetLinkStatus.observe(viewLifecycleOwner, { result ->
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
                    showSnackBar(getString(R.string.link_success))
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}