package io.github.dnloop.noteapp.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
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
import io.github.dnloop.noteapp.auxiliary.PathUtil
import io.github.dnloop.noteapp.auxiliary.Validator
import io.github.dnloop.noteapp.data.NoteDatabase
import io.github.dnloop.noteapp.databinding.FragmentBackupBinding
import io.github.dnloop.noteapp.ui.viewmodel.ShareViewModel
import io.github.dnloop.noteapp.ui.viewmodel.ShareViewModelFactory
import java.io.File

private const val WRITE_EXTERNAL_STORAGE_ID = 1
private const val PICK_SQL_FILE = 2

class BackupFragment : Fragment() {

    private var _permission = false

    private var _dbImport: File? = null

    private lateinit var shareViewModel: ShareViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity?)?.getFloatingActionButton()?.hide()
        val binding: FragmentBackupBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_backup, container, false
        )

        requestStoragePermission()

        shareViewModel = init()

        shareViewModel.setPermission(_permission)

        binding.btnLocalExport.setOnClickListener {
            if (!shareViewModel.exportLocalDatabase())
                Toast.makeText(activity, "File system permissions required.", Toast.LENGTH_SHORT)
                    .show()
        }

        binding.btnLocalImport.setOnClickListener {
            if (shareViewModel.isPermissionGranted())
                openFile()
            else Toast.makeText(activity, "File system permissions required.", Toast.LENGTH_SHORT)
                .show()
        }

        return binding.root
    }

    private fun init(): ShareViewModel {
        val application = requireNotNull(this.activity).application

        val dataSource = NoteDatabase.getInstance(application)

        val viewModelFactory = ShareViewModelFactory(application, dataSource)

        return ViewModelProvider(this, viewModelFactory).get(ShareViewModel::class.java)
    }

    private fun requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this.requireActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                Toast.makeText(
                    activity,
                    "Permission needed to store database backup on the file system.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this.requireActivity(),
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    WRITE_EXTERNAL_STORAGE_ID
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
            WRITE_EXTERNAL_STORAGE_ID -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    _permission = true
                }
                return
            }
        }

    }

    private fun openFile() {
        val dir = shareViewModel.checkBackupFolder()
        val uri: Uri = Uri.parse(dir)
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            data = uri
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }

        startActivityForResult(intent, PICK_SQL_FILE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_SQL_FILE) {
            val uri = data?.data!!
            val filePath: String?
            filePath = if (!PathUtil.checkVersion()) {
                PathUtil.getPath(this.requireContext(), uri)
            } else {
                val file = File(uri.path)
                val split =
                    file.path.split(":").toTypedArray()
                split[1]
            }
            if (filePath != null) {
                _dbImport = if (Validator.isValidSQLite(filePath))
                    File(filePath)
                else
                    null
                shareViewModel.checkOpenedFile(_dbImport)
            }
        }
    }
}