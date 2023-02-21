package com.mangpo.taste.view

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.mangpo.domain.model.getStat.GetStatResEntity
import com.mangpo.domain.model.getStat.MonthlyCategoryEntity
import com.mangpo.domain.model.getUserBadgesByUser.GetUserBadgesByUserResEntity
import com.mangpo.taste.R
import com.mangpo.taste.base2.BaseFragment
import com.mangpo.taste.databinding.FragmentAnalysisBinding
import com.mangpo.taste.util.SpfUtils
import com.mangpo.taste.view.adpater.AnalysisBannerVPAdapter
import com.mangpo.taste.view.adpater.NumOfRecordsTrendVPAdapter
import com.mangpo.taste.view.model.AnalysisUserInfo
import com.mangpo.taste.view.model.MonthlyCategory
import com.mangpo.taste.view.model.PercentageOfCategory
import com.mangpo.taste.viewmodel.AnalysisViewModel
import com.mangpo.taste.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class AnalysisFragment : BaseFragment<FragmentAnalysisBinding>(R.layout.fragment_analysis) {
    val stat: MutableLiveData<GetStatResEntity> = MutableLiveData()
    val percentageOfCategory: MutableLiveData<MutableList<PercentageOfCategory>> = MutableLiveData()
    val monthlyCategoryMain: MutableLiveData<MonthlyCategory> = MutableLiveData()
    val monthlyCategories: MutableLiveData<MutableList<MonthlyCategory>> = MutableLiveData()
    val monthlyCategoryCurrentIndex: MutableLiveData<Int> = MutableLiveData()
    val leftArrowEnableStatus: MutableLiveData<Boolean> = MutableLiveData(true)
    val rightArrowEnableStatus: MutableLiveData<Boolean> = MutableLiveData(true)

    private val analysisVm: AnalysisViewModel by viewModels()
    private val mainVm: MainViewModel by activityViewModels()

    private lateinit var numOfRecordsTrendVPAdapter: NumOfRecordsTrendVPAdapter
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    private lateinit var bannerVPAdapter: AnalysisBannerVPAdapter
    private lateinit var bannerTimer: Timer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            fragment = this@AnalysisFragment
            this.analysisVm = this@AnalysisFragment.analysisVm
        }

        binding.analysisDistributionBySensoryBarGraph2.clipToOutline = true

        initActivityResultLaunch()
        initTabLayout()
        initVPAdapter()
        observe()

        analysisVm.getStat(SpfUtils.getIntEncryptedSpf("userId"))
    }

    override fun initAfterBinding() {
        //SharedPreferences 에 저장돼 있는 유저 정보를 활용해 데이터 바인딩 실시
        binding.userInfo = AnalysisUserInfo(SpfUtils.getStrSpf("nickname")!!, SpfUtils.getStrEncryptedSpf("email")!!, SpfUtils.getIntSpf("startDayCnt")!!, SpfUtils.getStrSpf("badgeRepresent"))
        //뱃지 목록 데이터 조회 API 호출
        analysisVm.getBadges(SpfUtils.getIntEncryptedSpf("userId"))
        //배너 자동 스크롤 타이머 시작
        startBannerTimer()
    }

    override fun onPause() {
        super.onPause()

        //배너 자동 스크롤 타이머 실시돼 있으면 화면에서 나갔을 때 타이머 종료
        if (::bannerTimer.isInitialized) {
            bannerTimer.cancel()
        }
    }

    private fun initActivityResultLaunch() {
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val returnValue = it.data?.getBooleanExtra("goRecord", false)
            if (returnValue!=null)
                mainVm.setIsTasteRecordShown(returnValue)
        }
    }

    private fun initTabLayout() {
        numOfRecordsTrendVPAdapter = NumOfRecordsTrendVPAdapter(this@AnalysisFragment)
        binding.analysisVp.adapter = numOfRecordsTrendVPAdapter

        val tabTitles: Array<String> = arrayOf("일간", "월간")
        TabLayoutMediator(binding.analysisTl, binding.analysisVp) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    private fun initVPAdapter() {
        bannerVPAdapter = AnalysisBannerVPAdapter(this)
        binding.analysisBannerVp.adapter = bannerVPAdapter
    }

    private fun startBannerTimer() {
        bannerTimer = Timer()
        bannerTimer.schedule(object : TimerTask() {
            override fun run() {
                requireActivity().runOnUiThread {
                    val currentItem = binding.analysisBannerVp.currentItem

                    if (currentItem==2) {
                        binding.analysisBannerVp.setCurrentItem(0, true)
                    } else {
                        binding.analysisBannerVp.setCurrentItem(currentItem+1, true)
                    }
                }
            }
        }, 3000, 3000)
    }

    private fun observe() {
        //기록 조회 API 호출 여부를 판단하기 위한 LiveData Observing
        mainVm.callGetStatFlag.observe(viewLifecycleOwner, Observer {
            val callGetStatFlag = it.getContentIfNotHandled()

            if (callGetStatFlag!=null && callGetStatFlag) {
                analysisVm.getStat(SpfUtils.getIntEncryptedSpf("userId"))
            }
        })

        analysisVm.getStatResEntity.observe(viewLifecycleOwner, Observer {
//            binding.stat = it
            stat.postValue(it)

            if (it.totalPost>=10) {
                val percentageOfCategories: MutableList<PercentageOfCategory> = arrayListOf()
                percentageOfCategories.add(PercentageOfCategory(getString(R.string.title_sight), it.percentageOfCategory.SIGHT, ContextCompat.getColor(requireContext(), R.color.RD), ContextCompat.getDrawable(requireContext(), R.drawable.ic_sight_character_12)!!, ContextCompat.getDrawable(requireContext(), R.drawable.ic_bar_graph_sight)!!, it.cntOfCategory.SIGHT))
                percentageOfCategories.add(PercentageOfCategory(getString(R.string.title_smell), it.percentageOfCategory.SMELL, ContextCompat.getColor(requireContext(), R.color.GN), ContextCompat.getDrawable(requireContext(), R.drawable.ic_smell_character_12)!!, ContextCompat.getDrawable(requireContext(), R.drawable.ic_bar_graph_smell)!!, it.cntOfCategory.SMELL))
                percentageOfCategories.add(PercentageOfCategory(getString(R.string.title_ear), it.percentageOfCategory.HEARING, ContextCompat.getColor(requireContext(), R.color.Shadow_BU), ContextCompat.getDrawable(requireContext(), R.drawable.ic_ear_character_12)!!, ContextCompat.getDrawable(requireContext(), R.drawable.ic_bar_graph_ear)!!, it.cntOfCategory.HEARING))
                percentageOfCategories.add(PercentageOfCategory(getString(R.string.title_taste), it.percentageOfCategory.TASTE, ContextCompat.getColor(requireContext(), R.color.YE), ContextCompat.getDrawable(requireContext(), R.drawable.ic_taste_character_12)!!, ContextCompat.getDrawable(requireContext(), R.drawable.ic_bar_graph_taste)!!, it.cntOfCategory.TASTE))
                percentageOfCategories.add(PercentageOfCategory(getString(R.string.title_touch), it.percentageOfCategory.TOUCH, ContextCompat.getColor(requireContext(), R.color.Shadow_PU), ContextCompat.getDrawable(requireContext(), R.drawable.ic_touch_character_12)!!, ContextCompat.getDrawable(requireContext(), R.drawable.ic_bar_graph_touch)!!, it.cntOfCategory.TOUCH))
                percentageOfCategories.add(PercentageOfCategory(getString(R.string.title_etc), it.percentageOfCategory.AMBIGUOUS, ContextCompat.getColor(requireContext(), R.color.GY_04), ContextCompat.getDrawable(requireContext(), R.drawable.ic_question_character_12)!!, ContextCompat.getDrawable(requireContext(), R.drawable.ic_bar_graph_question)!!, it.cntOfCategory.AMBIGUOUS))
                percentageOfCategories.sortByDescending { it.percentage }
//                binding.percentageOfCategory = percentageOfCategories
                percentageOfCategory.postValue(percentageOfCategories)

                val fragment1 = NumOfRecordsTrendGraphFragment()
                val bundle1: Bundle = Bundle()
                bundle1.putParcelableArrayList("dayData", it.countByDayEntities as ArrayList<out Parcelable>)
                fragment1.arguments = bundle1

                val fragment2 = NumOfRecordsTrendGraphFragment()
                val bundle2: Bundle = Bundle()
                bundle2.putParcelableArrayList("monthData", it.countByMonthEntities as ArrayList<out Parcelable>)
                fragment2.arguments = bundle2

                numOfRecordsTrendVPAdapter.setFragments(mutableListOf(fragment1, fragment2))

//                binding.monthlyCategoryMain = mapperToMonthlyCategory(true, it.monthlyCategoryEntities[0])
                monthlyCategoryMain.postValue(mapperToMonthlyCategory(true, it.monthlyCategoryEntities[0]))
                drawMonthlyCategories(it.monthlyCategoryEntities, it.monthlyCategoryEntities[0])
            }
        })

//        binding.invalidateAll()
    }

    private fun drawMonthlyCategories(monthlyCategoryEntities: List<MonthlyCategoryEntity>, firstMonthlyCategoryEntity: MonthlyCategoryEntity) {
        val firstIndex = monthlyCategoryEntities.indexOf(firstMonthlyCategoryEntity)
//        var leftArrowEnableStatus: Boolean = true
//        var rightArrowEnableStatus: Boolean = true

        if (firstIndex + 3 < monthlyCategoryEntities.size) {
            leftArrowEnableStatus.postValue(true)
//            leftArrowEnableStatus = true
//            rightArrowEnableStatus = firstIndex != 0

        } else {
//            leftArrowEnableStatus = false
            leftArrowEnableStatus.postValue(false)
//            rightArrowEnableStatus = firstIndex != 0
        }
        rightArrowEnableStatus.postValue(firstIndex != 0)

        val monthlyCategories: MutableList<MonthlyCategory> = mutableListOf()
        for (i in firstIndex until firstIndex + 3) {
            if (i<=monthlyCategoryEntities.lastIndex) {
                monthlyCategories.add(mapperToMonthlyCategory(false, monthlyCategoryEntities[i]))
            }
        }

        this@AnalysisFragment.monthlyCategories.postValue(monthlyCategories)
        this@AnalysisFragment.monthlyCategoryCurrentIndex.postValue(firstIndex)

        /*binding.apply {
            this.monthlyCategories = monthlyCategories
            this.monthlyCategoryCurrentIndex = firstIndex
            this.leftArrowEnableStatus = leftArrowEnableStatus
            this.rightArrowEnableStatus = rightArrowEnableStatus
        }*/
    }

    private fun mapperToMonthlyCategory(isMain: Boolean, monthlyCategoryEntity: MonthlyCategoryEntity): MonthlyCategory {
        if (isMain) {
            return when (monthlyCategoryEntity.category) {
                getString(R.string.title_sight) -> MonthlyCategory(ContextCompat.getDrawable(requireContext(), R.drawable.ic_monthly_category_main_sight), monthlyCategoryEntity.month, ContextCompat.getColor(requireContext(), R.color.RD_2), monthlyCategoryEntity.category, monthlyCategoryEntity.cnt)
                getString(R.string.title_ear) -> MonthlyCategory(ContextCompat.getDrawable(requireContext(), R.drawable.ic_monthly_category_main_ear), monthlyCategoryEntity.month, ContextCompat.getColor(requireContext(), R.color.BU_2), monthlyCategoryEntity.category, monthlyCategoryEntity.cnt)
                getString(R.string.title_touch) -> MonthlyCategory(ContextCompat.getDrawable(requireContext(), R.drawable.ic_monthly_category_main_touch), monthlyCategoryEntity.month, ContextCompat.getColor(requireContext(), R.color.PU_2), monthlyCategoryEntity.category, monthlyCategoryEntity.cnt)
                getString(R.string.title_smell) -> MonthlyCategory(ContextCompat.getDrawable(requireContext(), R.drawable.ic_monthly_category_main_smell), monthlyCategoryEntity.month, ContextCompat.getColor(requireContext(), R.color.GN_3), monthlyCategoryEntity.category, monthlyCategoryEntity.cnt)
                getString(R.string.title_taste) -> MonthlyCategory(ContextCompat.getDrawable(requireContext(), R.drawable.ic_monthly_category_main_taste), monthlyCategoryEntity.month, ContextCompat.getColor(requireContext(), R.color.YE_2), monthlyCategoryEntity.category, monthlyCategoryEntity.cnt)
                else -> MonthlyCategory(character=ContextCompat.getDrawable(requireContext(), R.drawable.ic_monthly_category_main_no_records), month=monthlyCategoryEntity.month)
            }
        } else {
            return when (monthlyCategoryEntity.category) {
                getString(R.string.title_sight) -> MonthlyCategory(ContextCompat.getDrawable(requireContext(), R.drawable.ic_monthly_category_sight)!!, monthlyCategoryEntity.month)
                getString(R.string.title_ear) -> MonthlyCategory(ContextCompat.getDrawable(requireContext(), R.drawable.ic_monthly_category_ear)!!, monthlyCategoryEntity.month)
                getString(R.string.title_touch) -> MonthlyCategory(ContextCompat.getDrawable(requireContext(), R.drawable.ic_monthly_category_touch)!!, monthlyCategoryEntity.month)
                getString(R.string.title_smell) -> MonthlyCategory(ContextCompat.getDrawable(requireContext(), R.drawable.ic_monthly_category_smell)!!, monthlyCategoryEntity.month)
                getString(R.string.title_taste) -> MonthlyCategory(ContextCompat.getDrawable(requireContext(), R.drawable.ic_monthly_category_taste)!!, monthlyCategoryEntity.month)
                else -> MonthlyCategory(ContextCompat.getDrawable(requireContext(), R.drawable.bg_gy01_8)!!, monthlyCategoryEntity.month)
            }
        }
    }

    fun toSettingActivity() {
        findNavController().navigate(R.id.action_analysisFragment_to_settingActivity)
    }

    fun toUpdateNicknameActivity() {
        findNavController().navigate(R.id.action_analysisFragment_to_updateNicknameActivity)
    }

    fun goBadgeActivity(representativeBadge: GetUserBadgesByUserResEntity?) {
        val intent: Intent = Intent(requireContext(), BadgeActivity::class.java)
        intent.putExtra("representativeBadge", representativeBadge)
        intent.putExtra("badges", analysisVm.getBadges().toTypedArray())
        activityResultLauncher.launch(intent)
    }

    fun clickedAtLeftArrowIb(monthlyCategoryEntities: List<MonthlyCategoryEntity>, index: Int) {
        drawMonthlyCategories(monthlyCategoryEntities, monthlyCategoryEntities[index+3])
    }

    fun clickedAtRightArrowIb(monthlyCategoryEntities: List<MonthlyCategoryEntity>, index: Int) {
        drawMonthlyCategories(monthlyCategoryEntities, monthlyCategoryEntities[index-3])
    }
}
/*
class AnalysisFragment : BaseFragment<FragmentAnalysisBinding, AnalysisViewModel>(FragmentAnalysisBinding::inflate) {
    override val viewModel: AnalysisViewModel by viewModels()
    private val mainVm: MainViewModel by activityViewModels()

    private lateinit var numOfRecordsTrendVPAdapter: NumOfRecordsTrendVPAdapter
    private lateinit var activityResultLauncher : ActivityResultLauncher<Intent>
    private lateinit var bannerVPAdapter: AnalysisBannerVPAdapter
    private lateinit var bannerTimer: Timer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            fragment = this@AnalysisFragment
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        binding.analysisDistributionBySensoryBarGraph2.clipToOutline = true

        initActivityResultLaunch()
        initTabLayout()
        initVPAdapter()
        observe()

        viewModel.getStat(SpfUtils.getIntEncryptedSpf("userId"))
    }

    override fun initAfterBinding() {
        binding.userInfo = AnalysisUserInfo(SpfUtils.getStrSpf("nickname")!!, SpfUtils.getStrEncryptedSpf("email")!!, SpfUtils.getIntSpf("startDayCnt")!!, SpfUtils.getStrSpf("badgeRepresent"))
        viewModel.getBadges(SpfUtils.getIntEncryptedSpf("userId"))

        startBannerTimer()
    }

    override fun onPause() {
        super.onPause()

        if (::bannerTimer.isInitialized) {
            bannerTimer.cancel()
        }
    }

    private fun initActivityResultLaunch() {
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val returnValue = it.data?.getBooleanExtra("goRecord", false)
            if (returnValue!=null)
                mainVm.setIsTasteRecordShown(returnValue)
        }
    }

    private fun initTabLayout() {
        numOfRecordsTrendVPAdapter = NumOfRecordsTrendVPAdapter(this@AnalysisFragment)
        binding.analysisVp.adapter = numOfRecordsTrendVPAdapter

        val tabTitles: Array<String> = arrayOf("일간", "월간")
        TabLayoutMediator(binding.analysisTl, binding.analysisVp) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }

    private fun initVPAdapter() {
        bannerVPAdapter = AnalysisBannerVPAdapter(this)
        binding.analysisBannerVp.adapter = bannerVPAdapter
    }

    private fun startBannerTimer() {
        bannerTimer = Timer()
        bannerTimer.schedule(object : TimerTask() {
            override fun run() {
                requireActivity().runOnUiThread {
                    val currentItem = binding.analysisBannerVp.currentItem

                    if (currentItem==2) {
                        binding.analysisBannerVp.setCurrentItem(0, true)
                    } else {
                        binding.analysisBannerVp.setCurrentItem(currentItem+1, true)
                    }
                }
            }
        }, 3000, 3000)
    }

    private fun observe() {
        //기록 조회 API 호출 여부를 판단하기 위한 LiveData Observing
        mainVm.callGetStatFlag.observe(viewLifecycleOwner, Observer {
            val callGetStatFlag = it.getContentIfNotHandled()

            if (callGetStatFlag!=null && callGetStatFlag) {
                viewModel.getStat(SpfUtils.getIntEncryptedSpf("userId"))
            }
        })

        viewModel.getStatResEntity.observe(viewLifecycleOwner, Observer {
            binding.stat = it

            if (it.totalPost>=10) {
                val percentageOfCategories: MutableList<PercentageOfCategory> = arrayListOf()
                percentageOfCategories.add(PercentageOfCategory(getString(R.string.title_sight), it.percentageOfCategory.SIGHT, ContextCompat.getColor(requireContext(), R.color.RD), ContextCompat.getDrawable(requireContext(), R.drawable.ic_sight_character_12)!!, ContextCompat.getDrawable(requireContext(), R.drawable.ic_bar_graph_sight)!!, it.cntOfCategory.SIGHT))
                percentageOfCategories.add(PercentageOfCategory(getString(R.string.title_smell), it.percentageOfCategory.SMELL, ContextCompat.getColor(requireContext(), R.color.GN), ContextCompat.getDrawable(requireContext(), R.drawable.ic_smell_character_12)!!, ContextCompat.getDrawable(requireContext(), R.drawable.ic_bar_graph_smell)!!, it.cntOfCategory.SMELL))
                percentageOfCategories.add(PercentageOfCategory(getString(R.string.title_ear), it.percentageOfCategory.HEARING, ContextCompat.getColor(requireContext(), R.color.Shadow_BU), ContextCompat.getDrawable(requireContext(), R.drawable.ic_ear_character_12)!!, ContextCompat.getDrawable(requireContext(), R.drawable.ic_bar_graph_ear)!!, it.cntOfCategory.HEARING))
                percentageOfCategories.add(PercentageOfCategory(getString(R.string.title_taste), it.percentageOfCategory.TASTE, ContextCompat.getColor(requireContext(), R.color.YE), ContextCompat.getDrawable(requireContext(), R.drawable.ic_taste_character_12)!!, ContextCompat.getDrawable(requireContext(), R.drawable.ic_bar_graph_taste)!!, it.cntOfCategory.TASTE))
                percentageOfCategories.add(PercentageOfCategory(getString(R.string.title_touch), it.percentageOfCategory.TOUCH, ContextCompat.getColor(requireContext(), R.color.Shadow_PU), ContextCompat.getDrawable(requireContext(), R.drawable.ic_touch_character_12)!!, ContextCompat.getDrawable(requireContext(), R.drawable.ic_bar_graph_touch)!!, it.cntOfCategory.TOUCH))
                percentageOfCategories.add(PercentageOfCategory(getString(R.string.title_etc), it.percentageOfCategory.AMBIGUOUS, ContextCompat.getColor(requireContext(), R.color.GY_04), ContextCompat.getDrawable(requireContext(), R.drawable.ic_question_character_12)!!, ContextCompat.getDrawable(requireContext(), R.drawable.ic_bar_graph_question)!!, it.cntOfCategory.AMBIGUOUS))
                percentageOfCategories.sortByDescending { it.percentage }
                binding.percentageOfCategory = percentageOfCategories

                val fragment1 = NumOfRecordsTrendGraphFragment()
                val bundle1: Bundle = Bundle()
                bundle1.putParcelableArrayList("dayData", it.countByDayEntities as ArrayList<out Parcelable>)
                fragment1.arguments = bundle1

                val fragment2 = NumOfRecordsTrendGraphFragment()
                val bundle2: Bundle = Bundle()
                bundle2.putParcelableArrayList("monthData", it.countByMonthEntities as ArrayList<out Parcelable>)
                fragment2.arguments = bundle2

                numOfRecordsTrendVPAdapter.setFragments(mutableListOf(fragment1, fragment2))

                binding.monthlyCategoryMain = mapperToMonthlyCategory(true, it.monthlyCategoryEntities[0])
                drawMonthlyCategories(it.monthlyCategoryEntities, it.monthlyCategoryEntities[0])
            }
        })

        binding.invalidateAll()
    }

    private fun drawMonthlyCategories(monthlyCategoryEntities: List<MonthlyCategoryEntity>, firstMonthlyCategoryEntity: MonthlyCategoryEntity) {
        val firstIndex = monthlyCategoryEntities.indexOf(firstMonthlyCategoryEntity)
        var leftArrowEnableStatus: Boolean = true
        var rightArrowEnableStatus: Boolean = true

        if (firstIndex + 3 < monthlyCategoryEntities.size) {
            leftArrowEnableStatus = true
            rightArrowEnableStatus = firstIndex != 0

        } else {
            leftArrowEnableStatus = false
            rightArrowEnableStatus = firstIndex != 0
        }

        val monthlyCategories: MutableList<MonthlyCategory> = mutableListOf()
        for (i in firstIndex until firstIndex + 3) {
            if (i<=monthlyCategoryEntities.lastIndex) {
                monthlyCategories.add(mapperToMonthlyCategory(false, monthlyCategoryEntities[i]))
            }
        }

        binding.apply {
            this.monthlyCategories = monthlyCategories
            this.monthlyCategoryCurrentIndex = firstIndex
            this.leftArrowEnableStatus = leftArrowEnableStatus
            this.rightArrowEnableStatus = rightArrowEnableStatus
        }
    }

    private fun mapperToMonthlyCategory(isMain: Boolean, monthlyCategoryEntity: MonthlyCategoryEntity): MonthlyCategory {
        if (isMain) {
            return when (monthlyCategoryEntity.category) {
                getString(R.string.title_sight) -> MonthlyCategory(ContextCompat.getDrawable(requireContext(), R.drawable.ic_monthly_category_main_sight), monthlyCategoryEntity.month, ContextCompat.getColor(requireContext(), R.color.RD_2), monthlyCategoryEntity.category, monthlyCategoryEntity.cnt)
                getString(R.string.title_ear) -> MonthlyCategory(ContextCompat.getDrawable(requireContext(), R.drawable.ic_monthly_category_main_ear), monthlyCategoryEntity.month, ContextCompat.getColor(requireContext(), R.color.BU_2), monthlyCategoryEntity.category, monthlyCategoryEntity.cnt)
                getString(R.string.title_touch) -> MonthlyCategory(ContextCompat.getDrawable(requireContext(), R.drawable.ic_monthly_category_main_touch), monthlyCategoryEntity.month, ContextCompat.getColor(requireContext(), R.color.PU_2), monthlyCategoryEntity.category, monthlyCategoryEntity.cnt)
                getString(R.string.title_smell) -> MonthlyCategory(ContextCompat.getDrawable(requireContext(), R.drawable.ic_monthly_category_main_smell), monthlyCategoryEntity.month, ContextCompat.getColor(requireContext(), R.color.GN_3), monthlyCategoryEntity.category, monthlyCategoryEntity.cnt)
                getString(R.string.title_taste) -> MonthlyCategory(ContextCompat.getDrawable(requireContext(), R.drawable.ic_monthly_category_main_taste), monthlyCategoryEntity.month, ContextCompat.getColor(requireContext(), R.color.YE_2), monthlyCategoryEntity.category, monthlyCategoryEntity.cnt)
                else -> MonthlyCategory(character=ContextCompat.getDrawable(requireContext(), R.drawable.ic_monthly_category_main_no_records), month=monthlyCategoryEntity.month)
            }
        } else {
            return when (monthlyCategoryEntity.category) {
                getString(R.string.title_sight) -> MonthlyCategory(ContextCompat.getDrawable(requireContext(), R.drawable.ic_monthly_category_sight)!!, monthlyCategoryEntity.month)
                getString(R.string.title_ear) -> MonthlyCategory(ContextCompat.getDrawable(requireContext(), R.drawable.ic_monthly_category_ear)!!, monthlyCategoryEntity.month)
                getString(R.string.title_touch) -> MonthlyCategory(ContextCompat.getDrawable(requireContext(), R.drawable.ic_monthly_category_touch)!!, monthlyCategoryEntity.month)
                getString(R.string.title_smell) -> MonthlyCategory(ContextCompat.getDrawable(requireContext(), R.drawable.ic_monthly_category_smell)!!, monthlyCategoryEntity.month)
                getString(R.string.title_taste) -> MonthlyCategory(ContextCompat.getDrawable(requireContext(), R.drawable.ic_monthly_category_taste)!!, monthlyCategoryEntity.month)
                else -> MonthlyCategory(ContextCompat.getDrawable(requireContext(), R.drawable.bg_gy01_8)!!, monthlyCategoryEntity.month)
            }
        }
    }

    fun toSettingActivity() {
        findNavController().navigate(R.id.action_analysisFragment_to_settingActivity)
    }

    fun toUpdateNicknameActivity() {
        findNavController().navigate(R.id.action_analysisFragment_to_updateNicknameActivity)
    }

    fun goBadgeActivity(representativeBadge: GetUserBadgesByUserResEntity?) {
        val intent: Intent = Intent(requireContext(), BadgeActivity::class.java)
        intent.putExtra("representativeBadge", representativeBadge)
        intent.putExtra("badges", viewModel.getBadges().toTypedArray())
        activityResultLauncher.launch(intent)
    }

    fun clickedAtLeftArrowIb(monthlyCategoryEntities: List<MonthlyCategoryEntity>, index: Int) {
        drawMonthlyCategories(monthlyCategoryEntities, monthlyCategoryEntities[index+3])
    }

    fun clickedAtRightArrowIb(monthlyCategoryEntities: List<MonthlyCategoryEntity>, index: Int) {
        drawMonthlyCategories(monthlyCategoryEntities, monthlyCategoryEntities[index-3])
    }
}*/
