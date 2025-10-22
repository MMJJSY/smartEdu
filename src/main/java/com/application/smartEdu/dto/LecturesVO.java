package com.application.smartEdu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LecturesVO {
    private int lectureId;
    private int courseId;
    private int sectionNo;
    private String title;
    private String video; // DB 저장용
    private String description;
    private Status status;
    
    public enum Status{
        ACTIVE,INACTIVE
    }
}
