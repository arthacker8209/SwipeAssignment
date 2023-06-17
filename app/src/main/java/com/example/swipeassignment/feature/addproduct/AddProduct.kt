package com.example.swipeassignment.feature.addproduct

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.example.swipeassignment.R
import com.example.swipeassignment.common.ViewState
import com.example.swipeassignment.databinding.FragmentAddProductBinding
import com.example.swipeassignment.feature.product.ProductFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileNotFoundException

@AndroidEntryPoint
class AddProduct : Fragment() {

    companion object {
        const val TAG = "AddProduct"
        fun newInstance() = AddProduct()
    }
    private val permissionRequestCode = 123
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedImageUri = result.data?.data
                selectedImageUri?.let {
                    try {
                        val inputStream =
                            requireContext().contentResolver.openInputStream(selectedImageUri)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        binding.imageLayout.visibility = View.VISIBLE
                        binding.ivProduct.setImageBitmap(bitmap)
                        setImageFileList(selectedImageUri)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                }
            }
        }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickImageLauncher.launch(intent)
    }

    @SuppressLint("Range")
    private fun setImageFileList(selectedImageUri: Uri) {
        val imageFile: File? = selectedImageUri.let { uri ->
            val imagePath =
                context?.contentResolver?.query(uri, null, null, null, null)?.use { cursor ->
                    cursor.moveToFirst()
                    cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                }
            imagePath?.let { File(it) }
        }
        viewModel.imageFilesLiveData.value = listOf(imageFile!!)
    }

    private lateinit var binding: FragmentAddProductBinding
    private lateinit var viewModel: AddProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddProductBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[AddProductViewModel::class.java]
        setupDropDownList()
        setupUI()
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.errorMessageLiveData.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_SHORT).show()
                viewModel.errorMessageLiveData.value = null
            }
        }
        viewModel.productLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Error -> {
                }

                ViewState.Loading -> {}
                is ViewState.Success -> {
                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                    Handler().postDelayed(Runnable {
                        val fragment = ProductFragment.newInstance()
                        val fragmentManager = requireActivity().supportFragmentManager
                        val fragmentTransaction = fragmentManager.beginTransaction()
                        fragmentTransaction.replace(R.id.fvMain, fragment)
                        fragmentTransaction.commit()
                    }, 1000)
                }
            }
        }
    }

    private fun setupDropDownList() {
        val productTypes = arrayListOf("Type1", "Type2", "Type3")
        val adapter =
            ArrayAdapter(requireContext(), R.layout.producttype_dropdown_layout, productTypes)
        val autoCompleteTextView = binding.productTypeAutoComplete
        autoCompleteTextView.setAdapter(adapter)
    }

    private fun setupUI() {
        binding.titlebar.tickImageView.setOnClickListener {
            val productName = binding.etProductName.text.toString()
            val productType = binding.productTypeAutoComplete.text.toString()
            val productPrice = binding.etProductPrice.text.toString()
            val productTax = binding.etProductTax.text.toString()
            viewModel.onSubmitClicked(
                productName = productName,
                productType = productType,
                price = productPrice,
                tax = productTax
            )
        }
        binding.btnUpload.setOnClickListener {
            checkAndRequestPermissions()
        }
        binding.cvDeleteImage.setOnClickListener {
            viewModel.imageFilesLiveData.value = listOf()
            binding.imageLayout.visibility = View.GONE
        }

        binding.titlebar.backLayout.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun checkAndRequestPermissions() {
        val readStoragePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.READ_MEDIA_IMAGES
        } else {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        }
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                readStoragePermission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(readStoragePermission),
                permissionRequestCode
            )
        } else {
            // Permission is already granted
            openImagePicker()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionRequestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                openImagePicker()
            } else {
               Toast.makeText(requireContext(),"Permission Denied",Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }
}