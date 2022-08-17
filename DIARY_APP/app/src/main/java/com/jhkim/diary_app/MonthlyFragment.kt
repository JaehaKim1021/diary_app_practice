package com.jhkim.diary_app

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import com.jhkim.diary_app.databinding.FragmentMonthlyBinding
import com.jhkim.diary_app.databinding.FragmentWeeklyBinding
import kotlinx.android.synthetic.main.activity_add_diary.*
import kotlinx.android.synthetic.main.fragment_monthly.*
import kotlinx.android.synthetic.main.fragment_monthly.view.*
import java.util.*

class MonthlyFragment : Fragment() {

    // 제목을 담을 arraylist
    val subject_list = ArrayList<String>()
    // 작성 날짜 담을 arraylist
    val date_list = ArrayList<String>()
    // 글 번호 담을 arraylist
    val idx_list = ArrayList<Int>()
    //글내용
    val text_list = ArrayList<String>()

    lateinit var binding:FragmentMonthlyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentMonthlyBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
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


            print(android.icu.util.Calendar.DAY_OF_WEEK_IN_MONTH.toString())


            subject_list.add(diary_subject)
            date_list.add(diary_date)
            idx_list.add(diary_idx)
            text_list.add(diary_text)


        }



        var dateText=""
        val today=GregorianCalendar()
        val year:Int=today.get(Calendar.YEAR)
        val month:Int=today.get(Calendar.MONTH)+1
        val date:Int=today.get(Calendar.DATE)


        monthly_calendar.setOnDateChangeListener { calendarView, i, i2, i3 ->

            dateText="$i / ${i2+1} / $i3"
            textView1.text=dateText
            Log.d("date_list",date_list[0])
            Log.d("date_list_idx",date_list.toArray().indexOf(dateText).toString())
            var diary_idx=-1

            if(date_list.toArray().indexOf(dateText) >=0)
                diary_idx=date_list.toArray().indexOf(dateText)

                var bundle = Bundle()
                bundle.putInt("diary_idx",diary_idx)
                DailyFragment().arguments = bundle //fragment의 arguments에 데이터를 담은 bundle을 넘겨줌

                activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.container, DailyFragment())
                ?.commit()

            textView3.text= date_list.toArray().indexOf(dateText).toString()


        }

        monthly_calendar.setOnClickListener {
            val dlg= DatePickerDialog(requireContext(),object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
                    DateSelectButton?.setText("$p1 / ${p2+1} / $p3")
                    dateText="$p1 / ${p2+1} / $p3"
                }

            },year,month,date)
            dlg.show()

            Log.d("date_list 몇번째 인덱스",date_list.indexOf(dateText).toString())




        }
    }


}