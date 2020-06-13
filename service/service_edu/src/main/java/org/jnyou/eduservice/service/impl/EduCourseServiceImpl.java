package org.jnyou.eduservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jnyou.commonutils.Constast;
import org.jnyou.eduservice.entity.EduCourse;
import org.jnyou.eduservice.entity.EduCourseDescription;
import org.jnyou.eduservice.entity.vo.CourseInfoVo;
import org.jnyou.eduservice.entity.vo.CoursePublishVo;
import org.jnyou.eduservice.mapper.EduCourseDescriptionMapper;
import org.jnyou.eduservice.mapper.EduCourseMapper;
import org.jnyou.eduservice.service.EduCourseService;
import org.jnyou.servicebase.exception.IsMeException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author jnyou
 * @since 2020-06-07
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    @Autowired
    private EduCourseDescriptionMapper descriptionMapper;

    /**
     * 添加课程基本info
     * @param courseInfoVo
     */
    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {

        // 1、向课程表添加基本信息
        EduCourse course = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,course);
        int insert = this.baseMapper.insert(course);
        if(insert == 0){
            // 添加失败
            throw new IsMeException(-1,"添加课程信息失败");
        }

        // 获取添加成功之后的课程id
        String cid = course.getId();

        // 2、向课程简介表添加简介信息
        EduCourseDescription description = new EduCourseDescription();
        description.setDescription(courseInfoVo.getDescription());
        // 课程id和课程简介的id应该一致，他们是一对一的关系
        description.setId(cid);
        int insert1 = descriptionMapper.insert(description);
        if(insert1 == 0){
            throw new IsMeException(-1,"课程简介信息保存失败");
        }
        return cid;
    }

    /**
     * 根据课程ID查询课程信息
     * @param courseId
     * @return
     */
    @Override
    public CourseInfoVo getCourseInfoByCourseId(String courseId) {
        // 课程信息
        EduCourse course = this.baseMapper.selectById(courseId);
        // 课程简介信息
        EduCourseDescription courseDescription = descriptionMapper.selectById(courseId);

        // 封装VO对象
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        BeanUtils.copyProperties(course,courseInfoVo);
        courseInfoVo.setDescription(courseDescription.getDescription());
        return courseInfoVo;
    }

    /**
     * 修改课程信息
     * @return
     */
    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        // 修改课程信息
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int n = this.baseMapper.updateById(eduCourse);
        // 修改课程简介
        EduCourseDescription description = new EduCourseDescription();
        description.setDescription(courseInfoVo.getDescription());
        description.setId(courseInfoVo.getId());
        int m = descriptionMapper.updateById(description);
        if(n == 0){
            throw new IsMeException(-1,"修改课程信息失败");
        }
        if(m == 0){
            throw new IsMeException(-1,"修改课程简介失败");
        }
    }

    /**
     * 根据课程ID查询发布的课程信息
     * @param courseId
     * @return
     */
    @Override
    public CoursePublishVo queryCoursePublishInfo(String courseId) {
        CoursePublishVo coursePublishVo = this.baseMapper.queryCoursePublishInfo(courseId);
        return coursePublishVo;
    }

    /**
     * 根据课程ID发布课程
     * @param id
     */
    @Override
    public boolean publishCourseById(String id) {
        EduCourse course = new EduCourse();
        course.setId(id);
        course.setStatus(Constast.COURSE_NORMAL);
        Integer count = baseMapper.updateById(course);
        return null != count && count > 0;
    }
}