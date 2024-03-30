package com.example.fitplan.UI.fragments.sign_out

import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.service.autofill.VisibilitySetterAction
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.fitplan.R
import com.example.fitplan.databinding.FragmentSignOutBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import il.co.syntax.firebasemvvm.repository.FirebaseImpl.AuthRepositoryFirebase


class SignOutFragment : DialogFragment() {
    private var _binding: FragmentSignOutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignOutViewModel by viewModels {
        SignOutViewModel.ProfileViewModelFactory(AuthRepositoryFirebase())
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSignOutBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttonNavigation =  requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        buttonNavigation.visibility = View.VISIBLE
        binding.yesBtn.setOnClickListener {
            binding.messageTv.text = "Signing Out , Don't Forget : \"NO PAIN NO GAIN!\""
            binding.noBtn.visibility  = View.GONE
            binding.yesBtn.visibility = View.GONE

            Handler().postDelayed({
                viewModel.signOut()
                dismiss()
                findNavController().navigate(R.id.loginFragment)
            }, 3000)
        }

        binding.noBtn.setOnClickListener {
            findNavController().navigate(R.id.myWorkoutFragment)
            buttonNavigation.selectedItemId = R.id.myWorkout_btn
            dismiss()
        }




    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}