package com.hipi.vm

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.hapi.vmannotation.buidFuncName
import com.hapi.vmannotation.suffix


object VmBinding {

     fun bindVm(target: FragmentActivity){
          val classs = target.javaClass
          val claName = classs.name + suffix
          val clazz = Class.forName(claName)
          val bindMethod = clazz.getMethod(buidFuncName, target::class.java)
          val ob = clazz.newInstance()
          bindMethod.invoke(ob, target)
     }


     fun bindVm(target:Fragment){
          val classs = target.javaClass
          val claName = classs.name + suffix
          val clazz = Class.forName(claName)
          val bindMethod = clazz.getMethod(buidFuncName, target::class.java)
          val ob = clazz.newInstance()
          bindMethod.invoke(ob, target)
     }



}