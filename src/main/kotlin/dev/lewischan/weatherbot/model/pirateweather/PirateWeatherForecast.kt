package dev.lewischan.weatherbot.model.pirateweather

import com.fasterxml.jackson.annotation.JsonAlias

data class PirateWeatherForecast(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val offset: Double,
    val elevation: Double,
    val currently: PirateWeatherCurrently,
    val minutely: PirateWeatherMinutely,
    val hourly: PirateWeatherHourly,
    @JsonAlias("day_night") val dayNight: PirateWeatherDayNight,
    val daily: PirateWeatherDaily,
    val alerts: List<PirateWeatherAlert>,
    val flags: PirateWeatherFlags
)

data class PirateWeatherCurrently(
    val time: Long,
    val summary: String,
    val icon: String,
    val nearestStormDistance: Double,
    val nearestStormBearing: Int,
    val precipIntensity: Double,
    val precipProbability: Double,
    val precipIntensityError: Double,
    val precipType: String,
    val rainIntensity: Double,
    val snowIntensity: Double,
    val iceIntensity: Double,
    val temperature: Double,
    val apparentTemperature: Double,
    val dewPoint: Double,
    val humidity: Double,
    val pressure: Double,
    val windSpeed: Double,
    val windGust: Double,
    val windBearing: Int,
    val cloudCover: Double,
    val uvIndex: Double,
    val visibility: Double,
    val ozone: Double,
    val smoke: Double,
    val fireIndex: Double,
    val feelsLike: Double,
    val currentDayIce: Double,
    val currentDayLiquid: Double,
    val currentDaySnow: Double,
    val stationPressure: Double,
    val solar: Double,
    val cape: Int
)

data class PirateWeatherMinutely(
    val summary: String,
    val icon: String,
    val data: List<PirateWeatherMinutelyData>
)

data class PirateWeatherMinutelyData(
    val time: Long,
    val precipIntensity: Double,
    val precipProbability: Double,
    val precipIntensityError: Double,
    val precipType: String,
    val rainIntensity: Double,
    val snowIntensity: Double,
    val iceIntensity: Double?
)

data class PirateWeatherHourly(
    val summary: String,
    val icon: String,
    val data: List<PirateWeatherHourlyData>
)

data class PirateWeatherHourlyData(
    val time: Long,
    val summary: String,
    val icon: String,
    val precipIntensity: Double,
    val precipProbability: Double,
    val precipIntensityError: Double,
    val precipAccumulation: Double,
    val precipType: String,
    val rainIntensity: Double,
    val snowIntensity: Double,
    val iceIntensity: Double,
    val temperature: Double,
    val apparentTemperature: Double,
    val dewPoint: Double,
    val humidity: Double,
    val pressure: Double,
    val stationPressure: Double,
    val windSpeed: Double,
    val windGust: Double,
    val windBearing: Int,
    val cloudCover: Double,
    val uvIndex: Double,
    val visibility: Double,
    val ozone: Double,
    val smoke: Double,
    val liquidAccumulation: Double,
    val snowAccumulation: Double,
    val iceAccumulation: Double,
    val nearestStormDistance: Double,
    val nearestStormBearing: Int,
    val fireIndex: Double,
    val feelsLike: Double,
    val solar: Double,
    val cape: Int
)

data class PirateWeatherDayNight(
    val data: List<PirateWeatherDayNightData>
)

data class PirateWeatherDayNightData(
    val time: Long,
    val summary: String,
    val icon: String,
    val precipIntensity: Double,
    val precipIntensityMax: Double,
    val rainIntensity: Double,
    val rainIntensityMax: Double,
    val snowIntensity: Double,
    val snowIntensityMax: Double,
    val iceIntensity: Double,
    val iceIntensityMax: Double,
    val precipProbability: Double,
    val precipIntensityError: Double?,
    val precipAccumulation: Double,
    val precipType: String,
    val temperature: Double,
    val apparentTemperature: Double,
    val dewPoint: Double,
    val humidity: Double,
    val pressure: Double,
    val stationPressure: Double,
    val windSpeed: Double,
    val windGust: Double,
    val windBearing: Int,
    val cloudCover: Double,
    val uvIndex: Double,
    val visibility: Double,
    val ozone: Double,
    val smoke: Double,
    val liquidAccumulation: Double,
    val snowAccumulation: Double,
    val iceAccumulation: Double,
    val fireIndex: Double,
    val solar: Double,
    val cape: Int
)

data class PirateWeatherDaily(
    val summary: String,
    val icon: String,
    val data: List<PirateWeatherDailyData>
)

