package com.example.fitplan.UI.login_register

import android.opengl.Visibility
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.fitplan.R
import com.example.fitplan.databinding.ActivityMainBinding
import com.example.fitplan.databinding.FragmentLoginBinding
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import il.co.syntax.firebasemvvm.repository.FirebaseImpl.AuthRepositoryFirebase
import il.co.syntax.myapplication.util.Resource

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val _mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val mAuth get() = _mAuth

    private val viewModel : LoginViewModel by viewModels{
        LoginViewModel.LoginViewModelFactory(AuthRepositoryFirebase())
    }




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        binding.registerBtn.setOnClickListener {
            val navController = NavHostFragment.findNavController(this)
            navController.navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.passwordTextInput.isHelperTextEnabled = false
        binding.loginBtn.setOnClickListener {
            viewModel.signInUser(binding.emailTextInput.editText?.text.toString(),binding.passwordTextInput.editText?.text.toString())
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility = View.GONE

        viewModel.userSignInStatus.observe(viewLifecycleOwner){
            when(it){
                is Resource.Loading -> {
                    binding.loginProgress.isVisible = true
                    binding.registerBtn.isEnabled = false
                }
                is Resource.Success -> {
                    Toast.makeText(requireContext(),"Logged-in Successfuly", Toast.LENGTH_LONG).show()
                    findNavController().navigate(R.id.action_loginFragment_to_myWorkoutFragment)
                }

                is Resource.Error -> {
                    binding.loginProgress.isVisible = false
                    binding.registerBtn.isEnabled = true
                    binding.passwordTextInput.isHelperTextEnabled = true
                    binding.passwordTextInput.helperText = it.message
                }
            }
        }
        viewModel.currentUser.observe(viewLifecycleOwner){
            when(it){
                is Resource.Loading -> {
                    binding.loginProgress.isVisible = true
                    binding.registerBtn.isEnabled = false
                }
                is Resource.Success -> {
                    findNavController().navigate(R.id.action_loginFragment_to_myWorkoutFragment)
                }

                is Resource.Error -> {
                    binding.loginProgress.isVisible = false
                    binding.registerBtn.isEnabled = true
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun LoginFunc() {
        val email = binding.emailTextInput.editText?.text.toString().trim()
        val password = binding.passwordTextInput.editText?.text.toString().trim()
        val navController = NavHostFragment.findNavController(this)

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            binding.passwordTextInput.isHelperTextEnabled = true
            binding.passwordTextInput.helperText = "Please enter both email and password"
            return
        }

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(requireContext(), "Successful Login", Toast.LENGTH_LONG).show()
                    val user = mAuth.currentUser
                    if (user != null) {
                        navController.navigate(R.id.action_loginFragment_to_myWorkoutFragment)
                    } else {
                        binding.passwordTextInput.isHelperTextEnabled = true
                        binding.passwordTextInput.helperText ="User does not exist"
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    binding.passwordTextInput.isHelperTextEnabled = true
                    binding.passwordTextInput.helperText =  "Login failed. Please check your credentials."
                }
            }
    }
}