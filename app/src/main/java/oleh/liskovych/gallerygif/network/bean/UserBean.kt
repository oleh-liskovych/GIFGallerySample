package oleh.liskovych.gallerygif.network.bean

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import oleh.liskovych.gallerygif.DATETIME_PATTERN_USER
import oleh.liskovych.gallerygif.JsonKeywords.AVATAR
import oleh.liskovych.gallerygif.JsonKeywords.CREATION_TIME
import oleh.liskovych.gallerygif.JsonKeywords.TOKEN
import org.joda.time.DateTime

data class UserBean(@JsonProperty(CREATION_TIME)
                    @JsonFormat(pattern = DATETIME_PATTERN_USER)
                    val creationTime: DateTime?,
                    @JsonProperty(TOKEN)
                    val token: String?,
                    @JsonProperty(AVATAR)
                    val avatar: String?)