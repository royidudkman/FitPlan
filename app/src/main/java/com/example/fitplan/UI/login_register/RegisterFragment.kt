package com.example.fitplan.UI.login_register

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import com.example.fitplan.R
import com.example.fitplan.databinding.FragmentRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val _mAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val mAuth get() = _mAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        binding.passwordTextInput.isHelperTextEnabled = false
        binding.registerBtn.setOnClickListener{
            RegisterFunc()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility = View.GONE
    }

    fun RegisterFunc() {
        val email = binding.emailTextInput.editText?.text.toString().trim()
        val password = binding.passwordTextInput.editText?.text.toString().trim()

        val navController = NavHostFragment.findNavController(this)

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            binding.passwordTextInput.isHelperTextEnabled = true
            binding.passwordTextInput.helperText = "Please enter both email and password"
            return
        }

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity(),
                OnCompleteListener<AuthResult?> { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(requireContext(),"Registered Successfuly", Toast.LENGTH_LONG).show()
                        val user : FirebaseUser? = mAuth.currentUser
                        //TODO Write data to Database
                        //writeUserDataToDatabase(user.uid, name, email, phone)
                        navController.navigate(R.id.action_registerFragment_to_loginFragment)
                    } else {
                        // If sign in fails, display a message to the user.
                        binding.passwordTextInput.isHelperTextEnabled = true
                        binding.passwordTextInput.helperText =  "Register failed. Email already taken"
                    }
                })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}