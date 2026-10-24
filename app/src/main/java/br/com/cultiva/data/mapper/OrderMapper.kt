package br.com.cultiva.data.mapper

import br.com.cultiva.data.local.OrderEntity
import br.com.cultiva.domain.model.Order
import br.com.cultiva.domain.model.OrderStatus

fun OrderEntity.toDomain(): Order = Order(
    id = id,
    highway = highway,
    status = runCatching { OrderStatus.valueOf(status) }.getOrDefault(OrderStatus.ALERTA),
    extension = extension,
    type = type,
    attended = attended,
    completed = completed,
    hasEvidence = hasEvidence
)

fun Order.toEntity(): OrderEntity = OrderEntity(
    id = id,
    highway = highway,
    status = status.name,
    extension = extension,
    type = type,
    attended = attended,
    completed = completed,
    hasEvidence = hasEvidence
)
