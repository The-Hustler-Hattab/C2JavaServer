package com.mtattab.c2cServer.model.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ReverseShellInfoInitialMessage {

    @JsonProperty("osName")
    private String osName;

    @JsonProperty("osVersion")
    private String osVersion;

    @JsonProperty("osArch")
    private String osArch;

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("userHome")
    private String userHome;

    @JsonProperty("userCurrentWorkingDir")
    private String userCurrentWorkingDir;

    @JsonProperty("userLanguage")
    private String userLanguage;

    @JsonProperty("userPublicIp")
    private String userPublicIp;


    @JsonProperty("reply")
    private String reply;


    @JsonCreator
    public ReverseShellInfoInitialMessage(@JsonProperty("osName")String osName,
                                          @JsonProperty("osVersion")String osVersion,
                                          @JsonProperty("osArch")String osArch,
                                          @JsonProperty("userName")String userName,
                                          @JsonProperty("userHome")String userHome,
                                          @JsonProperty("userCurrentWorkingDir")String userCurrentWorkingDir,
                                          @JsonProperty("userLanguage") String userLanguage,
                                          @JsonProperty("userPublicIp") String userPublicIp,

                                          @JsonProperty("reply") String reply) {
        this.osName = osName;
        this.osVersion = osVersion;
        this.osArch = osArch;
        this.userName = userName;
        this.userHome = userHome;
        this.userCurrentWorkingDir = userCurrentWorkingDir;
        this.userLanguage = userLanguage;
        this.userPublicIp = userPublicIp;
        this.reply = reply;
    }
}
