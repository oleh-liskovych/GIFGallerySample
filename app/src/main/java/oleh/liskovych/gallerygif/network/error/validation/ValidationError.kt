package oleh.liskovych.gallerygif.network.error.validation

import com.fasterxml.jackson.annotation.JsonProperty

data class ValidationError(
    @JsonProperty("children")
    var children: ValidationErrorChildren
)

// Error response example:

//{
//    "children":
//    {
//        "username": {},
//        "email": {
//        "errors": [
//        "This value should not be blank."
//        ]
//    },
//        "password": {
//        "errors": [
//        "This value should not be blank."
//        ]
//    },
//        "avatar": {
//        "errors": [
//        "Please, upload the profile avatar picture."
//        ]
//    }
//    }
//}
