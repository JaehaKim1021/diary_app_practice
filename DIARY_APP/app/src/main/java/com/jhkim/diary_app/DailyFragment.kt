package com.jhkim.diary_app

import android.app.IntentService
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.jhkim.diary_app.databinding.FragmentDailyBinding


class DailyFragment : Fragment() {

    lateinit var binding:FragmentDailyBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding=FragmentDailyBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val helper = DBHelper(context)

        val sql = """
            select diary_subject, diary_date, diary_text
            from DiaryTable
            where diary_idx = ?
        """.trimIndent()

        val diary_idx=(activity?.intent?.getIntExtra("diary_idx",0))?.plus(1)

        //뭐리 실행
        val args= arrayOf(diary_idx.toString())
        val c1=helper.writableDatabase.rawQuery(sql,args)

        c1.moveToNext()

        Log.d("diary_idx",diary_idx.toString())

        //글 데이터를 가져옴
        val idx1=c1.getColumnIndex("diary_subject")
        val idx2=c1.getColumnIndex("diary_date")
        val idx3=c1.getColumnIndex("diary_text")

        val diary_subject=c1.getString(idx1)
        val diary_date=c1.getString(idx2)
        val diary_text=c1.getString(idx3)

        helper.writableDatabase.close()


        binding.dailySubject.text=diary_subject
        binding.dailyDate.text=diary_date
        binding.dailyText.text=diary_text

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.bottom_menu,menu)
    }




}