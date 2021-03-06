package com.example.gnsspositionapp.ui.send

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.gnsspositionapp.R
import com.example.gnsspositionapp.data.EventObserver
import com.example.gnsspositionapp.databinding.FileSendFragmentBinding
import com.example.gnsspositionapp.ui.showShortSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FileSendFragment : Fragment() ,OnItemSelected{

    private val viewModel : FileSendViewModel by viewModels()

    private lateinit var binding : FileSendFragmentBinding

    private lateinit var adapter: FileListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FileListAdapter(this)

        binding.apply {
            fileList.adapter = this@FileSendFragment.adapter

            btnSend.setOnClickListener {
                viewModel.sendCSVFiles()
            }

            btnDelete.setOnClickListener {
                viewModel.deleteCSVFiles()
            }
        }


        viewModel.fileLists.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }

        viewModel.sendFinishedEvent.observe(viewLifecycleOwner,EventObserver{
            showShortSnackBar(binding.root,requireContext(),R.string.snack_bar_send_finished)
        })

        viewModel.sendErrorEvent.observe(viewLifecycleOwner,EventObserver{
            showShortSnackBar(binding.root,requireContext(),R.string.snack_bar_send_error)
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.file_send_fragment,
            container,false
        )

        return binding.root
    }

    override fun onChecked(position: Int) {
        viewModel.addPosition(position)
    }

    override fun onUnChecked(position: Int) {
        viewModel.removePosition(position)
    }
}