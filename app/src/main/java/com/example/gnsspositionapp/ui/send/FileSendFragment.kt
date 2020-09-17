package com.example.gnsspositionapp.ui.send

import android.content.Intent
import android.os.Bundle
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
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class FileSendFragment : Fragment() ,OnItemSelected{

    private val viewModel : FileSendViewModel by viewModels()

    private lateinit var binding : FileSendFragmentBinding

    private lateinit var adapter: FileListAdapter

    private var drawableIndex = 0

    private val drawables = arrayOf(R.drawable.ic_baseline_send_24,
                                    R.drawable.ic_baseline_delete_24,
                                    R.drawable.ic_baseline_share_24)

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

            fabShare.setOnClickListener {fab ->
                viewModel.shareCSVFiles()
                /*fab.rotation = 0f
                fab.animate()
                    .rotationBy(180f)
                    .setDuration(100)
                    .scaleX(1.1f)
                    .scaleY(1.1f)
                    .withEndAction {
                        (fab as FloatingActionButton).setImageResource(drawables[drawableIndex])
                        fab.animate()
                            .rotationBy(180f)
                            .setDuration(100)
                            .scaleX(1f)
                            .scaleY(1f)
                            .start()
                        drawableIndex = (drawableIndex+1)%3
                    }.start()*/
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

        viewModel.selectedMoreThanOne.observe(viewLifecycleOwner,EventObserver{
            showShortSnackBar(binding.root, requireContext(),R.string.snack_bar_share)
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