package com.ubb.david.playlistcreator.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ubb.david.playlistcreator.data.AuthenticationResult
import com.ubb.david.playlistcreator.data.Authenticator
import com.ubb.david.playlistcreator.R
import com.ubb.david.playlistcreator.StartupFragmentDirections

class StartupFragment : Fragment(R.layout.fragment_startup) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (Authenticator.user == null) {
            Authenticator.authenticate(this)
        } else {
            findNavController().navigate(StartupFragmentDirections.actionToMainScreen())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Authenticator.handleAuthenticationResult(requestCode, resultCode, data)?.let { authResult ->
            when (authResult) {
                AuthenticationResult.Success -> {
                    Toast.makeText(requireContext(), "Authentication successful!", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(StartupFragmentDirections.actionToMainScreen())
                }
                AuthenticationResult.Failure.UserCancelled -> {
                    Toast.makeText(requireContext(), "Authentication failed, user cancelled login.", Toast.LENGTH_SHORT).show()
                    requireActivity().finish()
                }
                is AuthenticationResult.Failure.Error -> {
                    Toast.makeText(
                        requireContext(),
                        "Authentication failed, error cause: ${authResult.errorCode}, ${authResult.errorMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                    requireActivity().finish()
                }
            }
        }
    }
}