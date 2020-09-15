package com.example.gnsspositionapp.ui.send

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.gnsspositionapp.R
import com.example.gnsspositionapp.data.EventObserver
import com.example.gnsspositionapp.databinding.FileSendFragmentBinding
import com.example.gnsspositionapp.ui.showShortSnackBar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.File
import java.util.*

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

            fabShare.setOnClickListener {
                viewModel.shareCSVFiles()
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

        viewModel.sharedFile.observe(viewLifecycleOwner,EventObserver{
            share(it)
        })

    }

    private fun share(file : File) {

        val uri = FileProvider.getUriForFile(requireContext(),"com.example.gnsspositionapp.MainActivity",file)

        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM,uri)
            type = "text/*"
            flags = Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
        }

        startActivity(Intent.createChooser(shareIntent, "share a file with slack"))
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