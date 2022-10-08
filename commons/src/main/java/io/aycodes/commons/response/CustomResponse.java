package response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@SuperBuilder
@JsonInclude(NON_NULL)
public class CustomResponse {

    private LocalDateTime       timeStamp;
    private String              message;
    private HttpStatus          httpStatus;
    private int                 statusCode;
    private String              reason;
    private JSONObject          data;

    public CustomResponse() {
        timeStamp   = LocalDateTime.now();
        data        =   new JSONObject();
    }
}
