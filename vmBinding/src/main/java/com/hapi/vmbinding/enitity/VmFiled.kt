package com.hapi.vmbinding.enitity

import com.hapi.vmannotation.vm
import com.sun.tools.javac.code.Symbol
import javax.lang.model.element.VariableElement

class VmFiled(val variable: VariableElement) {

    val fileName = (variable as Symbol.VarSymbol ).name
    val fileType = variable.asType()

    val vmType = variable.getAnnotation(vm::class.java).vmType

}