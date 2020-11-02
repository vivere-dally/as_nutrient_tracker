package ro.ubbcluj.cs.sbuciu.nutrient_tracker.domain.model

class Nutrient(
    override var id: Long?,
    var name: String,
    var value: Float
) : Entity<Long>