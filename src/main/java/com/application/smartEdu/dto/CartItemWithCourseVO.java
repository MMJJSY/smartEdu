package com.application.smartEdu.dto;

import java.sql.Timestamp;

import com.application.smartEdu.enums.CourseCategory;
import com.application.smartEdu.enums.CoursePendingStatus; // Courses.status: PENDING/OPEN/CLOSE

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Carts × Courses JOIN 조회 결과 대응 VO
 * - Cart-Mapper 의 selectCartItemsWithCourseByStudentId 결과 컬럼들과 1:1 매핑
 * - coursePrice: Courses.price (VARCHAR)이지만 숫자 문자열이라면 Double 변환 가능
 *   * 가격을 정수 KRW로 다루고 싶다면 Double 대신 Long으로 교체 권장
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemWithCourseVO {

    // Carts
    private int cartId;
    private int studentId;
    private int courseId;
    private Long courseAmount;        // DECIMAL(12,0) → Long
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Courses (별칭: course_*)
    private String courseTitle;
    private CourseCategory courseCategory;
    private String courseImg;
    private String courseDescription;
    private int courseViewCount;
    private Double coursePrice;       // VARCHAR → Double 변환(숫자 문자열 가정)
    private CoursePendingStatus courseStatus; // 'PENDING','OPEN','CLOSE'

    // ✅ 추가: 강사명
    private String instructorName;
}
