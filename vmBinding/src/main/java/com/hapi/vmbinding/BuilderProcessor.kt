package com.hapi.vmbinding

import com.hapi.vmannotation.VmType
import com.hapi.vmannotation.vm
import com.hapi.vmbinding.aptUtils.AptContext
import com.hapi.vmbinding.aptUtils.logger.Logger
import com.hapi.vmbinding.enitity.ViewClass
import com.hapi.vmbinding.enitity.VmFiled
import java.io.File
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

class BuilderProcessor : AbstractProcessor() {

    private val supportedAnnotations = mutableSetOf(vm::class.java)

    private lateinit var mOutputDirectory: String
    private  val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        val res = supportedAnnotations.map { it.canonicalName }.toMutableSet()
        return res
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }


    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        AptContext.init(processingEnv)
        Logger.warn("init  ")
        for (item in processingEnv.options) {
            if (item.key.equals(KAPT_KOTLIN_GENERATED_OPTION_NAME)) {
                mOutputDirectory = item.value
            }
        }
    }

    override fun process(annotations: MutableSet<out TypeElement>?, env: RoundEnvironment): Boolean {

        Logger.warn("process.process    start")
        val viewClassMap = HashMap<Element, ViewClass>()

        env.getElementsAnnotatedWith(vm::class.java).forEach {
            val viewElement = it.enclosingElement as TypeElement
            var viewClass = viewClassMap[viewElement]
            if(viewClass==null){
                 viewClass = ViewClass(viewElement)
                viewClassMap[viewElement] = viewClass
            }
            viewClass.vms.add(VmFiled(it as VariableElement))
        }

        Logger.warn("viewClassMap  "+viewClassMap)
        viewClassMap.forEach { _, viewClass ->
            viewClass.build(mOutputDirectory)
        }

        return true
    }
}