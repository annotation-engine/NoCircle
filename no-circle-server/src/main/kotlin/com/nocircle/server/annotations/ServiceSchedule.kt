package com.nocircle.server.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ServiceSchedule(
	val schedule: Schedule = Schedule.Developing
)

enum class Schedule {
	Release,
	Developing,
	Designing
}