package oleh.liskovych.gallerygif.network.bean

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import org.joda.time.DateTime

data class UserBean(@JsonProperty("creation_time")
                    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                    val creationTime: DateTime?,
                    @JsonProperty("token")
                    val token: String?,
                    @JsonProperty("avatar")
                    val avatar: String?)