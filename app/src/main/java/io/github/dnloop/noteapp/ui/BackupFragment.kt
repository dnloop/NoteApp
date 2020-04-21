package io.github.dnloop.noteapp.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import io.github.dnloop.noteapp.MainActivity
import io.github.dnloop.noteapp.R
import io.github.dnloop.noteapp.data.NoteDatabase
import io.github.dnloop.noteapp.databinding.FragmentBackupBinding
import io.github.dnloop.noteapp.ui.viewmodel.ShareViewModel

class BackupFragment : Fragment() {

    private val _writeExternalStorageId = 1

    private var _permission = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentBackupBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_backup, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = NoteDatabase.getInstance(application)

        val shareViewModel =
            ViewModelProvider(this).get(ShareViewModel::class.java)

        shareViewModel.setDataSource(dataSource)

        requestStoragePermission()

        if (ContextCompat.checkSelfPermission(this.activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED) {
            shareViewModel.setPermission(_permission)
        }

        binding.btnLocalExport.setOnClickListener {
            if (!shareViewModel.exportLocalDatabase())
                Toast.makeText(activity, "File system permissions required.", Toast.LENGTH_SHORT).show()
        }

        binding.btnLocalImport.setOnClickListener {
            if (shareViewModel.importLocalDatabase())
                Toast.makeText(activity, "Database Successfully export.", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(activity, "File system permissions required.", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this.context!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this.activity!!,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(activity, "Permission needed to store database backup on the file system.", Toast.LENGTH_LONG).show()
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this.activity!!,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    _writeExternalStorageId
                )
            }
        } else {
            _permission = true
        }
    }

    override fun onDetach() {
        super.onDetach()
        (activity as MainActivity?)?.getFloatingActionButton()?.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            _writeExternalStorageId -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    _permission = true
                }
                return
            }
        }

    }
}