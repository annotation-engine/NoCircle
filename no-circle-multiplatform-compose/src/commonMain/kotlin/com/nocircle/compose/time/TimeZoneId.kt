package com.nocircle.compose.time

import com.nocircle.common.config.ConfigKey
import com.nocircle.common.config.get
import com.nocircle.common.config.set
import com.nocircle.compose.coroutines.StateConfigManager
import kotlinx.serialization.Serializable

@Serializable
enum class TimeZoneId(
	val zoneId: String,
	val offset: String
) {
	ETC_GMT_12("Etc/GMT+12", "UTC -12:00"),                                         // 贝克岛
	PACIFIC_PAGO_PAGO("Pacific/Pago_Pago", "UTC -11:00"),                           // 美属萨摩亚
	PACIFIC_HONOLULU("Pacific/Honolulu", "UTC -10:00"),                             // 夏威夷
	AMERICA_ANCHORAGE("America/Anchorage", "UTC -09:00"),                           // 阿拉斯加
	AMERICA_LOS_ANGELES("America/Los_Angeles", "UTC -08:00"),                       // 洛杉矶
	AMERICA_DENVER("America/Denver", "UTC -07:00"),                                 // 丹佛
	AMERICA_CHICAGO("America/Chicago", "UTC -06:00"),                               // 芝加哥
	AMERICA_NEW_YORK("America/New_York", "UTC -05:00"),                             // 纽约
	AMERICA_HALIFAX("America/Halifax", "UTC -04:00"),                               // 哈利法克斯
	AMERICA_ARGENTINA_BUENOS_AIRES("America/Argentina/Buenos_Aires", "UTC -03:00"), // 布宜诺斯艾利斯
	AMERICA_NORONHA("America/Noronha", "UTC -02:00"),                               // 费尔南多·迪诺罗尼亚
	ATLANTIC_AZORES("Atlantic/Azores", "UTC -01:00"),                               // 亚速尔群岛
	ETC_UTC("Etc/UTC", "UTC +00:00"),                                               // 世界标准时间
	EUROPE_BERLIN("Europe/Berlin", "UTC +01:00"),                                   // 柏林
	EUROPE_ATHENS("Europe/Athens", "UTC +02:00"),                                   // 雅典
	EUROPE_MOSCOW("Europe/Moscow", "UTC +03:00"),                                   // 莫斯科
	ASIA_TEHRAN("Asia/Tehran", "UTC +03:30"),                                       // 德黑兰
	ASIA_DUBAI("Asia/Dubai", "UTC +04:00"),                                         // 迪拜
	ASIA_KABUL("Asia/Kabul", "UTC +04:30"),                                         // 喀布尔
	ASIA_KARACHI("Asia/Karachi", "UTC +05:00"),                                     // 卡拉奇
	ASIA_KOLKATA("Asia/Kolkata", "UTC +05:30"),                                     // 新德里
	ASIA_KATHMANDU("Asia/Kathmandu", "UTC +05:45"),                                 // 加德满都
	ASIA_DHAKA("Asia/Dhaka", "UTC +06:00"),                                         // 达卡
	ASIA_YANGON("Asia/Yangon", "UTC +06:30"),                                       // 仰光
	ASIA_BANGKOK("Asia/Bangkok", "UTC +07:00"),                                     // 曼谷
	ASIA_SHANGHAI("Asia/Shanghai", "UTC +08:00"),                                   // 上海
	AUSTRALIA_EUCLA("Australia/Eucla", "UTC +08:45"),                               // 欧克拉
	ASIA_TOKYO("Asia/Tokyo", "UTC +09:00"),                                         // 东京
	AUSTRALIA_DARWIN("Australia/Darwin", "UTC +09:30"),                             // 达尔文
	AUSTRALIA_SYDNEY("Australia/Sydney", "UTC +10:00"),                             // 悉尼
	AUSTRALIA_LORD_HOWE("Australia/Lord_Howe", "UTC +10:30"),                       // 豪勋爵岛
	PACIFIC_GUADALCANAL("Pacific/Guadalcanal", "UTC +11:00"),                       // 所罗门群岛
	PACIFIC_AUCKLAND("Pacific/Auckland", "UTC +12:00"),                             // 奥克兰
	PACIFIC_CHATHAM("Pacific/Chatham", "UTC +12:45"),                               // 查塔姆群岛
	PACIFIC_TONGATAPU("Pacific/Tongatapu", "UTC +13:00"),                           // 汤加
	PACIFIC_KIRITIMATI("Pacific/Kiritimati", "UTC +14:00");                         // 基里巴斯圣诞岛
	
	companion object : StateConfigManager<TimeZoneId>() {
		
		override suspend fun getConfigFromStorage(): TimeZoneId {
			return TimeZoneIdConfigKey.get() ?: ASIA_SHANGHAI
		}
		
		override suspend fun setConfigToStorage(oldConfig: TimeZoneId, newConfig: TimeZoneId) {
			TimeZoneIdConfigKey.set(newConfig)
		}
	}
}

private object TimeZoneIdConfigKey : ConfigKey<TimeZoneId>("timeZoneId")