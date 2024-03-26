package com.example.fitplan.UI.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.fitplan.R
import com.example.fitplan.databinding.FragmentLoginBinding
import com.example.fitplan.databinding.FragmentProfileBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import il.co.syntax.firebasemvvm.repository.FirebaseImpl.AuthRepositoryFirebase


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel : ProfileViewModel by viewModels{
        ProfileViewModel.ProfileViewModelFactory(AuthRepositoryFirebase())
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.signOutBtn.setOnClickListener {
            viewModel.signOut()
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)

            binding.changeUserNameBtn.setOnClickListener {
                val dialogBuilder = AlertDialog.Builder(requireContext())
                val dialogView: View = layoutInflater.inflate(R.layout.change_username_card_layout, null)
                val newName = dialogView.findViewById<TextInputEditText>(R.id.newName_text)
                val cancelBtn = dialogView.findViewById<Button>(R.id.cancel_btn)
                val saveBtn = dialogView.findViewById<Button>(R.id.save_btn)
                saveBtn.setOnClickListener {
                    viewModel.setUsername(newName.text.toString())
                }
            }

        }




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility = View.VISIBLE

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}