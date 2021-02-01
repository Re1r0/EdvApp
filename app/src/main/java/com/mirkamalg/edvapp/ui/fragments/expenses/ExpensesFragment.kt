package com.mirkamalg.edvapp.ui.fragments.expenses

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.mirkamalg.edvapp.R
import com.mirkamalg.edvapp.databinding.FragmentExpensesBinding
import com.mirkamalg.edvapp.viewmodels.ChequesViewModel

/**
 * Created by Mirkamal on 29 January 2021
 */
class ExpensesFragment : Fragment() {

    private val chequesViewModel: ChequesViewModel by navGraphViewModels(R.id.nav_graph_main)
    private val args: ExpensesFragmentArgs by navArgs()

    private lateinit var binding: FragmentExpensesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition = null

        binding = FragmentExpensesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()

        binding.textViewTotalExpenseBanner.text = args.spendingTotal
    }

    private fun setOnClickListeners() {
        binding.buttonGoBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}