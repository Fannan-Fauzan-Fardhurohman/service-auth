package com.fanxan.serviceauth.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.Objects;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonIgnoreProperties(
        ignoreUnknown = true
)
public class BaseErrorResponse {
    @Generated
    private static final Logger log = LoggerFactory.getLogger(BaseErrorResponse.class);
    @JsonIgnore
    private static final String nodeErrorName = "error";
    private Long timestamp;
    private String code;
    private Integer status;
    private String error;

    public BaseErrorResponse() {
    }

    @JsonIgnore
    public static BaseErrorResponse parseFromResponse(HttpResponse<String> response) {
        BaseErrorResponse decode = null;
        JsonMapper mapper = new JsonMapper();

        Exception ex;
        try {
            decode = (BaseErrorResponse)mapper.readValue((String)response.body(), new TypeReference<BaseErrorResponse>() {
            });
        } catch (Exception var5) {
            ex = var5;
            log.error(ex.getMessage(), ex);
        }

        if (Objects.isNull(decode)) {
            decode = builder().build();

            try {
                JsonNode jsonNode = (JsonNode)mapper.readValue((String)response.body(), JsonNode.class);
                if (!Objects.isNull(jsonNode) && jsonNode.has("error")) {
                    decode.setCode(jsonNode.get("error").asText());
                    decode.setError(jsonNode.get("error").asText());
                }
            } catch (Exception var4) {
                ex = var4;
                log.error(ex.getMessage(), ex);
            }
        }

        decode.setStatus(response.statusCode());
        return decode;
    }

    @Generated
    private static Long $default$timestamp() {
        return (new Date()).toInstant().toEpochMilli();
    }

    @Generated
    private static String $default$code() {
        return "INTERNAL_ERROR";
    }

    @Generated
    private static Integer $default$status() {
        return 500;
    }

    @Generated
    private static String $default$error() {
        return "INTERNAL_ERROR";
    }

    @Generated
    public static BaseErrorResponseBuilder builder() {
        return new BaseErrorResponseBuilder();
    }

    @Generated
    public Long getTimestamp() {
        return this.timestamp;
    }

    @Generated
    public String getCode() {
        return this.code;
    }

    @Generated
    public Integer getStatus() {
        return this.status;
    }

    @Generated
    public String getError() {
        return this.error;
    }

    @Generated
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Generated
    public void setCode(String code) {
        this.code = code;
    }

    @Generated
    public void setStatus(Integer status) {
        this.status = status;
    }

    @Generated
    public void setError(String error) {
        this.error = error;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof BaseErrorResponse)) {
            return false;
        } else {
            BaseErrorResponse other = (BaseErrorResponse)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label59: {
                    Object this$timestamp = this.getTimestamp();
                    Object other$timestamp = other.getTimestamp();
                    if (this$timestamp == null) {
                        if (other$timestamp == null) {
                            break label59;
                        }
                    } else if (this$timestamp.equals(other$timestamp)) {
                        break label59;
                    }

                    return false;
                }

                Object this$status = this.getStatus();
                Object other$status = other.getStatus();
                if (this$status == null) {
                    if (other$status != null) {
                        return false;
                    }
                } else if (!this$status.equals(other$status)) {
                    return false;
                }

                Object this$code = this.getCode();
                Object other$code = other.getCode();
                if (this$code == null) {
                    if (other$code != null) {
                        return false;
                    }
                } else if (!this$code.equals(other$code)) {
                    return false;
                }

                Object this$error = this.getError();
                Object other$error = other.getError();
                if (this$error == null) {
                    if (other$error != null) {
                        return false;
                    }
                } else if (!this$error.equals(other$error)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof BaseErrorResponse;
    }

    @Generated
    public int hashCode() {
        boolean PRIME = true;
        int result = 1;
        Object $timestamp = this.getTimestamp();
        result = result * 59 + ($timestamp == null ? 43 : $timestamp.hashCode());
        Object $status = this.getStatus();
        result = result * 59 + ($status == null ? 43 : $status.hashCode());
        Object $code = this.getCode();
        result = result * 59 + ($code == null ? 43 : $code.hashCode());
        Object $error = this.getError();
        result = result * 59 + ($error == null ? 43 : $error.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        Long var10000 = this.getTimestamp();
        return "BaseErrorResponse(timestamp=" + var10000 + ", code=" + this.getCode() + ", status=" + this.getStatus() + ", error=" + this.getError() + ")";
    }

    @Generated
    public BaseErrorResponse(Long timestamp, String code, Integer status, String error) {
        this.timestamp = timestamp;
        this.code = code;
        this.status = status;
        this.error = error;
    }

    @Generated
    public static class BaseErrorResponseBuilder {
        @Generated
        private boolean timestamp$set;
        @Generated
        private Long timestamp$value;
        @Generated
        private boolean code$set;
        @Generated
        private String code$value;
        @Generated
        private boolean status$set;
        @Generated
        private Integer status$value;
        @Generated
        private boolean error$set;
        @Generated
        private String error$value;

        @Generated
        BaseErrorResponseBuilder() {
        }

        @Generated
        public BaseErrorResponseBuilder timestamp(Long timestamp) {
            this.timestamp$value = timestamp;
            this.timestamp$set = true;
            return this;
        }

        @Generated
        public BaseErrorResponseBuilder code(String code) {
            this.code$value = code;
            this.code$set = true;
            return this;
        }

        @Generated
        public BaseErrorResponseBuilder status(Integer status) {
            this.status$value = status;
            this.status$set = true;
            return this;
        }

        @Generated
        public BaseErrorResponseBuilder error(String error) {
            this.error$value = error;
            this.error$set = true;
            return this;
        }

        @Generated
        public BaseErrorResponse build() {
            Long timestamp$value = this.timestamp$value;
            if (!this.timestamp$set) {
                timestamp$value = BaseErrorResponse.$default$timestamp();
            }

            String code$value = this.code$value;
            if (!this.code$set) {
                code$value = BaseErrorResponse.$default$code();
            }

            Integer status$value = this.status$value;
            if (!this.status$set) {
                status$value = BaseErrorResponse.$default$status();
            }

            String error$value = this.error$value;
            if (!this.error$set) {
                error$value = BaseErrorResponse.$default$error();
            }

            return new BaseErrorResponse(timestamp$value, code$value, status$value, error$value);
        }

        @Generated
        public String toString() {
            return "BaseErrorResponse.BaseErrorResponseBuilder(timestamp$value=" + this.timestamp$value + ", code$value=" + this.code$value + ", status$value=" + this.status$value + ", error$value=" + this.error$value + ")";
        }
    }
}
