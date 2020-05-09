package com.ichzh.easy4enrolwas.web.api;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings({"unchecked","static-access"})
@RestController
@RequestMapping({"/api/test"})
@Slf4j
public class TestApi {
	
//	@Autowired
//	private InitDictionUtil initUtil;
//	@Autowired
//	private CourseRepository courseRepository;
//	@Autowired
//	private ClassHourRepository classHourRepository;
//	@Autowired
//	private DmUserRepository dmUserRepository;
//	@Autowired
//	private UserhourRepository userhourRepository;
//	
//	
//	/**
//     * 获取当前登录学生所有课程
//     * @return
//     */
//    @RequestMapping(value = "/query_student_all_hour", method = {RequestMethod.POST})
//    public List<StudentHour> queryStudentAllHour(
//            @RequestParam(value = "schoolYearId", required = false) Integer schoolYearId,
//            @RequestParam(value = "userId", required = false) Integer userId,
//            HttpServletRequest request) {
//    	DmUser du = dmUserRepository.findOne(userId);
//    	if(du==null||du.getEducationId()==null){
//    		return null;
//    	}
//    	String stuEduId= du.getEducationId();//获取学生educationId        
//        List<StudentHour> returnList = new ArrayList<StudentHour>();
//        if(schoolYearId == null ){
//        	schoolYearId = initUtil.getCurrentSchoolYear().getSchoolYearId();
//        }
//        Integer versionType = Integer.valueOf(courseRepository.querySysParam("versionType").toString());//0：redis1：数据库
//		if(versionType==0){
//			returnList = classHourRepository.queryRedisStudentHour(schoolYearId,stuEduId);
//		}else{
//			returnList = classHourRepository.queryStudentAllHour(schoolYearId, stuEduId);//查询方式为查数据库
//		}
//        return returnList;
//    }
//    
//    /**
//     * 学生端-自主预约列表
//     * @return
//     */
//    @RequestMapping(value = "/query_self_course_list", method = {RequestMethod.POST})
//    public Map<String, Object> queryStudentSelfCourseList(
//            @RequestParam(value = "pageNum", required = false) Integer pageNum,
//            @RequestParam(value = "fieldId", required = false) Integer fieldId,
//            @RequestParam(value = "instType", required = false) Integer instType,
//            @RequestParam(value = "address", required = false) Integer address,
//            @RequestParam(value = "timeStr", required = false) String timeStr,
//            @RequestParam(value = "keywords", required = false) String keywords,
//            @RequestParam(value = "orderType", required = false) Integer orderType,
//            @RequestParam(value = "userId", required = false) Integer userId,
//            HttpServletRequest request) {
//        DmUser loginStudent = dmUserRepository.findOne(userId);
//        if(loginStudent==null||loginStudent.getGradeId()==null||loginStudent.getEducationId()==null){
//        	return null;
//        }
//        Integer gradeId = loginStudent.getGradeId();
//        String stuEduId = loginStudent.getEducationId();//根据传进来的页码获取某一段数据，这里目前写成10条一页
//        Integer start = (pageNum - 1) * 10;
//        Integer end = pageNum * 10 - 1;
//        Integer versionType = Integer.valueOf(courseRepository.querySysParam("versionType").toString());//0：redis  1：数据库
//        if(versionType==0){
//        	return courseRepository.queryByRedis(fieldId,instType,address,timeStr,keywords,orderType,gradeId,stuEduId,start,end);
//        }else{
//        	return courseRepository.queryStudentSelfCoursePage1(fieldId,instType,address,timeStr,keywords,orderType,gradeId,stuEduId,start,end);
//        }
//    }
//    
//    /**
//     * 学生端-自主预约列表redis
//     * @return
//     */
//    @RequestMapping(value = "/query_self_course_list_redis", method = {RequestMethod.POST})
//    public Map<String, Object> queryStudentSelfCourseListRedis(
//            @RequestParam(value = "pageNum", required = false) Integer pageNum,
//            @RequestParam(value = "fieldId", required = false) Integer fieldId,
//            @RequestParam(value = "instType", required = false) Integer instType,
//            @RequestParam(value = "address", required = false) Integer address,
//            @RequestParam(value = "timeStr", required = false) String timeStr,
//            @RequestParam(value = "keywords", required = false) String keywords,
//            @RequestParam(value = "orderType", required = false) Integer orderType,
//            @RequestParam(value = "userId", required = false) Integer userId,
//            HttpServletRequest request) {
//        Integer gradeId = 21;
//        String stuEduId = "11249599";
//        Integer start = (pageNum - 1) * 10;
//        Integer end = pageNum * 10 - 1;
//        Integer versionType = Integer.valueOf(courseRepository.querySysParam("versionType").toString());//0：redis  1：数据库
//        if(versionType==0){
//        	return courseRepository.queryByRedis(fieldId,instType,address,timeStr,keywords,orderType,gradeId,stuEduId,start,end);
//        }else{
//        	return courseRepository.queryStudentSelfCoursePage1(fieldId,instType,address,timeStr,keywords,orderType,gradeId,stuEduId,start,end);
//        }
//    }
//    
//    /**
//     * 学生端-课程预约-详情页面
//     *
//     * @return
//     */
//    @RequestMapping(value = "/query_stu_course", method = {RequestMethod.POST})
//    public Map<String, Object> queryStudentOrderCourse(
//            @RequestParam(value = "courseId", required = false) Integer courseId,
//            @RequestParam(value = "type", required = false) Integer type,
//            @RequestParam(value = "town", required = false) Integer town,
//            @RequestParam(value = "date", required = false) String date,
//            @RequestParam(value = "stuEduId",required = false)String stuEduId,
//            @RequestParam(value = "userId",required = false)Integer userId,
//            HttpServletRequest request) {
//        DmUser loginUser=dmUserRepository.findOne(userId);
//        if(loginUser==null||loginUser.getEducationId()==null){
//        	return null;
//        }
//        if(loginUser.getGroupId()==4){
//            stuEduId=loginUser.getEducationId();
//        }
//        if(courseId==null||courseId.equals("")){
//        	return null;
//        }
//        Map<String, Object> course = courseRepository.queryStudentOrderCourse(courseId, stuEduId, type, town, date, loginUser);
//        return course;
//    }
//    
//    /**
//     * 学生端-课程预约-约课
//     *
//     * @return
//     */
//    @RequestMapping(value = "/stu_order_hour", method = {RequestMethod.POST})
//    public Map<String, Object> stuOrderHour(
//            @RequestParam(value = "id", required = false) Integer classHourId,
//            @RequestParam(value = "stuEduId", required = false) String stuEduId,
//            @RequestParam(value = "ordertype", required = false) Integer ordertype,
//            @RequestParam(value = "userId", required = false) Integer userId,
//            HttpServletRequest request) {
//    	DmUser loginStu = dmUserRepository.findOne(userId);
//        if (stuEduId == null || stuEduId.equals("")) {
//        	if(loginStu==null || loginStu.getEducationId() == null){
//        		return null;
//        	}else{
//        		stuEduId = loginStu.getEducationId();
//        	}
//        }
//        Integer groupId = loginStu.getGroupId();
//        Map<String, Object> result = userhourRepository.stuOrderHour(request, classHourId, stuEduId, groupId, loginStu, ordertype);
//        return result;
//    }
}