data class PirateWeatherDailyData(
    val time: Long,
    val summary: String,
    val icon: String,
    val dawnTime: Long,
    val sunriseTime: Long,
    val sunsetTime: Long,
    val duskTime: Long,
    val moonPhase: Double,
    val precipIntensity: Double,
    val precipIntensityMax: Double,
    val precipIntensityMaxTime: Long,
    val precipProbability: Double,
    val precipAccumulation: Double,
    val precipType: String,
    val rainIntensity: Double,
    val rainIntensityMax: Double,
    val snowIntensity: Double,
    val snowIntensityMax: Double,
    val iceIntensity: Double,
    val iceIntensityMax: Double,
    val temperatureHigh: Double,
    val temperatureHighTime: Long,
    val temperatureLow: Double,
    val temperatureLowTime: Long,
    val apparentTemperatureHigh: Double,
    val apparentTemperatureHighTime: Long,
    val apparentTemperatureLow: Double,
    val apparentTemperatureLowTime: Long,
    val dewPoint: Double,
    val humidity: Double,
    val pressure: Double,
    val windSpeed: Double,
    val windGust: Double,
    val windGustTime: Long,
    val windBearing: Int,
    val cloudCover: Double,
    val uvIndex: Double,
    val uvIndexTime: Long,
    val visibility: Double,
    val temperatureMin: Double,
    val temperatureMinTime: Long,
    val temperatureMax: Double,
    val temperatureMaxTime: Long,
    val apparentTemperatureMin: Double,
    val apparentTemperatureMinTime: Long,
    val apparentTemperatureMax: Double,
    val apparentTemperatureMaxTime: Long,
    val smokeMax: Double,
    val smokeMaxTime: Long,
    val liquidAccumulation: Double,
    val snowAccumulation: Double,
    val iceAccumulation: Double,
    val fireIndexMax: Double,
    val fireIndexMaxTime: Long,
    val solar: Double?,
    val solarMax: Long,
    val cape: Int?,
    val capeMax: Long
)

data class PirateWeatherAlert(
    val title: String,
    val regions: List<String>,
    val severity: String,
    val time: Long,
    val expires: Long,
    val description: String,
    val uri: String
)

data class PirateWeatherFlags(
    val sources: List<String>,
    val sourceTimes: PirateWeatherSourceTimes,
    @JsonAlias("nearest-station") val nearestStation: Double,
    val units: String,
    val version: String,
    @JsonAlias("sourceIDX") val sourceIdx: PirateWeatherSourceIndexes,
    val processTime: Int,
    val ingestVersion: String,
    val nearestCity: String,
    val nearestCountry: String,
    val nearestSubNational: String?
)

data class PirateWeatherSourceTimes(
    @JsonAlias("hrrr_subh") val hrrrSubh: String?,
    @JsonAlias("rtma_ru") val rtmaRu: String?,
    @JsonAlias("hrrr_0-18") val hrrrZeroToEighteen: String?,
    val nbm: String?,
    @JsonAlias("nbm_fire") val nbmFire: String?,
    @JsonAlias("dwd_mosmix") val dwdMosmix: String?,
    @JsonAlias("ecmwf_ifs") val ecmwfIfs: String?,
    val aifs: String?,
    @JsonAlias("hrrr_18-48") val hrrrEighteenToFortyEight: String?,
    val gfs: String?,
    val aigfs: String?,
    val gefs: String?,
    val aigefs: String?
)

data class PirateWeatherSourceIndexes(
    val hrrr: PirateWeatherGridPoint?,
    val nbm: PirateWeatherGridPoint?,
    @JsonAlias("ecmwf_ifs") val ecmwfIfs: PirateWeatherGridPoint?,
    val aifs: PirateWeatherGridPoint?,
    @JsonAlias("dwd_mosmix") val dwdMosmix: PirateWeatherStationSource?,
    @JsonAlias("rtma_ru") val rtmaRu: PirateWeatherGridPoint?,
    val gfs: PirateWeatherGridPoint?,
    val aigfs: PirateWeatherGridPoint?,
    val aigefs: PirateWeatherGridPoint?,
    val etopo: PirateWeatherGridPoint?
)

data class PirateWeatherGridPoint(
    val x: Int,
    val y: Int,
    val lat: Double,
    @JsonAlias("lon") val long: Double?
)

data class PirateWeatherStationSource(
    val stations: List<PirateWeatherStation>
)

data class PirateWeatherStation(
    val id: String,
    val name: String,
    val lat: Double,
    @JsonAlias("lon") val long: Double?
)
