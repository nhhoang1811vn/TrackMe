package com.ssteam.trackme.data.db.mapper

abstract class BaseMapper<MODEL, ENTITY> {
    abstract fun mapToEntity(model: MODEL) : ENTITY
    abstract fun mapToModel(entity: ENTITY) : MODEL

    fun mapToEntities(models: List<MODEL>) : List<ENTITY>{
        val entities = mutableListOf<ENTITY>()
        models.forEach {
            entities.add(mapToEntity(it))
        }
        return entities
    }

    fun mapToModels(entities: List<ENTITY>) : List<MODEL>{
        val models = mutableListOf<MODEL>()
        entities.forEach {
            models.add(mapToModel(it))
        }
        return models
    }
}