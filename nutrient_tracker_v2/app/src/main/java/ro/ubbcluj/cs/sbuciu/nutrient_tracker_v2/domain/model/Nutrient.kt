package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.domain.model

class Nutrient(
    override var id: Long?,
    var name: String,
    var value: Float
) : BaseEntity<Long>