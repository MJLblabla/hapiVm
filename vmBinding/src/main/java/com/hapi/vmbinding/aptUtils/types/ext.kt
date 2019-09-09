package com.hapi.vmbinding.aptUtils.types

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import javax.lang.model.element.Element

import kotlin.reflect.jvm.internal.impl.name.FqName
import kotlin.reflect.jvm.internal.impl.platform.JavaToKotlinClassMap

 fun Element.javaToKotlinType(): TypeName =
        asType().asTypeName().javaToKotlinType()

 fun TypeName.javaToKotlinType(): TypeName {
    return if (this is ParameterizedTypeName) {
        (rawType.javaToKotlinType() as ClassName).parameterizedBy(
                *typeArguments.map { it.javaToKotlinType() }.toTypedArray()
        )
    } else {
        var className =
                JavaToKotlinClassMap.INSTANCE.mapJavaToKotlin(FqName(toString()))
                        ?.asSingleFqName()?.asString()
         if(className.equals("java.util.List")){
             className = "kotlin.collections.List"
         }
        return if (className == null) {
            this
        } else {
            ClassName.bestGuess(className)
        }
    }
}

fun <T> Class<T>.toClassName() :ClassName {
    val str = this.canonicalName
    var index = 0
    str.forEachIndexed { i, c ->
        if(c=='.'){
            index = i
        }
    }
    System.out.print(str.substring(0,index)+"   "+ str.substring(index+1,str.length))
    val className = ClassName(str.substring(0,index),str.substring(index+1,str.length))
    return className
}