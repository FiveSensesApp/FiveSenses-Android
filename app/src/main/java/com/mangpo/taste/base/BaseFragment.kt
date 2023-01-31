package com.mangpo.taste.base

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewbinding.ViewBinding
import com.mangpo.taste.util.Inflate
import com.mangpo.taste.util.SpfUtils
import com.mangpo.taste.view.OnBoardingActivity
import com.mangpo.taste.view.OneBtnDialogFragment
import com.mangpo.taste.view.model.OneBtnDialog
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil.hideKeyboard

abstract class BaseFragment<VB : ViewBinding, K: BaseViewModel>(
    private val inflate: Inflate<VB>
) : Fragment() {
    abstract val viewModel: K // 뷰모델

    private val refreshTokenErrorDialog: OneBtnDialogFragment = OneBtnDialogFragment()

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)

        initRefreshTokenErrorDialog()
        observe()

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

    private fun initRefreshTokenErrorDialog() {
        refreshTokenErrorDialog.setMyCallback(object : OneBtnDialogFragment.MyCallback {
            override fun end() {
                SpfUtils.clear()
                SpfUtils.writeSpf("onBoarding", true)
                goLogin()
            }
        })

        val oneBtnDialog: OneBtnDialog = OneBtnDialog("재로그인이 필요합니다.", "토큰에 문제가 발생해 재로그인이 필요합니다.\n로그인 화면으로 이동합니다.", "확인", listOf(46, 10, 46, 12))
        val bundle: Bundle = Bundle()
        bundle.putParcelable("data", oneBtnDialog)
        refreshTokenErrorDialog.arguments = bundle
    }

    private fun goLogin() {
        val intent: Intent = Intent(requireContext(), OnBoardingActivity::class.java)
        intent.putExtra("currentItem", 3)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun observe() {
        viewModel.isLoading.observe(viewLifecycleOwner, Observer {
            if (requireActivity() is BaseNoVMActivity<*>) {
                if (it) {
                    (requireActivity() as BaseNoVMActivity<*>).showLoading()
                } else {
                    (requireActivity() as BaseNoVMActivity<*>).hideLoading()
                }
            } else {
                if (it) {
                    (requireActivity() as BaseActivity<*, *>).showLoading()
                } else {
                    (requireActivity() as BaseActivity<*, *>).hideLoading()
                }
            }
        })

        viewModel.toast.observe(viewLifecycleOwner, Observer {
            val msg = it.getContentIfNotHandled()

            if (msg!=null) {
                hideKeyboard(requireActivity())
                showToast(msg)
            }
        })

        viewModel.tokenExpired.observe(viewLifecycleOwner, Observer {
            if (it && !refreshTokenErrorDialog.isAdded) {
                refreshTokenErrorDialog.show(requireActivity().supportFragmentManager, null)
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