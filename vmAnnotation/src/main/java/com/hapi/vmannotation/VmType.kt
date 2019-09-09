package com.hapi.vmannotation

enum class VmType(val type:Int) {
    //以new 一个对象依赖
    FROM_NEW(-1),
    /***
     *  复用使用activity的对象 vm对象　，
     *   　ui发起业务处理　activity fragment都能感知到业务处理结果处理ui ,
     *     activity和fragment共享vm里的数据,
     *     activity和fragment之间通信
     */

    FROM_ACTIVITY(0),
    /**
     * 复用使用父fragment的对象 vm对象　，
     */
    FROM_PARENT(1)
}
