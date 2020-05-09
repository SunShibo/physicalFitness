package com.ichzh.physicalFitness.service;

import java.util.Date;
import java.util.List;

import com.ichzh.physicalFitness.dto.AdmissionConditonFilterDto;
import com.ichzh.physicalFitness.model.AdmissionCondition;
import com.ichzh.physicalFitness.model.ConditionLabor;
import com.ichzh.physicalFitness.model.ConditionResidencePermit;
import com.ichzh.physicalFitness.model.EnrollmentSchedule;
import com.ichzh.physicalFitness.model.EnrollmentScheduleList;
import com.ichzh.physicalFitness.model.HighLevel;
import com.ichzh.physicalFitness.model.MiddleLevel;
import com.ichzh.physicalFitness.model.MiddleSchoolExport;
import com.ichzh.physicalFitness.model.NurserySchool;
import com.ichzh.physicalFitness.model.PrimSchoolExport;
import com.ichzh.physicalFitness.model.Ranking;
import com.ichzh.physicalFitness.model.RankingArea;
import com.ichzh.physicalFitness.model.SchoolArea;
import com.ichzh.physicalFitness.model.SchoolChoice;
import com.ichzh.physicalFitness.model.SchoolCollection;
import com.ichzh.physicalFitness.model.SchoolHeatingPower;
import com.ichzh.physicalFitness.model.SysDict;

//专用于系统启动时缓存数据和获取缓存数据的服务
public interface ICacheApplicationService {

	//缓存数据的服务入口
	public void cacheData();
	//根据学校代码获取该校的热力值
	public Float getHeatingPowerValueBy(String schoolCode, Integer yearYear);
	/**
	 * 根据学校所属区和学校类型从缓存中获取学校热力信息
	 * @param town          区.
	 * @param schoolType    学校类型 . (1: 小学  2：中学)
	 * @param level    热力等级 （取值为: 1 2  3  4  5）.
	 * @return List<SchoolHeatingPower>.
	 */
	public List<SchoolHeatingPower> getHeatingPowerInfoBy(Integer town, Integer schoolType, Integer level, String memberId);
	/**
	 * 根据学校ID从缓存中获取幼儿园信息.
	 * @param schoolId  学校标识号.
	 * @return NurserySchool.
	 */
	public NurserySchool getNurserySchoolBy(Integer schoolId);
	/**
	 * 根据学校ID从缓存中获取中小学信息.
	 * @param schoolId  学校标识号.
	 * @return SchoolChoice.
	 */
	public SchoolChoice getSchoolChoiceBy(Integer schoolId);
	/**
	 * 根据字典ID获得字典名称.
	 * @param dictId 字典ID
	 * @return
	 */
	public String getDictName(Integer dictId);
	/**
	 * 根据字典类型获得字典.
	 * @param dictType  字典类型.
	 * @return
	 */
	public List<SysDict> getDictByDictType(Integer dictType);
	/**
	 * 根据字典ID获得字典.
	 * @param dictId
	 * @return
	 */
	public SysDict getDictByDictId(Integer dictId);
	
	/**
	 * 根据学校类型和学校代码在缓存中获取学校在学校优选表总的主键.
	 * @param serviceBlock  10002 小学  10003 初中
	 * @param schoolCode  学校代码
	 * @return
	 */
	public Integer getSchoolChoiceIdBy(Integer serviceBlock, String schoolCode);
	/**
	 * 根据学校类型和学校代码在缓存中获取对应的学校优选表的数据.
	 * @param serviceBlock  10002 小学  10003 初中
	 * @param schoolCode  学校代码
	 * @return
	 */
	public SchoolChoice getSchoolChoiceBy(Integer serviceBlock, String schoolCode);
	/**
	 * 根据学校代码在缓存中获取学校的等级数据.
	 * @param schoolCode  学校代码
	 * @return
	 */
	public MiddleLevel getMiddleLevelBy(String schoolCode);
	
	/**
	 * 根据学校代码在缓存中获取学校的等级数据(高中等级).
	 * @param schoolCode  学校代码
	 * @return
	 */
	public HighLevel getHighLevelBy(String schoolCode);
	/**
	 * 获得具备在哪个区上小学资格的 所有条件组合.
	 * @param town  入学区域.
	 * @return  List<String>.  每个组个的组成：  上学阶段+"#"+入学区域+"#"+户籍+"#"+居住地
	 */
	public List<String> getAdmissionConditionCombination4primSchool(Integer town);
	
	/**
	 * 获得具备在哪个区上中学资格的 所有条件组合.
	 * @param town  入学区域.
	 * @return  List<String>.  每个组个的组成：  上学阶段+"#"+入学区域+"#"+学籍+"#"+户籍+"#"+居住地
	 */
	public List<String> getAdmissionConditionCombination4MiddleSchool(Integer town);
	/**
	 * 根据字典类型获得字典类型名称.
	 * @param dictType 字典类型.
	 * @return
	 */
	public String getDictTypeName(int dictType);
	
	/**
	 * 缓存学校收藏信息.
	 */
	public void cacheSchoolCollection();
	/**
	 * 缓存某个会员的学校收藏信息.
	 * @param memberId
	 */
	public void cacheSchoolCollectionForSomeone(String memberId);
	
