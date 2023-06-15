package com.example.swipeassignment.feature.product

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.swipeassignment.R
import com.example.swipeassignment.common.ViewState
import com.example.swipeassignment.databinding.FragmentProductBinding
import com.example.swipeassignment.network.models.ProductListing
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductFragment : Fragment() {

    companion object {
        const val TAG = "ProductFragment"
        fun newInstance() = ProductFragment()
    }

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
    }

    private fun setupRecyclerView() {
        val verticalLayoutManager = LinearLayoutManager(requireContext())
        binding.rvProduct.apply {
            layoutManager = verticalLayoutManager
            adapter = productAdapter
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
        Toast.makeText(requireContext(), "HElloooooooo", Toast.LENGTH_SHORT).show()
    }

    private fun showProducts(productEntities: List<ProductListing>) {
       // dismissLoadingSpinner()
        productAdapter.submitList(productEntities)
    }

    private fun onProductItemClick(productEntity: ProductListing) {
        Toast.makeText(requireContext(),"Clicked",Toast.LENGTH_SHORT).show()
        //startActivity(CommentsActivity.getIntent(this, issueEntity))
    }

}