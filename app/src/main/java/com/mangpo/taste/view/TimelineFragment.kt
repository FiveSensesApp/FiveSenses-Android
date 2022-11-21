package com.mangpo.taste.view

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.mangpo.domain.model.getPosts.ContentEntity
import com.mangpo.domain.model.updatePost.UpdatePostResEntity
import com.mangpo.taste.R
import com.mangpo.taste.base.BaseFragment
import com.mangpo.taste.databinding.FragmentTimelineBinding
import com.mangpo.taste.util.SpfUtils
import com.mangpo.taste.view.adpater.RecordDetailAdapter
import com.mangpo.taste.view.model.TwoBtnDialog
import com.mangpo.taste.viewmodel.FeedViewModel
import com.mangpo.taste.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

@AndroidEntryPoint
class TimelineFragment : BaseFragment<FragmentTimelineBinding>(FragmentTimelineBinding::inflate) {
    private val mainVm: MainViewModel by activityViewModels()
    private val feedVm: FeedViewModel by viewModels()

    private var page: Int = 0
    private var isLast: Boolean = true

    private lateinit var twoBtnDialogFragment: TwoBtnDialogFragment
    private lateinit var recordDetailAdapter: RecordDetailAdapter
    private lateinit var updateCompleteLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        updateCompleteLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent = result.data!!
                val updatedPost: UpdatePostResEntity = data.getParcelableExtra<UpdatePostResEntity>("updatedPost")!!
                recordDetailAdapter.updateData(updatedPost)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTwoBtnDialog()
        initAdapter()
        observe()
    }

    override fun initAfterBinding() {
    }

    private fun initTwoBtnDialog() {
        twoBtnDialogFragment = TwoBtnDialogFragment()
        twoBtnDialogFragment.setMyCallback(object : TwoBtnDialogFragment.MyCallback {
            override fun leftAction(action: String) { //삭제하기, 이미지 저장
                when (action) {
                    getString(R.string.action_delete_long) -> feedVm.deletePost(recordDetailAdapter.getDeletePostId())
                    getString(R.string.action_save_image) -> {

                    }
                }
            }

            override fun rightAction(action: String) {    //뒤로가기, SNS 공유
                when (action) {
                    getString(R.string.action_go_back) -> {

                    }
                    getString(R.string.action_share_SNS) -> {

                    }
                }
            }
        })
    }

    private fun initAdapter() {
        recordDetailAdapter = RecordDetailAdapter()

        recordDetailAdapter.setMyClickListener(object : RecordDetailAdapter.MyClickListener {
            override fun update(content: ContentEntity) {
                val intent = Intent(requireContext(), RecordUpdateActivity::class.java)
                intent.putExtra("content", content)
                updateCompleteLauncher.launch(intent)
            }

            override fun delete() {
                val bundle: Bundle = Bundle()
                bundle.putParcelable("data", TwoBtnDialog(getString(R.string.msg_really_delete), getString(R.string.msg_cannot_recover), getString(R.string.action_delete_long), getString(R.string.action_go_back), R.drawable.bg_gy01_12))
                twoBtnDialogFragment.arguments = bundle
                twoBtnDialogFragment.show(requireActivity().supportFragmentManager, null)
            }

            override fun share() {
                val bundle: Bundle = Bundle()
                bundle.putParcelable("data", TwoBtnDialog(getString(R.string.action_sharing), getString(R.string.msg_share_your_preferences), getString(R.string.action_save_image), getString(R.string.action_share_SNS), R.drawable.bg_gy01_12))
                twoBtnDialogFragment.arguments = bundle
                twoBtnDialogFragment.show(requireActivity().supportFragmentManager, null)
            }

            override fun changeSortFilter(sort: String) {   //정렬 필터(최신순, 오래된순)가 바뀔 때 호출되는 함수
                clearPaging()   //page, isLast 초기화
                feedVm.findCountByParam(SpfUtils.getIntEncryptedSpf("userId"), null, null, null)
                getPosts(page, sort)    //정렬 필터에 따라 getPosts API 호출
            }

            override fun callGetPostsAPI(filter: String) {
                clearPaging()
                getPosts(page, filter)
            }
        })

        //무한스크롤 구현
        binding.timelineRecordRv.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                //스크롤이 최하단에 있을 때
                if (!binding.timelineRecordRv.canScrollVertically(1)) {
                    if (!isLast) {  //마지막 페이지가 아니면 현재페이지+1, 현재 선택돼 있는 정렬 형태를 가져다 getPosts API 호출
                        getPosts(page+1, recordDetailAdapter.getFilter())
                    }
                }
            }
        })

        binding.timelineRecordRv.adapter = recordDetailAdapter
        feedVm.findCountByParam(SpfUtils.getIntEncryptedSpf("userId"), null, null, null)
        getPosts(page, recordDetailAdapter.getFilter())
    }

    //기록 목록 조회 API 호출
    private fun getPosts(page: Int, sort: String) {
        feedVm.getPosts(SpfUtils.getIntEncryptedSpf("userId"), page, "id,$sort", null, null, null)
    }

    private fun clearPaging() {
        page = 0
        isLast = true
    }

    private fun getBitmapFromView(view: View): Bitmap? {
        //Define a bitmap with the same size as the view
        val returnedBitmap = Bitmap.createBitmap(view.measuredWidth, view.measuredHeight, Bitmap.Config.ARGB_8888)

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

    //Android Q (Android 10, API 29 이상에서는 이 메서드를 통해서 이미지를 저장한다.)
    private fun saveImageOnAboveAndroidQ(bitmap: Bitmap): Uri? {
        val fileName = System.currentTimeMillis().toString() + ".png" // 파일이름 현재시간.png

        /*
        * ContentValues() 객체 생성.
        * ContentValues는 ContentResolver가 처리할 수 있는 값을 저장해둘 목적으로 사용된다.
        * */
        val contentValues = ContentValues()
        contentValues.apply {
            put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/ImageSave") // 경로 설정
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName) // 파일이름을 put해준다.
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.IS_PENDING, 1) // 현재 is_pending 상태임을 만들어준다.
            // 다른 곳에서 이 데이터를 요구하면 무시하라는 의미로, 해당 저장소를 독점할 수 있다.
        }

        // 이미지를 저장할 uri를 미리 설정해놓는다.
        val uri = requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        try {
            if(uri != null) {
                val image = requireContext().contentResolver.openFileDescriptor(uri, "w", null)
                // write 모드로 file을 open한다.

                if(image != null) {
                    val fos = FileOutputStream(image.fileDescriptor)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
                    //비트맵을 FileOutputStream를 통해 compress한다.
                    fos.close()

                    contentValues.clear()
                    contentValues.put(MediaStore.Images.Media.IS_PENDING, 0) // 저장소 독점을 해제한다.
                    requireContext().contentResolver.update(uri, contentValues, null, null)
                }
            }
        } catch(e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return uri
    }

    private fun observe() {
        //기록 조회 API 호출 여부를 판단하기 위한 LiveData Observing
        mainVm.callGetPostsFlag.observe(viewLifecycleOwner, Observer {
            val callGetPostsFlag = it.getContentIfNotHandled()

            if (callGetPostsFlag!=null && callGetPostsFlag) {
                clearPaging()   //페이징 관련 데이터 초기화
                recordDetailAdapter.clearData() //현재 리사이클러뷰에 있는 content 데이터들 지우기

                feedVm.findCountByParam(SpfUtils.getIntEncryptedSpf("userId"), null, null, null)    //전체 개수 조회 API 호출
                getPosts(page, recordDetailAdapter.getFilter()) //기록 조회 API 호출
            }
        })

        feedVm.toast.observe(viewLifecycleOwner, Observer {
            val msg: String? = it.getContentIfNotHandled()

            if (msg!=null)
                showToast(msg)
        })

        feedVm.isLoading.observe(viewLifecycleOwner, Observer {
            if (it) {
                (requireActivity() as MainActivity).showLoading()
            } else {
                (requireActivity() as MainActivity).hideLoading()
            }
        })

        feedVm.posts.observe(viewLifecycleOwner, Observer {
            val posts = it.getContentIfNotHandled()

            if (posts!=null) {
                page = posts.pageNumber
                isLast = posts.isLast
                recordDetailAdapter.addData(posts.content)
            }
        })

        feedVm.deletePostResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                200 -> recordDetailAdapter.removeData()
                404 -> showToast("삭제 중 문제가 발생했습니다.")
                else -> {}
            }
        })

        feedVm.feedCnt.observe(viewLifecycleOwner, Observer {
            val feedCnt = it.getContentIfNotHandled()

            if (feedCnt!=null) {
                recordDetailAdapter.setCnt(feedCnt)
            }
        })
    }
}