	/**
	 * 检查学校是否收藏.
	 * @param memberId      会员标识号.
	 * @param serviceBlock  服务模块  10001  幼儿园  10002\10003  中小学
	 * @param schoolId  幼儿园ID和中小学ID
	 * @return
	 */
    public boolean checkSchoolIfCollected(String memberId, Integer serviceBlock, Integer schoolId);
    /**
         * 向缓存中写入新的收藏
     * @param memerId     会员ID
     * @param schoolCollection SchoolCollection.
     */
    public void writeCollectionToCache(String memerId, SchoolCollection schoolCollection);
    
    /**
          * 从缓存中移除收藏
     * @param memberId
     * @param serviceBlock
     * @param schoolId
     */
    public void removeCollectionFromCache(String memberId, Integer serviceBlock, Integer schoolId);
    /**
         * 从缓存中获取某个会员的收藏的学校.
     * @param memberId
     * @return
     */
    public List<SchoolCollection> querySchoolCollectionBy(String memberId);
    
    /**
     * 从缓存中获取区名称
     * @param town
     * @return
     */
    public String getTownNameByTown(Integer town);
    
    /**
     * 根据学段和学籍代码从缓存中获取SchoolChoice
     * @param serviceBlock  学段 10002 小学 10003 初中
     * @param schoolCode  学籍代码
     * @return
     */
    public SchoolChoice getChoiceIdByServiceBlockAndSchoolCode(String serviceBlock, String schoolCode);
    /**
     * 根据学校代码和年度从缓存中查询小学出口数据.
     * @param schoolCode  小学代码.
     * @param year  年度.
     * @return
     */
    public List<PrimSchoolExport> getPrimSchoolExportBy(String schoolCode, Integer year);
    /**
     * 根据学校代码和年度从缓存中查询中学出口数据.
     * @param schoolCode  中学代码.
     * @param year  年度.
     * @return
     */
    public List<MiddleSchoolExport> getMiddleSchoolExportBy(String schoolCode, Integer year);
    /**
     * 从缓存中获取最新年份的某一天的入学日程.
     * @param serviceBlock 学段  10002  小学  10003 初中
     * @param town  区
     * @param date 
     * @return
     */
    public EnrollmentSchedule getEnrollmentScheduleBy(Integer serviceBlock, Integer town, Date date);
    
    public EnrollmentSchedule getEnrollmentScheduleBy(Integer serviceBlock, Integer town, Date date, Integer admissionMode);
    
    public List<EnrollmentSchedule> getEnrollmentScheduleBy(Integer serviceBlock, Integer town, Integer admissionMode);
    
    public List<EnrollmentScheduleList> getEnrollmentScheduleListBy(Integer serviceBlock, Integer town, Integer admissionMode);
    
    /**
     * 从缓存中获取某个区的最新一年的排名数据
     * @return
     */
    public List<Ranking> getRankingSchoolOflatestYear(Integer town);
    
    /**
     * 从缓存中获取最新年份的入学日程.
     * @param serviceBlock 学段  10002  小学  10003 初中
     * @param town  区
     * @return
     */
    public List<EnrollmentSchedule> getEnrollmentScheduleBy(Integer serviceBlock, Integer town);
    /**
     * 从缓存中获取最新一年的某个片区排名数据(片区)
     * @return
     */
    public List<RankingArea> getRankingAreaSchoolOflatestYear(String areaName);

    /**
     * 从缓存中获取某个学校所属片区
     * @param serviceBlockId 学段  10002  小学  10003 初中
     * @param schoolCode 学校代码
     * @return
     */
    public SchoolArea getSchoolAreaBy(Integer serviceBlockId, String schoolCode);
    
    /**
     * 根据学段和区查询学籍
     * @param serviceBlock
     * @param town
     * @return
     */
    public List<AdmissionConditonFilterDto> queryStudentStatus(Integer serviceBlock, Integer town);
    
    /**
     * 根据入学阶段、区、户籍、居住查询入学条件 
     * @param serviceBlock
     * @param town
     * @param householdRegistration  户籍
     * @param residence   居住 
     * @return
     */
    public AdmissionCondition queryAdmissionConditionBy(Integer serviceBlock, Integer town, Integer householdRegistration, Integer residence);
    /**
     * 根据入学阶段、区、户籍、居住查询务工条件
     * @param serviceBlock
     * @param town
     * @param householdRegistration
     * @param residence
     * @return
     */
    public List<ConditionLabor> queryLaborBy(Integer serviceBlock, Integer town, Integer householdRegistration, Integer residence);
    
    
    public List<ConditionResidencePermit> queryConditionResidenPermitBy(Integer serviceBlock, Integer town, 
    		Integer householdRegistration, Integer residence, Integer labor, Integer residenPermit);
    
    public List<ConditionLabor> queryLaborBy(Integer serviceBlock, Integer town, Integer householdRegistration, Integer residence, Integer labor);
    /**
     * 将模块详细内容新增到缓存
     * @param tplMsgContent
     */
    public void writeTemplateMsgToCache(String tplMsgContent);
    /**
     * 检查模板消息是否
     * @param tplMsgContent
     * @return
     */
    public boolean checkTemplateMsgIfSended(String tplMsgContent);
	
}
