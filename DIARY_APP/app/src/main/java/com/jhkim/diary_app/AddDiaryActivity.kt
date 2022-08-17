package com.jhkim.diary_app

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.widget.DatePicker
import com.jhkim.diary_app.databinding.ActivityAddDiaryBinding
import kotlinx.android.synthetic.main.activity_add_diary.*
import java.util.*

class AddDiaryActivity : AppCompatActivity() {

    lateinit var binding:ActivityAddDiaryBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityAddDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var dateText=""
        val today=GregorianCalendar()
        val year:Int=today.get(Calendar.YEAR)
        val month:Int=today.get(Calendar.MONTH)+1
        val date:Int=today.get(Calendar.DATE)
        DateSelectButton?.setText("$year / $month / $date")
        dateText="$year / $month / $date"

        DateSelectButton.setOnClickListener {

                val dlg= DatePickerDialog(this,object : DatePickerDialog.OnDateSetListener {
                    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
                        DateSelectButton?.setText("$p1 / ${p2+1} / $p3")
                        dateText="$p1 / ${p2+1} / $p3"
                    }

                },year,month,date)
                dlg.show()
        }
        saveDiary.setOnClickListener {
            //사용자가 입력한 내용 가져오기
            val diary_subject=binding.AddDiarySubject.text
            val diary_text=binding.AddDiaryText.text
            val diary_date=dateText

            //쿼리문
            val sql="""
                    insert into DiaryTable(diary_subject, diary_text, diary_date)
                    values(?, ?, ?)
                """.trimIndent()

            //데이터베이스 오픈
            val helper=DBHelper(this)

            //현재시간 구한다
            val now=diary_date.format(Date())

            // ? 에 셋팅될 값
            val arg1= arrayOf(diary_subject,diary_text,now)

            //저장
            helper.writableDatabase.execSQL(sql,arg1)

            helper.writableDatabase.close()

            finish()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return super.onOptionsItemSelected(item)
    }
}

