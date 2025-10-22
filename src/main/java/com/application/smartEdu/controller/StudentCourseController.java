// package com.application.smartEdu.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;

// import com.application.smartEdu.command.CourseWithLectureDetailVO;
// import com.application.smartEdu.dto.CoursesVO;
// import com.application.smartEdu.dto.MemberVO;
// import com.application.smartEdu.service.CourseService;

// import jakarta.servlet.ServletContext;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpSession;

// @Controller
// @RequestMapping("/courses")
// public class StudentCourseController {
//     @Autowired
//     private CourseService courseService;

//     @GetMapping("/alldetail")
//     public String CourseWidthLectureDetail(@RequestParam Integer courseId, Model model, HttpServletRequest request)
//             throws Exception {
//         String url = "/course/detail";
//         CourseWithLectureDetailVO course = courseService.getAlldetail(courseId);
//         model.addAttribute("course", course.getCourse());
//         model.addAttribute("lectures", course.getLectures());
//         return url;
//     }

//     @GetMapping("/detail/{courseId}")
//     public String CourseDetail(@RequestParam Integer courseId, Model model, HttpServletRequest request)
//             throws Exception {

//         String url = "courses/detail";
//         ServletContext application = request.getServletContext();

//         HttpSession session = request.getSession();
//         MemberVO loginUser = (MemberVO) session.getAttribute("loginUser");

//         String key = "course:"+loginUser.getEmail()+courseId;
//         Object value = application.getAttribute(key);

        

//         CoursesVO course = null;
//         if (value != null ) {
//             course = courseService.getCourse(courseId);
//         } else {
//             course = courseService.getCourse(courseId);
//             if (key != null) {
//                 application.setAttribute(key, "");
//             }
//         }
//         model.addAttribute("course", course);

//         return url;
//     }


// }
