package ro.pub.cs.systems.eim.project.hiitapplication.data.dto

data class WorkoutSession(var type: String?, var details: String?, var date: String?) {
    constructor() : this(null, null, null)
}
