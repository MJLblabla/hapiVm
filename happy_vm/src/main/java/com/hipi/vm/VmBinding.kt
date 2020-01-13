package com.hipi.vm

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.hapi.vmannotation.buidFuncName
import com.hapi.vmannotation.suffix
import java.lang.Exception


object VmBinding {


     fun bindVm(target: FragmentActivity) {
          try {
               val classs = target.javaClass
               val claName = classs.name + suffix
               val clazz = Class.forName(claName)
               val bindMethod = clazz.getMethod(buidFuncName, target::class.java)
               val ob = clazz.newInstance()
               bindMethod.invoke(ob, target)
          } catch (e: ClassNotFoundException) {
               e.printStackTrace()
          }
     }


     fun bindVm(target: Fragment) {
          try {
               val classs = target.javaClass
               val claName = classs.name + suffix
               val clazz = Class.forName(claName)
               val bindMethod = clazz.getMethod(buidFuncName, target::class.java)
               val ob = clazz.newInstance()
               bindMethod.invoke(ob, target)
          } catch (e: ClassNotFoundException) {
               e.printStackTrace()
          }
     }



}