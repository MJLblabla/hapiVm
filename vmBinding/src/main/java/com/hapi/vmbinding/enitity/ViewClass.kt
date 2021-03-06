package com.hapi.vmbinding.enitity


import com.hapi.vmannotation.VmType
import com.hapi.vmannotation.buidFuncName
import com.hapi.vmannotation.suffix
import com.hapi.vmbinding.aptUtils.types.isSubTypeOf
import com.hapi.vmbinding.aptUtils.types.packageName
import com.hapi.vmbinding.aptUtils.types.simpleName
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.io.File
import javax.lang.model.element.TypeElement

/**
 * activity fragment 类
 */
class ViewClass(private val typeElement: TypeElement) {

    val vms: ArrayList<VmFiled> = ArrayList<VmFiled>()


    private val simpleName = typeElement.simpleName()
    /**
     * 生成的类名
     */
    private val generaClassName = simpleName + suffix
    private val activityClassType = ClassName("androidx.appcompat.app", "AppCompatActivity")
    private val fragmentClassName = ClassName("androidx.fragment.app", "Fragment")
    private val dilogClassName = ClassName("androidx.fragment.app", "DialogFragment")

    private val createVm = ClassName("com.hipi.vm", "createVm")
    private val isActivity = typeElement.asType().isSubTypeOf(activityClassType.canonicalName)
    private val isFragMent = typeElement.asType().isSubTypeOf(fragmentClassName.canonicalName)
    private val isDilog =  typeElement.asType().isSubTypeOf(fragmentClassName.canonicalName)


    private var targetName = "target"

    fun build(outputPath: String) {
        if (isActivity || isFragMent) {
            val fileBuilder = FileSpec.builder(typeElement.packageName(), generaClassName)
            val typeBuilder = TypeSpec.classBuilder(generaClassName)
                .addModifiers(KModifier.PUBLIC)
                .addModifiers(KModifier.FINAL)
                .addKdoc("This file is generated by kapt, please do not edit this file")
            funcBuild(typeBuilder)
            fileBuilder.addType(typeBuilder.build())
            fileBuilder.build().writeTo(File(outputPath))
        }
    }

    private fun funcBuild(typeBuilder: TypeSpec.Builder) {
        val paramSpecBuilder = ParameterSpec.builder(targetName, typeElement.asClassName())
        val funcBuilder = FunSpec.builder(buidFuncName)
            .addModifiers(KModifier.PUBLIC)
        // todo return
        funcBuilder.addParameter(paramSpecBuilder.build())
        addStatement(funcBuilder)

        typeBuilder.addFunction(funcBuilder.build())
    }


    private fun addStatement(funcBuilder: FunSpec.Builder) {

        vms.forEach {
            if (isActivity) {
                funcBuilder.addStatement(
                    "%L.%L = " +
                            " %L.%T(%T::class.java)"
                    , targetName
                    , it.fileName
                    , targetName
                    , createVm
                    , it.fileType
                )
            } else {
                funcBuilder.addStatement(
                    "%L.%L = " +
                            " %L.%T(%T::class.java,%T.%L)"
                    , targetName
                    , it.fileName
                    , targetName
                    , createVm
                    , it.fileType
                    , VmType::class.java
                    , it.vmType
                )

            }
        }
    }
}