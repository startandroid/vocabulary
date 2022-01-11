package ru.startandroid.vocabulary.data.mapper

abstract class Mapper<I,O> {

    abstract fun map(input: I): O

    fun mapList(inputList: Collection<I>): List<O> {
        return inputList.map { map(it) }
    }

}