package my.edu.tarumt.epf.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import my.edu.tarumt.epf.R
import my.edu.tarumt.epf.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        //Activity:
        // binding = FragmentHomeBinding.inflate(LayoutInflater)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        //Activity:
        //SetContentView(binding.root)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonDividend.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_nav_dividend)
        }
        binding.buttonInvestment.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_nav_investment)
        }
    }
}