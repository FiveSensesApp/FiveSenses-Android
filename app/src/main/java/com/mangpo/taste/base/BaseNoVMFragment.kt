package com.mangpo.taste.base

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.mangpo.taste.util.Inflate
import com.mangpo.taste.util.SpfUtils
import com.mangpo.taste.view.OnBoardingActivity
import com.mangpo.taste.view.OneBtnDialogFragment
import com.mangpo.taste.view.model.OneBtnDialog

abstract class BaseNoVMFragment<VB : ViewBinding>(
    private val inflate: Inflate<VB>
) : Fragment() {
    private val baseVm: BaseViewModel by activityViewModels()

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initAfterBinding()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected abstract fun initAfterBinding()

    private fun goLogin() {
        val intent: Intent = Intent(requireContext(), OnBoardingActivity::class.java)
        intent.putExtra("currentItem", 3)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun observe() {
        baseVm.tokenExpired.observe(viewLifecycleOwner, Observer {
            if (it) {
                val oneBtnDialog: OneBtnDialog = OneBtnDialog("재로그인이 필요합니다.", "토큰에 문제가 발생해 재로그인이 필요합니다.\n로그인 화면으로 이동합니다.", "확인", listOf(46, 10, 46, 12))
                val bundle: Bundle = Bundle()
                bundle.putParcelable("data", oneBtnDialog)

                val oneBtnDialogFragment: OneBtnDialogFragment = OneBtnDialogFragment()
                oneBtnDialogFragment.setMyCallback(object : OneBtnDialogFragment.MyCallback {
                    override fun end() {
                        SpfUtils.clear()
                        SpfUtils.writeSpf("onBoarding", true)
                        goLogin()
                    }
                })
                oneBtnDialogFragment.arguments = bundle
                oneBtnDialogFragment.show(requireActivity().supportFragmentManager, null)
            }
        })
    }

    fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun goUrlPage(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}