package com.hapi.vmbinding.aptUtils.types



import com.hapi.vmbinding.aptUtils.AptContext
import com.squareup.javapoet.TypeName
import com.squareup.kotlinpoet.*
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror
import kotlin.reflect.KClass
import com.squareup.kotlinpoet.TypeName as KotlinTypeName

/**
 * Created by benny on 2/3/18.
 */
object TypeUtils {

    internal fun doubleErasure(elementType: TypeMirror): String {
        var name = AptContext.types.erasure(elementType).toString()
        val typeParamStart = name.indexOf('<')
        if (typeParamStart != -1) {
            name = name.substring(0, typeParamStart)
        }
        return name
    }

    internal fun getTypeFromClassName(className: String) = AptContext.elements.getTypeElement(className).asType()
}

fun TypeElement.packageName(): String {
    var element = this.enclosingElement
    while (element != null && element.kind != ElementKind.PACKAGE) {
        element = element.enclosingElement
    }
    return element?.asType()?.toString() ?: throw IllegalArgumentException("$this does not have an enclosing element of package.")
}

fun Element.simpleName(): String = simpleName.toString()

fun TypeElement.canonicalName(): String = qualifiedName.toString()

fun TypeMirror.simpleName() = TypeUtils.doubleErasure(this).let { name -> name.substring(name.lastIndexOf(".") + 1) }

fun TypeMirror.erasure() = AptContext.types.erasure(this)

//region subType
fun TypeMirror.isSubTypeOf(className: String): Boolean {
    return AptContext.types.isSubtype(this, TypeUtils.getTypeFromClassName(className))
}

fun TypeMirror.isSubTypeOf(cls: Class<*>): Boolean {
    return cls.canonicalName?.let { className ->
        isSubTypeOf(className)
    } ?: false
}

fun TypeMirror.isSubTypeOf(cls: KClass<*>) = isSubTypeOf(cls.java)

fun TypeMirror.isSubTypeOf(typeMirror: TypeMirror): Boolean {
    return AptContext.types.isSubtype(this, typeMirror)
}
//endregion

//region sameType
fun TypeMirror.isSameTypeWith(typeMirror: TypeMirror): Boolean {
    return AptContext.types.isSameType(this, typeMirror)
}

fun TypeMirror.isSameTypeWith(cls: Class<*>): Boolean {
    return cls.canonicalName?.let { className ->
        isSameTypeWith(className)
    } ?: false
}

fun TypeMirror.isSameTypeWith(cls: KClass<*>) = isSameTypeWith(cls.java)

fun TypeMirror.isSameTypeWith(className: String): Boolean {
    return isSameTypeWith(TypeUtils.getTypeFromClassName(className))
}
//endregion

//region Class/KClass
fun Class<*>.asTypeMirror(): TypeMirror {
    return AptContext.elements.getTypeElement(canonicalName).asType()
}

fun Class<*>.asJavaTypeName() = this.asTypeMirror().asJavaTypeName()
fun Class<*>.asKotlinTypeName() = this.asTypeMirror().asKotlinTypeName()
fun Class<*>.asElement() = this.asTypeMirror().asElement()

fun KClass<*>.asTypeMirror(): TypeMirror {
    return AptContext.elements.getTypeElement(qualifiedName/* == java.canonicalName*/).asType()
}

fun KClass<*>.asJavaTypeName() = this.asTypeMirror().asJavaTypeName()
fun KClass<*>.asKotlinTypeName() = this.asTypeMirror().asKotlinTypeName()
fun KClass<*>.asElement() = this.asTypeMirror().asElement()
//endregion

//region TypeMirror
fun TypeMirror.asElement() = AptContext.types.asElement(this)

fun TypeMirror.asJavaTypeName() = TypeName.get(this)

fun TypeMirror.asKotlinTypeName(): KotlinTypeName {
    return this.asTypeName().javaToKotlinType()
}

