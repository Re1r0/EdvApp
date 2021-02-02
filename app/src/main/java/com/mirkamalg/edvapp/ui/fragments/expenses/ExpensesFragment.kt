package com.mirkamalg.edvapp.ui.fragments.expenses

import android.graphics.Color
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
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
        configureChart(listOf(15f, 24f, 18f, 44f, 23f, 44f, 36f))

        binding.textViewTotalExpenseBanner.text = args.spendingTotal
    }

    private fun configureChart(values: List<Float>) {
        binding.ringChart.apply {
            val typeAndAmount = mapOf(
                context.getString(R.string.msg_monday) to values[0],
                context.getString(R.string.msg_tuesday) to values[1],
                context.getString(R.string.msg_wednesday) to values[2],
                context.getString(R.string.msg_thursday) to values[3],
                context.getString(R.string.msg_friday) to values[4],
                context.getString(R.string.msg_saturday) to values[5],
                context.getString(R.string.msg_sunday) to values[6]
            )
            val colors = listOf(
                ActivityCompat.getColor(context, R.color.colorMonday),
                ActivityCompat.getColor(context, R.color.colorTuesday),
                ActivityCompat.getColor(context, R.color.colorWednesday),
                ActivityCompat.getColor(context, R.color.colorThursday),
                ActivityCompat.getColor(context, R.color.colorFriday),
                ActivityCompat.getColor(context, R.color.colorSaturday),
                ActivityCompat.getColor(context, R.color.colorSunday)
            )
            val pieEntries = arrayListOf<PieEntry>()
            for (key in typeAndAmount.keys) {
                pieEntries.add(PieEntry(typeAndAmount[key] ?: error(""), key))
            }

            val pieDataSet = PieDataSet(pieEntries, "")
            pieDataSet.valueTextSize = 18f
            pieDataSet.valueTextColor = Color.WHITE
            pieDataSet.colors = colors
            val pieData = PieData(pieDataSet)

            setDrawCenterText(true)

            setCenterTextColor(Color.BLACK)
            setCenterTextSize(24f)
            centerText = getString(R.string.title_expenses)

            description = Description().also {
                it.text = ""
            }

            setDrawEntryLabels(false)
            holeRadius = 65f
            legend.isWordWrapEnabled = true
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
            legend.textSize = 18f

            data = pieData
            invalidate()
        }
    }

    private fun setOnClickListeners() {
        binding.buttonGoBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}