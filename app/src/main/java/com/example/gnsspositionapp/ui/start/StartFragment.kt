package com.example.gnsspositionapp.ui.start

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.example.gnsspositionapp.R
import com.example.gnsspositionapp.ServiceEventViewModel
import kotlinx.android.synthetic.main.start_fragment.*

class StartFragment : Fragment() {

    companion object {
        private val necessaryPermissions = listOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET
        )
        private const val REQUEST_CODE = 1000
    }

    private val serviceEventViewModel : ServiceEventViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.start_fragment,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_to_measure.setOnClickListener{
            val deniedPermissions = permissionCheck()

            if(deniedPermissions.isEmpty()){
                navigateToMeasureFragment()
                serviceEventViewModel.measureStart()
            }else{
                requestPermissions(deniedPermissions.toTypedArray(), REQUEST_CODE)
            }

        }

        btn_list.setOnClickListener {
            navigateToLocationListFragment()
        }

        button.setOnClickListener {
            navigateToFileSendFragment()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun permissionCheck() : List<String>{
        val deniedPermissions = mutableListOf<String>()

        for (permission in necessaryPermissions) {
            if(ActivityCompat.checkSelfPermission(requireContext(),permission) != PackageManager.PERMISSION_GRANTED){
                deniedPermissions.add(permission)
            }
        }
        return deniedPermissions
    }

    private fun navigateToMeasureFragment() {
        findNavController().navigate(R.id.start_to_measure)
    }
    private fun navigateToLocationListFragment(){
        findNavController().navigate(R.id.start_to_list)
    }

    private fun navigateToFileSendFragment() {
        findNavController().navigate(R.id.start_to_send)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val navController = findNavController()

        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

}