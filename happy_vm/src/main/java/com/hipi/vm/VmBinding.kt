package com.hipi.vm

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.hapi.vmannotation.buidFuncName
import com.hapi.vmannotation.suffix


object VmBinding {

     fun bindVm(target: androidx.fragment.app.FragmentActivity){
          val classs = target.javaClass
          val claName = classs.name + suffix
          val clazz = Class.forName(claName)
          val bindMethod = clazz.getMethod(buidFuncName, target::class.java)
          val ob = clazz.newInstance()
          bindMethod.invoke(ob, target)
     }


     fun bindVm(target: androidx.fragment.app.Fragment){
          val classs = target.javaClass
          val claName = classs.name + suffix
          val clazz = Class.forName(claName)
          val bindMethod = clazz.getMethod(buidFuncName, target::class.java)
          val ob = clazz.newInstance()
          bindMethod.invoke(ob, target)
     }



}