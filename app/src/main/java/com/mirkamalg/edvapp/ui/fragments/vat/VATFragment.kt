package com.mirkamalg.edvapp.ui.fragments.vat

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.mirkamalg.edvapp.R
import com.mirkamalg.edvapp.databinding.FragmentVatBinding
import com.mirkamalg.edvapp.ui.fragments.vat.recyclerview.VATListAdapter
import com.mirkamalg.edvapp.viewmodels.ChequesViewModel

/**
 * Created by Mirkamal on 29 January 2021
 */
class VATFragment : Fragment() {

    private val chequesViewModel: ChequesViewModel by navGraphViewModels(R.id.nav_graph_main)
    private val args: VATFragmentArgs by navArgs()

    private var binding: FragmentVatBinding? = null

    private lateinit var adapter: VATListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition = null

        binding = FragmentVatBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()
        configureRecyclerView()
        configureObservers()

        binding?.textViewTotalVatBanner?.text = args.vatTotal
        chequesViewModel.getAllCheques()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun configureObservers() {
        chequesViewModel.allCheques.observe(viewLifecycleOwner) {
            chequesViewModel.getVatListData(it)
        }
        chequesViewModel.vatList.observe(viewLifecycleOwner) {
            it?.let {
                binding?.apply {
                    recyclerViewVAT.isVisible = true
                    textViewGoodsAndVatsLabel.isVisible = true
                    progressBar.isVisible = false

                    adapter.submitList(it)
                }
            }

        }
    }

    private fun configureRecyclerView() {
        adapter = VATListAdapter()
        binding?.recyclerViewVAT?.adapter = adapter
    }

    private fun setOnClickListeners() {
        binding?.buttonGoBack?.setOnClickListener {
            findNavController().popBackStack()
        }
        binding?.imageButtoninfoVat?.setOnClickListener {
            val extras = FragmentNavigatorExtras(binding!!.imageButtoninfoVat to "infoImageVat")
            findNavController().navigate(
                VATFragmentDirections.actionVATFragmentToVATInfoFragment(),
                extras
            )
        }
    }
}