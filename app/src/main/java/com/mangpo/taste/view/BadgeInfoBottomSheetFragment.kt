package com.mangpo.taste.view

import android.Manifest
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.gun0912.tedpermission.coroutine.TedPermission
import com.mangpo.domain.model.getUserBadgesByUser.GetUserBadgesByUserResEntity
import com.mangpo.taste.R
import com.mangpo.taste.databinding.FragmentBadgeInfoBottomSheetBinding
import com.mangpo.taste.util.*
import com.mangpo.taste.viewmodel.BadgeInfoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.*
import java.io.File.separator
import java.time.LocalDate

@AndroidEntryPoint
class BadgeInfoBottomSheetFragment : BottomSheetDialogFragment() {
    interface EventListener {
        fun goRecord()
        fun goReview()
        fun changeRepresentativeBadge(badgeId: String)
    }

    private val vm: BadgeInfoViewModel by activityViewModels()

    private lateinit var binding: FragmentBadgeInfoBottomSheetBinding
    private lateinit var eventListener: EventListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)

        //높이 설정
        dialog.setOnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog
            BottomSheetDialogUtils.setupRatio(requireContext(), bottomSheetDialog, 0.46)
        }

        return dialog
    }

    //다이얼로그 위 테두리가 둥글게 돼 있는 테마로 설정
    override fun getTheme(): Int {
        return R.style.BottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBadgeInfoBottomSheetBinding.inflate(inflater, container, false)

        observe()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            fragment = this@BadgeInfoBottomSheetFragment
            vm = this@BadgeInfoBottomSheetFragment.vm
            lifecycleOwner = viewLifecycleOwner
            badge = arguments?.getParcelable("badge")
            today = formatDate(LocalDate.now().toString(), "yyyy.MM.dd")
        }
    }

    private fun observe() {
        vm.toast.observe(viewLifecycleOwner, Observer {
            val toast = it.getContentIfNotHandled()

            if (toast != null) {
                Toast.makeText(requireContext(), toast, Toast.LENGTH_SHORT).show()
            }
        })

        vm.isLoading.observe(viewLifecycleOwner, Observer {
            if (it) {
                (requireActivity() as BadgeActivity).showLoading()
            } else {
                (requireActivity() as BadgeActivity).hideLoading()
            }
        })

        vm.updateBadgeResult.observe(viewLifecycleOwner, Observer {
            val updateBadgeResult = it.getContentIfNotHandled()

            if (updateBadgeResult!=null && updateBadgeResult==200) {
                eventListener.changeRepresentativeBadge(SpfUtils.getStrSpf("badgeRepresent")!!)
                dismiss()
            }
        })
    }

    fun setEventListener(eventListener: EventListener) {
        this.eventListener = eventListener
    }

    fun clickedBtnAfter(seqNum: Int) {
        dismiss()

        if (seqNum == 3) {
            eventListener.goReview()
        } else {
            eventListener.goRecord()
        }
    }

    fun saveAsImage(b: GetUserBadgesByUserResEntity) {
        lifecycleScope.launch {
            val permissionResult = lifecycleScope.async {
                TedPermission.create()
                    .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .setDeniedMessage("이미지 저장을 위해 저장소 접근 권한이 필요합니다. 권한을 허용해주세요.")
                    .check()
            }

            if (permissionResult.await().isGranted) {
                binding.cl.visibility = View.VISIBLE
                val bitmap = getBitmapFromView(binding.cl)
                saveImage(bitmap!!, requireContext(), getString(R.string.app_name))
            }
        }
    }

    private fun getBitmapFromView(view: View): Bitmap? {
        //Define a bitmap with the same size as the view
        val returnedBitmap = Bitmap.createBitmap(binding.cl.measuredWidth, binding.cl.measuredHeight, Bitmap.Config.ARGB_8888)

        //Bind a canvas to it
        val canvas = Canvas(returnedBitmap)

        //Get the view's background
        val bgDrawable = view.background
        if (bgDrawable != null) {   //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas)
        } else {    //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE)
        }

        // draw the view on the canvas
        view.draw(canvas)

        //return the bitmap
        return returnedBitmap
    }

    /// @param folderName can be your app's name
    private fun saveImage(bitmap: Bitmap, context: Context, folderName: String) {
        if (Build.VERSION.SDK_INT >= 29) {  // RELATIVE_PATH and IS_PENDING are introduced in API 29.
            val values = contentValues()
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$folderName")
            values.put(MediaStore.Images.Media.IS_PENDING, true)

            val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                saveImageToStream(bitmap, context.contentResolver.openOutputStream(uri))
                values.put(MediaStore.Images.Media.IS_PENDING, false)
                context.contentResolver.update(uri, values, null, null)
            }
        } else {    // getExternalStorageDirectory is deprecated in API 29
            val directory = File(Environment.getExternalStorageDirectory().toString() + separator + folderName)

            if (!directory.exists()) {
                directory.mkdirs()
            }

            val fileName = System.currentTimeMillis().toString() + ".png"
            val file = File(directory, fileName)
            saveImageToStream(bitmap, FileOutputStream(file))

            if (file.absolutePath != null) {
                val values = contentValues()
                values.put(MediaStore.Images.Media.DATA, file.absolutePath) // .DATA is deprecated in API 29
                context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            }
        }

        binding.cl.visibility = View.INVISIBLE
        dismiss()
    }

    private fun contentValues() : ContentValues {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())

        return values
    }

    private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}