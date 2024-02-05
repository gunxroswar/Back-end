package com.Deadline.BackEnd.Backend.Dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewPostDto {

    @NotNull
    private Long UID;
    @NotBlank(message = "topic must not be Blank")
    @Size(max = 512,message = "topic is at most 512 characters.")
    private String topic;
    @NotBlank(message = "detail must not be Blank")
    @Size(max = 512,message = "topic is at most 4096 characters.")
    private String detail;
    private Boolean anonymous=false;
}
