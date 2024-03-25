package com.example.fitplan.UI.login_register

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.fitplan.R
import com.example.fitplan.databinding.FragmentRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import il.co.syntax.firebasemvvm.repository.FirebaseImpl.AuthRepositoryFirebase
import il.co.syntax.myapplication.util.Resource

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val _mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val mAuth get() = _mAuth

    private val viewModel : RegisterViewModel by viewModels() {
        RegisterViewModel.RegisterViewModelFactory(AuthRepositoryFirebase())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        binding.passwordTextInput.isHelperTextEnabled = false
        binding.registerBtn.setOnClickListener{
            viewModel.createUser(binding.emailTextInput.editText?.text.toString(),binding.passwordTextInput.editText?.text.toString())
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility = View.GONE

        viewModel.userRegistrationStatus.observe(viewLifecycleOwner){
            when(it){
                is Resource.Loading -> {
                    binding.registerProgress.isVisible = true
                    binding.registerBtn.isEnabled = false
                }
                is Resource.Success -> {
                    Toast.makeText(requireContext(),"Registered Successfuly", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_registerFragment_to_myWorkoutFragment)
                }

                is Resource.Error -> {
                    binding.registerProgress.isVisible = false
                    binding.registerBtn.isEnabled = true
                    binding.passwordTextInput.helperText = it.message
                }
            }
        }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}