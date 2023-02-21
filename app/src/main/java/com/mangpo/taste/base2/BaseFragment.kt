package com.mangpo.taste.base2

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.mangpo.taste.base.BaseViewModel
import com.mangpo.taste.util.SpfUtils
import com.mangpo.taste.view.OnBoardingActivity
import com.mangpo.taste.view.OneBtnDialogFragment
import com.mangpo.taste.view.model.OneBtnDialog
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil.hideKeyboard

abstract class BaseFragment<T: ViewDataBinding>(private val layoutResId: Int): Fragment() {
    protected lateinit var binding: T

    private lateinit var refreshTokenErrorDialog: OneBtnDialogFragment

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRefreshTokenErrorDialog()
    }

    override fun onStart() {
        super.onStart()
        initAfterBinding()
    }

    protected abstract fun initAfterBinding()

    private fun initRefreshTokenErrorDialog() {
        refreshTokenErrorDialog = OneBtnDialogFragment()
        refreshTokenErrorDialog.setMyCallback(object : OneBtnDialogFragment.MyCallback {
            override fun end() {
                SpfUtils.clear()
                SpfUtils.writeSpf("onBoarding", true)

                val intent: Intent = Intent(requireContext(), OnBoardingActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra("currentItem", 3)
                startActivity(intent)
            }
        })

        val oneBtnDialog: OneBtnDialog = OneBtnDialog("재로그인이 필요합니다.", "토큰에 문제가 발생해 재로그인이 필요합니다.\n로그인 화면으로 이동합니다.", "확인", listOf(46, 10, 46, 12))
        val bundle: Bundle = Bundle()
        bundle.putParcelable("data", oneBtnDialog)
        refreshTokenErrorDialog.arguments = bundle
    }

    fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    fun setCommonObserver(vmList: List<BaseViewModel>) {
        vmList.forEach { vm ->
            vm.toast.observe(viewLifecycleOwner, Observer {
                val msg = it.getContentIfNotHandled()

                if (msg!=null) {
                    hideKeyboard(requireActivity())
                    showToast(msg)
                }
            })

            vm.tokenExpired.observe(this, Observer {
                if (it && !refreshTokenErrorDialog.isAdded) {
                    refreshTokenErrorDialog.show(requireActivity().supportFragmentManager, null)
                }
            })
        }

        initRefreshTokenErrorDialog()
    }
}