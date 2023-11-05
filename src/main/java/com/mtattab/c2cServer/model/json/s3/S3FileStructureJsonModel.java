package com.mtattab.c2cServer.model.json.s3;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mtattab.c2cServer.model.enums.s3.S3FileType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class S3FileStructureJsonModel {


    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM dd, yyyy")
    private Date updated;

    private S3FileType type;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<S3FileStructureJsonModel> subFolders;

    private String path;


}
