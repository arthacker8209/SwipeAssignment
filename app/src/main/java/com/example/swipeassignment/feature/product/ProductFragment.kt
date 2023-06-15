package com.example.swipeassignment.feature.product

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swipeassignment.common.ViewState
import com.example.swipeassignment.databinding.FragmentProductBinding
import com.example.swipeassignment.network.models.ProductListing
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductFragment : Fragment() {

    companion object {
        const val TAG = "ProductFragment"
        fun newInstance() = ProductFragment()
    }
    private lateinit var productList: List<ProductListing>
    private val searchDelayMillis = 1000L
    private var searchJob: Job? = null

    private lateinit var binding: FragmentProductBinding
    private lateinit var viewModel: ProductViewModel

    private val productAdapter by lazy {
        ProductAdapter(onItemClick = { productEntity ->
            onProductItemClick(productEntity)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding  = FragmentProductBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        setupRecyclerView()
        observeProducts()
        viewModel.fetchProducts()
        binding.crossImageView.setOnClickListener {
            binding.toolbarTitle.setText("")
            binding.crossImageView.visibility = View.INVISIBLE
            clearCursor()
            hideKeyboard(context,binding.toolbarTitle)
        }

        binding.toolbarTitle.setOnClickListener {
            binding.toolbarTitle.isCursorVisible = true
            binding.crossImageView.visibility = View.VISIBLE
        }

        binding.toolbarTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No implementation needed
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No implementation needed
            }
            override fun afterTextChanged(s: Editable?) {
                val keyword = s.toString().trim()
                performSearch(keyword)
            }
        })
    }

    private fun setupRecyclerView() {
        val verticalLayoutManager = LinearLayoutManager(requireContext())
        binding.rvProduct.apply {
            layoutManager = verticalLayoutManager
            adapter = productAdapter
            addItemDecoration(DividerItemDecoration(context, verticalLayoutManager.orientation))
        }
    }

    private fun observeProducts() {
        viewModel.productLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ViewState.Loading -> {
                   // showLoadingSpinner()
                }
                is ViewState.Success -> {
                    showProducts(it.data)
                }
                is ViewState.Error -> showErrorMessage(it.errorMessage)
            }
        }
    }

    private fun showErrorMessage(errorMessage: String) {
        //dismissLoadingSpinner()
    }

    private fun showProducts(productEntities: List<ProductListing>) {
       // dismissLoadingSpinner()
        productList = productEntities
        setProductList(productList)
    }

    private fun onProductItemClick(productEntity: ProductListing) {
        Toast.makeText(requireContext(),"Clicked",Toast.LENGTH_SHORT).show()
        //startActivity(CommentsActivity.getIntent(this, issueEntity))
    }

    private fun hideKeyboard(context: Context?, target: View?) {
        if (context == null || target == null) {
            return
        }
        val imm = getInputMethodManager(context)
        imm.hideSoftInputFromWindow(target.windowToken, 0)
    }

    private fun getInputMethodManager(context: Context): InputMethodManager {
        return context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    private fun clearCursor() {
        binding.toolbarTitle.isCursorVisible = false
    }

    private fun performSearch(keyword: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            delay(searchDelayMillis)
            if (keyword.isNotEmpty()){
                hideKeyboard(context, binding.toolbarTitle)
                productAdapter.filterList(keyword)
            }
            else{
                productAdapter.submitList(productList)
            }

        }
    }

    private fun setProductList(productList: List<ProductListing>) {
        this.productList = productList
        productAdapter.submitList(productList)
        productAdapter.originalList = productList
    }

}