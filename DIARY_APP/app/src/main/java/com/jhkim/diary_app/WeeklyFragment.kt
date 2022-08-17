package com.jhkim.diary_app

import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jhkim.diary_app.databinding.FragmentWeeklyBinding
import com.jhkim.diary_app.databinding.WeeklyRecyclerRowBinding
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList


class WeeklyFragment : Fragment() {

    // 제목을 담을 arraylist
    val subject_list = ArrayList<String>()
    // 작성 날짜 담을 arraylist
    val date_list = ArrayList<String>()
    // 글 번호 담을 arraylist
    val idx_list = ArrayList<Int>()
    //글내용
    val text_list = ArrayList<String>()

    lateinit var binding: FragmentWeeklyBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding=FragmentWeeklyBinding.inflate(layoutInflater)


        val weekly_recycler_adapter=WeeklyRecyclerAdapter()
        binding.weeklyRecycler.adapter=weekly_recycler_adapter
        binding.weeklyRecycler.layoutManager=LinearLayoutManager(context)

        return binding.root



    }

    override fun onResume() {
        super.onResume()

        // Arraylist 비워준다
        subject_list.clear()
        date_list.clear()
        idx_list.clear()
        text_list.clear()

        //데이터베이스 오픈
        val helper = DBHelper(context)

        val sql = """
            select diary_subject, diary_date, diary_text, diary_idx
            from DiaryTable
            order by diary_idx desc
        """.trimIndent()

        val c1 = helper.writableDatabase.rawQuery(sql, null)

        while (c1.moveToNext()) {
            //컬럼 인덱스 가져옴
            val idx1 = c1.getColumnIndex("diary_subject")
            val idx2 = c1.getColumnIndex("diary_date")
            val idx3 = c1.getColumnIndex("diary_text")
            val idx4 = c1.getColumnIndex("diary_idx")

            //데이터 가져옴
            val diary_subject = c1.getString(idx1)
            val diary_date = c1.getString(idx2)
            val diary_text = c1.getString(idx3)
            val diary_idx = c1.getInt(idx4)

            Log.d("diary_idx",diary_idx.toString())
            Log.d("diary_date",diary_date.toString())
            Log.d("diary_subject",diary_subject.toString())
            Log.d("diary_text",diary_text.toString())


            print(Calendar.DAY_OF_WEEK_IN_MONTH.toString())


//            Log.d("memo_app",memo_subject)
//            Log.d("memo_app",memo_date)
//            Log.d("memo_app","---------------------------------------")

            //데이터 담기
            subject_list.add(diary_subject)
            date_list.add(diary_date)
            idx_list.add(diary_idx)
            text_list.add(diary_text)


            //RecyclerView에게 갱신하라고 명령
            binding.weeklyRecycler.adapter?.notifyDataSetChanged()
        }
    }

    inner class WeeklyRecyclerAdapter :
        RecyclerView.Adapter<WeeklyRecyclerAdapter.ViewHolderClass>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
            val weeklyRecyclerBinding = WeeklyRecyclerRowBinding.inflate(layoutInflater)
            val holder = ViewHolderClass(weeklyRecyclerBinding)

            val layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            weeklyRecyclerBinding.root.layoutParams = layoutParams

            weeklyRecyclerBinding.root.setOnClickListener(holder)
            return holder
        }


        override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {
            holder.rowDiarySubject.text = subject_list[position]
            holder.rowDiaryDate.text = date_list[position]
            holder.rowDiaryText.text = text_list[position]
        }

        override fun getItemCount(): Int {
            return subject_list.size
        }

        inner class ViewHolderClass(weeklyRecyclerBinding: WeeklyRecyclerRowBinding) :
            RecyclerView.ViewHolder(weeklyRecyclerBinding.root), View.OnClickListener {
            val rowDiarySubject = weeklyRecyclerBinding.WeeklySubject
            val rowDiaryText = weeklyRecyclerBinding.WeeklyText
            val rowDiaryDate = weeklyRecyclerBinding.WeeklyDate

            override fun onClick(p0: View?) {
                val diary_idx=idx_list[adapterPosition]

                var bundle = Bundle()
                bundle.putInt("diary_idx",diary_idx)
                DailyFragment().arguments = bundle //fragment의 arguments에 데이터를 담은 bundle을 넘겨줌

                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.container, DailyFragment())
                    ?.commit()

            }


        }
    }


}