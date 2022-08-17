package com.jhkim.diary_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jhkim.diary_app.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)


        //첫화면을 montly fragment로 설정
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, WeeklyFragment()).commit()

        binding.writeDiary.setOnClickListener {
            val writeDiaryIntent = Intent(this, AddDiaryActivity::class.java)
            startActivity(writeDiaryIntent)
        }

        binding.bottomMenu.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.menu_daily -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, DailyFragment())
                        .commit()
                }
                R.id.menu_weekly -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, WeeklyFragment())
                        .commit()
                }
                R.id.menu_monthly -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, MonthlyFragment())
                        .commit()
                }
            }
            true
        }
    }



}