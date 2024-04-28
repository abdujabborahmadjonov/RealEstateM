package uz.sultonbek1547.hackathonproject2024_innovatex.ui.presentation.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.mahozad.android.PieChart
import uz.sultonbek1547.hackathonproject2024_innovatex.R
import uz.sultonbek1547.hackathonproject2024_innovatex.databinding.FragmentAnalysticsBinding


class AnalysticsFragment : Fragment() {
    val binding by lazy { FragmentAnalysticsBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding.pieChart.slices = listOf(
            PieChart.Slice(0.2f, Color.BLUE),
            PieChart.Slice(0.4f, Color.MAGENTA),
            PieChart.Slice(0.3f, Color.GREEN),
            PieChart.Slice(0.1f, Color.CYAN)
        )
        return binding.root
    }

}