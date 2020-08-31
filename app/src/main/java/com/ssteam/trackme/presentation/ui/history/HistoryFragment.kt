package com.ssteam.trackme.presentation.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.SupportMapFragment
import com.ssteam.trackme.R
import com.ssteam.trackme.databinding.FragmentHistoryBinding
import com.ssteam.trackme.databinding.FragmentRecordingBinding
import com.ssteam.trackme.di.Injectable
import com.ssteam.trackme.domain.AppExecutors
import com.ssteam.trackme.domain.Status
import com.ssteam.trackme.presentation.ui.MainActivity
import com.ssteam.trackme.presentation.ui.openRecordingFragmentWithPermissionCheck
import com.ssteam.trackme.presentation.ui.recording.RecordingViewModel
import com.ssteam.trackme.presentation.utils.autoCleared
import javax.inject.Inject

class HistoryFragment : Fragment(),Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var appExecutors: AppExecutors
    private lateinit var adapter: ResultListAdapter
    val viewModel: HistoryViewModel by viewModels {
        viewModelFactory
    }
    var binding by autoCleared<FragmentHistoryBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<FragmentHistoryBinding>(
            inflater,
            R.layout.fragment_history,
            container,
            false
        )
        binding = dataBinding
        return dataBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        binding.recordingCallback = View.OnClickListener {
            (requireActivity() as MainActivity).openRecordingFragmentWithPermissionCheck()
        }
        binding.results = viewModel.results
        val adapter = ResultListAdapter(appExecutors)
        this.adapter = adapter
        binding.resultList.adapter = adapter
        postponeEnterTransition()
        binding.resultList.doOnPreDraw {
            startPostponedEnterTransition()
        }
        initResultList()
    }
    private fun initResultList(){
        viewModel.results.observe(viewLifecycleOwner, Observer { repos ->
            adapter.submitList(repos?.data)
        })
    }
    companion object {
        @JvmStatic
        fun newInstance() = HistoryFragment()
    }
}