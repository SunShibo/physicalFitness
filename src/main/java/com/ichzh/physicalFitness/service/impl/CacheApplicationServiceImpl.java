package com.ichzh.physicalFitness.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.ichzh.physicalFitness.conf.Constant;
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
import com.ichzh.physicalFitness.model.SchoolMapInfo;
import com.ichzh.physicalFitness.model.SysDict;
import com.ichzh.physicalFitness.model.TemplateMsgSendLog;
import com.ichzh.physicalFitness.model.Town;
import com.ichzh.physicalFitness.repository.AdmissionConditionRepository;
import com.ichzh.physicalFitness.repository.ConditionLaborRepository;
import com.ichzh.physicalFitness.repository.ConditionResidencePermitRepository;
import com.ichzh.physicalFitness.repository.EnrollmentScheduleListRepository;
import com.ichzh.physicalFitness.repository.EnrollmentScheduleRepository;
import com.ichzh.physicalFitness.repository.HighLevelRepository;
import com.ichzh.physicalFitness.repository.MemberRepository;
import com.ichzh.physicalFitness.repository.MiddleLevelRepository;
import com.ichzh.physicalFitness.repository.MiddleSchoolExportRepository;
import com.ichzh.physicalFitness.repository.NurserySchoolRepository;
import com.ichzh.physicalFitness.repository.PrimSchoolExportRepository;
import com.ichzh.physicalFitness.repository.RankingAreaRepository;
import com.ichzh.physicalFitness.repository.RankingRepository;
import com.ichzh.physicalFitness.repository.SchoolAreaRepository;
import com.ichzh.physicalFitness.repository.SchoolChoiceRepository;
import com.ichzh.physicalFitness.repository.SchoolCollectionRepository;
import com.ichzh.physicalFitness.repository.SchoolHeatingPowerRepository;
import com.ichzh.physicalFitness.repository.SchoolMapInfoRepository;
import com.ichzh.physicalFitness.repository.SysDictRepository;
import com.ichzh.physicalFitness.repository.TemplateMsgSendLogRepository;
import com.ichzh.physicalFitness.repository.TownRepository;
import com.ichzh.physicalFitness.service.ICacheApplicationService;
import com.ichzh.physicalFitness.util.CommonUtil;
import com.ichzh.physicalFitness.util.EnrollmentScheduleComparator;
import com.ichzh.physicalFitness.util.EnrollmentScheduleListComparator;
import com.ichzh.physicalFitness.util.RankingAreaComparator;
import com.ichzh.physicalFitness.util.RankingComparator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("cacheApplicationService")
public class CacheApplicationServiceImpl  implements ICacheApplicationService{

	    @Autowired
		private MemberRepository memberRepository;
	    @Autowired
	    private SchoolHeatingPowerRepository  schoolHeatingPowerRepository;
	    @Autowired
	    private NurserySchoolRepository nurserySchoolRepository;
	    @Autowired
	    private SchoolChoiceRepository schoolChoiceRepository;
	    @Autowired
	    private SysDictRepository sysDictRepository;
	    @Autowired
	    private MiddleLevelRepository middleLevelRepository;
	    @Autowired
	    private HighLevelRepository highLevelRepository;
	    @Autowired
	    private AdmissionConditionRepository admissionConditionRepository;
	    @Autowired
	    private SchoolCollectionRepository schoolCollectionRepository;
	    @Resource
	    private RedisTemplate<String, Object> redisTemplate;
	    @Autowired
	    private TownRepository townRepository;
	    @Autowired
	    private SchoolMapInfoRepository schoolMapInfoRepository;
	    @Autowired
	    private PrimSchoolExportRepository primSchoolExportRepository;
	    @Autowired
	    private MiddleSchoolExportRepository middleSchoolExportRepository;
	    @Autowired
	    private EnrollmentScheduleRepository enrollmentScheduleRepository;
	    @Autowired
	    private EnrollmentScheduleListRepository enrollmentScheduleListRepository;
	    @Autowired
	    private RankingRepository rankingRepository;
	    @Autowired
	    private RankingAreaRepository rankingAreaRepository;
	    @Autowired
	    private SchoolAreaRepository schoolAreaRepository;
	    @Autowired
	    private ConditionLaborRepository conditionLaborRepository;
	    @Autowired
	    private ConditionResidencePermitRepository conditionResidencePermitRepository;
	    @Autowired
	    private TemplateMsgSendLogRepository templateMsgSendLogRepository;
	   
		// 缓存学校热力信息<学校代码+年度，热力值>
		private Map<String, Float> heatingPowerInfo = new HashMap<String, Float>();
		// 所有学校热力信息
		private List<SchoolHeatingPower> allSchoolHeatingPower = new ArrayList<SchoolHeatingPower>();
		//缓存幼儿园信息—list格式
		private List<NurserySchool> cacheAllNurserySchoolInfo = new ArrayList<NurserySchool>();
		//缓存中小学信息—list格式
		private List<SchoolChoice> cacheAllSchoolChoiceInfo = new ArrayList<SchoolChoice>();
		//缓存所有字典
		private List<SysDict> cacheDictes = new ArrayList<SysDict>();
		//缓存初中学校等级数据
		 private List<MiddleLevel> cacheMiddleLevel = new ArrayList<MiddleLevel>();
		 //缓存高中学校等级数据
		 private List<HighLevel> cacheHighLevel = new ArrayList<HighLevel>();
		 //缓存所有入学条件
		 private List<AdmissionCondition> cacheAdmissionCon = new ArrayList<AdmissionCondition>();
		 //缓存区
		 private List<Town> cacheTown = new ArrayList<Town>();
		 //缓存学籍代码与统计代码映射关系(key: 学段+学籍代码  value: 统计代码)
		 private Map<String, String> cacheSChoolMapInfo = new HashMap<String, String>();
		 //缓存雷达数据中 学段+统计代码 与SchoolChoice的对应关系
		 private Map<String, SchoolChoice> cacheChoiceIdInfo = new HashMap<String, SchoolChoice>();
		 //缓存小学出口
		 private List<PrimSchoolExport> cachePrimSchoolExports = new ArrayList<PrimSchoolExport>();
		 //缓存中学出口
		 private List<MiddleSchoolExport> cacheMiddleSchoolExports = new ArrayList<MiddleSchoolExport>();
		 //缓存入学日程_按天
		 private List<EnrollmentSchedule> cacheEnrollmentSchedules = new ArrayList<EnrollmentSchedule>();
		 //缓存入学日程_按时间段
		 private List<EnrollmentScheduleList> cacheEnrollmentScheduleLists = new ArrayList<EnrollmentScheduleList>();
		 
		 //缓存排名数据(全区排名)
		 private List<Ranking> cacheRankings = new ArrayList<Ranking>();
		 //缓存排名数据(片区内排名)	
		 private List<RankingArea> cacheRankingAreas = new ArrayList<RankingArea>();
		 //缓存学校片区关系
		 private List<SchoolArea> cacheSchoolAreas = new ArrayList<SchoolArea>();
		 //缓存入学条件——务工
		 private List<ConditionLabor> cacheLabors = new ArrayList<ConditionLabor>();
		 //缓存入学条件——居住证
		 private List<ConditionResidencePermit> cacheResPermits = new ArrayList<ConditionResidencePermit>();
		 //缓存模板消息发送内容
		 private Map<String, String> cacheTemplateMsgContents = new HashMap<String, String>();
		 
		public void cacheData() {
			log.info("======开始缓存数据======"); 
			//缓存热力数据
			 allSchoolHeatingPower = schoolHeatingPowerRepository.findAll();
			 if (allSchoolHeatingPower != null && allSchoolHeatingPower.size() > 0) {
				 for (SchoolHeatingPower oneHeatingPower : allSchoolHeatingPower) {
					 heatingPowerInfo.put(oneHeatingPower.getSchoolCode()+oneHeatingPower.getYearYear(), oneHeatingPower.getHeatingValue());
				 }
			 }
			 
			 //缓存幼儿园信息
			 cacheAllNurserySchoolInfo = nurserySchoolRepository.findAll();
			 //缓存中小学信息
			 cacheAllSchoolChoiceInfo = schoolChoiceRepository.findAll();
			 //缓存所有字典
			 cacheDictes = sysDictRepository.findAll();
			 // 缓存所有初中校等级
			 cacheMiddleLevel = middleLevelRepository.findAll();
			 // 缓存高中校等级
			 cacheHighLevel = highLevelRepository.findAll();
			 //缓存入学条件
			 cacheAdmissionCon = admissionConditionRepository.findAll();
			 //缓存收藏
			 cacheSchoolCollection();
			 //缓存区
			 cacheTown = townRepository.findAll();
			 
			 //缓存雷达数据中 学段+统计代码 与choiceId的对应关系
			 if (cacheAllSchoolChoiceInfo != null && cacheAllSchoolChoiceInfo.size() >0) {
				 for (SchoolChoice oneSchoolChoice : cacheAllSchoolChoiceInfo) {  
					 cacheChoiceIdInfo.put(oneSchoolChoice.getServiceBlock().toString()+oneSchoolChoice.getStatisticSchoolCode(), oneSchoolChoice);
				 }
			 }
			 
			//缓存学籍代码与统计代码映射关系(key: 学段+学籍代码  value: 统计代码)
			 List<SchoolMapInfo> schoolMapInfos = schoolMapInfoRepository.findAll();
			 if (schoolMapInfos != null && schoolMapInfos.size() > 0) {
				 for (SchoolMapInfo oneMapInfo :  schoolMapInfos) {
					 if (oneMapInfo.getSearningSection() != null) {
						 cacheSChoolMapInfo.put(oneMapInfo.getSearningSection().toString()+oneMapInfo.getSchoolCode(), oneMapInfo.getStatisticSchoolCode());
					 }
				 }
			 }
			 
			 //缓存小学出口
			 cachePrimSchoolExports = primSchoolExportRepository.findAll();
			 //缓存中学出口
			 cacheMiddleSchoolExports = middleSchoolExportRepository.findAll();
			 //缓存入学日程_按天
			 cacheEnrollmentSchedules = enrollmentScheduleRepository.findAll();
			 //缓存入学日程_按时间段
			 cacheEnrollmentScheduleLists = enrollmentScheduleListRepository.findAll();
			 //缓存排名数据(全区)
			 cacheRankings = rankingRepository.findAll();
			 //按 年倒序排列
			 if (cacheRankings != null && cacheRankings.size() > 0) {
				 Collections.sort(cacheRankings, new RankingComparator());
			 }
			 //缓存排名数据（片区）
			 cacheRankingAreas = rankingAreaRepository.findAll();
			//按 年倒序排列
			 if (cacheRankingAreas != null && cacheRankingAreas.size() > 0) {
				 Collections.sort(cacheRankingAreas, new RankingAreaComparator());
			 }
			 
			 //缓存学校片区关系
			 cacheSchoolAreas = schoolAreaRepository.findAll();
			//缓存入学条件——务工
			 cacheLabors = conditionLaborRepository.findAll();
			 //缓存入学条件——居住证
			 cacheResPermits = conditionResidencePermitRepository.findAll();
			 //缓存已经发送的模板消息内容
			  List<TemplateMsgSendLog> templateMsgSendLogs = templateMsgSendLogRepository.findAll();
			  if (templateMsgSendLogs != null && templateMsgSendLogs.size() > 0) {
				for (TemplateMsgSendLog oneSendLog : templateMsgSendLogs) {
					cacheTemplateMsgContents.put(oneSendLog.getSendContent(), oneSendLog.getLogId().toString());
				}  
			  }
			 
			 
			 log.info("======缓存数据结束======"); 
		}

		//根据学校代码获取该校的热力值
		public Float getHeatingPowerValueBy(String schoolCode, Integer yearYear) {
			return heatingPowerInfo.get(schoolCode+yearYear);
		}

		/**
		 * 根据学校所属区和学校类型从缓存中获取学校热力信息
		 * @param town          区.
		 * @param schoolType    学校类型 .
		 * @return List<SchoolHeatingPower>.
		 */
		public List<SchoolHeatingPower> getHeatingPowerInfoBy(Integer town, Integer schoolType, Integer level, String memberId) {
			
			List<SchoolHeatingPower> ret = new ArrayList<SchoolHeatingPower>();
			if (allSchoolHeatingPower != null && allSchoolHeatingPower.size() > 0) {
				for (SchoolHeatingPower oneHeatingPower : allSchoolHeatingPower) {
					Integer currentTown = oneHeatingPower.getTown();
					
					int currentLevel = oneHeatingPower.getLevel();
					
					if (currentTown != null  && level != null) {
						if (currentTown.compareTo(town) ==0 && currentLevel == level.intValue()) {
							
							//学校代码
							String schoolCode = oneHeatingPower.getSchoolCode();
							//学校类型
							Integer schoolType2 = oneHeatingPower.getSchoolType();
							SchoolChoice schoolChoice = new SchoolChoice();
							if (schoolType2 != null) {
								if (schoolType2.intValue() == 2) {
									schoolChoice = getSchoolChoiceBy(Constant.DICT_ID_10003, schoolCode);
								}else {
									schoolChoice = getSchoolChoiceBy(Constant.DICT_ID_10002, schoolCode);
								}
							}
							if (schoolChoice != null) {
								oneHeatingPower.setSchoolId(schoolChoice.getChoiceId());
								oneHeatingPower.setLongitude(schoolChoice.getLongitude());
								oneHeatingPower.setDimension(schoolChoice.getDimension());
								
								//学校是否收藏
								boolean schoolIfCollected = this.checkSchoolIfCollected(memberId, schoolChoice.getServiceBlock(), schoolChoice.getChoiceId());
								if (schoolIfCollected) {
									oneHeatingPower.setColStatus(1);
								}else {
									oneHeatingPower.setColStatus(0);
								}
							}
							
							
							ret.add(oneHeatingPower);
						}
					}
				}
			}
			
			return ret;
		}
		/**
		 * 根据学校ID从缓存中获取幼儿园信息.
		 * @param schoolId  学校标识号.
		 * @return NurserySchool.
		 */
		public NurserySchool getNurserySchoolBy(Integer schoolId) {
			NurserySchool ret = new NurserySchool();
			if (cacheAllNurserySchoolInfo != null && cacheAllNurserySchoolInfo.size() > 0) {
				for (NurserySchool oneSchool : cacheAllNurserySchoolInfo) {
					if (oneSchool.getNurserySchoolId().compareTo(schoolId) == 0) {
						ret = oneSchool;
					    break;
					}
				}
			}
			return ret;
		}
		
		/**
		 * 根据学校ID从缓存中获取中小学信息.
		 * @param schoolId  学校标识号.
		 * @return SchoolChoice.
		 */
		public SchoolChoice getSchoolChoiceBy(Integer schoolId) {
			SchoolChoice ret = new SchoolChoice();
			if (cacheAllSchoolChoiceInfo != null && cacheAllSchoolChoiceInfo.size() > 0) {
				for (SchoolChoice oneSchool : cacheAllSchoolChoiceInfo) {
					if (oneSchool.getChoiceId().compareTo(schoolId) == 0) {
						ret = oneSchool;
					    break;
					}
				}
			}
			return ret;
		}		
		/**
		 * 根据字典ID获得字典名称
		 * @param dictId
		 * @return
		 */
		public String getDictName(Integer dictId) {
			String ret = "";
			if (cacheDictes != null && cacheDictes.size() > 0) {
				for (SysDict oneDict : cacheDictes) {
					if (oneDict.getDictId().compareTo(dictId) == 0) {
						ret =  oneDict.getDictName();
						break;
					}
				}
			}
			return ret;
		}
		
		/**
		 * 根据字典ID获得字典.
		 * @param dictId
		 * @return
		 */
		public SysDict getDictByDictId(Integer dictId) {
			if (cacheDictes != null && cacheDictes.size() > 0) {
				for (SysDict oneDict : cacheDictes) {
					if (oneDict.getDictId().compareTo(dictId) == 0) {
						return oneDict;
					}
				}
			}
			return null;
		}

		/**
		 * 根据字典类型获得字典.
		 * @param dictType  字典类型.
		 * @return
		 */
		public List<SysDict> getDictByDictType(Integer dictType) {
			List<SysDict> ret = new ArrayList<SysDict>();
			if (cacheDictes != null && cacheDictes.size() > 0) {
				for (SysDict oneDict : cacheDictes) {
					if (oneDict.getDictType().compareTo(dictType) == 0) {
						ret.add(oneDict);
					}
				}
			}
			return ret;
		}

		/**
		 * 根据学校类型和学校代码在缓存中获取学校在学校优选表总的主键.
		 * @param serviceBlock  10002 小学  10003 初中
		 * @param schoolCode  学校代码
		 * @return
		 */
		public Integer getSchoolChoiceIdBy(Integer serviceBlock, String schoolCode) {
			
			if (cacheAllSchoolChoiceInfo != null && cacheAllSchoolChoiceInfo.size() > 0) {
				for (SchoolChoice oneSchool : cacheAllSchoolChoiceInfo) {
					if (oneSchool.getServiceBlock().compareTo(serviceBlock) == 0 &&  oneSchool.getSchoolCode().equals(schoolCode)) {
						return oneSchool.getChoiceId();
					}
				}
			}
			return null;
		}
		
		/**
		 * 根据学校类型和学校代码在缓存中获取学校在学校优选表的数据
		 * @param serviceBlock  10002 小学  10003 初中
		 * @param schoolCode  学校代码
		 * @return
		 */
		public SchoolChoice getSchoolChoiceBy(Integer serviceBlock, String schoolCode) {
			
			return getChoiceIdByServiceBlockAndSchoolCode(serviceBlock.toString(), schoolCode);
		}

		/**
		 * 根据学校代码在缓存中获取学校的等级数据.
		 * @param schoolCode  学校代码
		 * @return
		 */
		public MiddleLevel getMiddleLevelBy(String schoolCode) {
			
			if (cacheMiddleLevel != null && cacheMiddleLevel.size() > 0) {
				for (MiddleLevel oneLevel : cacheMiddleLevel) {
					if (oneLevel.getSchoolCode().equals(schoolCode)) {
						return oneLevel;
					}
				}
			}
			return null;
		}

		/**
		 * 根据学校代码在缓存中获取学校的等级数据(高中等级).
		 * @param schoolCode  学校代码
		 * @return
		 */
		public HighLevel getHighLevelBy(String schoolCode) {
			if (cacheHighLevel != null && cacheHighLevel.size() > 0) {
				for (HighLevel oneLevel : cacheHighLevel) {
					if (oneLevel.getSchoolCode().equals(schoolCode)) {
						return oneLevel;
					}
				}
			}
			return null;
		}

		/**
		 * 获得具备在哪个区上小学资格的 所有条件组合.
		 * @param town  入学区域.
		 * @return  List<String>.  每个组个的组成：  上学阶段+"#"+入学区域+"#"+户籍+"#"+居住地
		 */
		public List<String> getAdmissionConditionCombination4primSchool(Integer town) {
			
			List<String> ret = new ArrayList<String>();
			if (cacheAdmissionCon != null && cacheAdmissionCon.size() > 0) {
				for (AdmissionCondition oneCondition : cacheAdmissionCon) {
					// 上学阶段
					Integer serviceBlock = oneCondition.getServiceBlock();
					// 入学区域
					Integer town2 = oneCondition.getTown();
					// 户籍
					Integer householdRegistration = oneCondition.getHouseholdRegistration();
					// 居住地
					Integer residence = oneCondition.getResidence();
					// 是否有资格
					Integer isCanAdmisssion = oneCondition.getIsCanAdmission();
					
					if (isCanAdmisssion != null && isCanAdmisssion.intValue() ==1 && serviceBlock.intValue() == Constant.DICT_ID_10002 && town2.intValue() == town.intValue()) {
						ret.add(serviceBlock+"#"+town2+"#"+householdRegistration+"#"+residence);
					}
				}
			}
			return ret;
		}

		/**
		 * 获得具备在哪个区上中学资格的 所有条件组合.
		 * @param town  入学区域.
		 * @return  List<String>.  每个组个的组成：  上学阶段+"#"+入学区域+"#"+学籍+"#"+户籍+"#"+居住地
		 */
		public List<String> getAdmissionConditionCombination4MiddleSchool(Integer town) {
			
			List<String> ret = new ArrayList<String>();
			if (cacheAdmissionCon != null && cacheAdmissionCon.size() > 0) {
				for (AdmissionCondition oneCondition : cacheAdmissionCon) {
					// 上学阶段
					Integer serviceBlock = oneCondition.getServiceBlock();
					// 入学区域
					Integer town2 = oneCondition.getTown();
					// 学籍
					Integer studentStatus = oneCondition.getStudentStatus();
					// 户籍
					Integer householdRegistration = oneCondition.getHouseholdRegistration();
					// 居住地
					Integer residence = oneCondition.getResidence();
					// 是否有资格
					Integer isCanAdmisssion = oneCondition.getIsCanAdmission();
					
					if (isCanAdmisssion.intValue() == 1 && serviceBlock.intValue() == Constant.DICT_ID_10002 && town2.intValue() == town.intValue()) {
						ret.add(serviceBlock+"#"+town2+"#"+studentStatus + "#" +householdRegistration+"#"+residence);
					}
				}
			}
			return ret;
		}

		/**
		 * 根据字典类型获得字典类型名称.
		 * @param dictType 字典类型.
		 * @return
		 */
		public String getDictTypeName(int dictType) {
			
			String ret = "";
			if (cacheDictes != null && cacheDictes.size() > 0) {
				for (SysDict oneDict :  cacheDictes) {
					if (oneDict.getDictType().intValue() == dictType) {
						ret = oneDict.getTypeName();
						break;
					}
				}
			}
			return ret;
		}

		/**
		 * 缓存学校收藏信息.
		 */
		public void cacheSchoolCollection() {
			
			//缓存收藏信息
			 List<SchoolCollection> schoolCollections = schoolCollectionRepository.findAll();
			 if (schoolCollections != null && schoolCollections.size() > 0) {
				 Map<String, List<SchoolCollection>>  cacheMapSchoolCollection = new HashMap<String, List<SchoolCollection>>();
				 for (SchoolCollection oneSchoolCollection : schoolCollections) {
					 //会员ID
					 String mermberId = oneSchoolCollection.getMemberId();
					 
					 List<SchoolCollection> existSchoolCollections = cacheMapSchoolCollection.get(mermberId);
					 if (existSchoolCollections == null) {
						 existSchoolCollections = new ArrayList<SchoolCollection>();
						 existSchoolCollections.add(oneSchoolCollection);
					 }else if(!existSchoolCollections.contains(oneSchoolCollection)) {
						 existSchoolCollections.add(oneSchoolCollection);
					 }
					 cacheMapSchoolCollection.put(mermberId, existSchoolCollections);
				 }
				 for (String oneKey : cacheMapSchoolCollection.keySet()) {
					 redisTemplate.opsForValue().set("schoolCollectionInfo_"+oneKey, cacheMapSchoolCollection.get(oneKey));
				 }
			 }
		}
		
		/**
		 * 缓存某个会员的学校收藏信息.
		 * @param memberId
		 */
		public void cacheSchoolCollectionForSomeone(String memberId) {
			
			List<SchoolCollection> schoolCollections = schoolCollectionRepository.findByMemberId(memberId);
			if (schoolCollections != null && schoolCollections.size() > 0) {
				redisTemplate.opsForValue().set("schoolCollectionInfo_"+memberId, schoolCollections);
			}
			
		}

		/**
		 * 检查学校是否收藏.
		 * @param memberId      会员标识号.
		 * @param serviceBlock  服务模块  10001  幼儿园  10002\10003  中小学
		 * @param schoolId  幼儿园ID和中小学ID
		 * @return
		 */
		public boolean checkSchoolIfCollected(String memberId, Integer serviceBlock, Integer schoolId) {
			
			//从缓存中获取学校收藏信息
			List<SchoolCollection>  schoolCollections = (List<SchoolCollection>)redisTemplate.opsForValue().get("schoolCollectionInfo_"+memberId);
			if (schoolCollections != null && schoolCollections.size() > 0) {
				for (SchoolCollection oneColleciton : schoolCollections) {
					if (oneColleciton.getServiceBlock().compareTo(serviceBlock) == 0 && oneColleciton.getSchoolId().compareTo(schoolId) == 0) {
						return true;
					}
				}
			}
			
			return false;
		}

		/**
	         * 向缓存中写入新的收藏
	     * @param memerId     会员ID
	     * @param schoolCollection SchoolCollection.
	     */
		public void writeCollectionToCache(String memerId, SchoolCollection schoolCollection) {
			
			//从缓存中获取学校收藏信息
			List<SchoolCollection>  schoolCollections = (List<SchoolCollection>)redisTemplate.opsForValue().get("schoolCollectionInfo_"+memerId);
			if (schoolCollections != null && schoolCollections.size() > 0) {
				schoolCollections.add(schoolCollection);
			}else {
				schoolCollections = new ArrayList<SchoolCollection>();
				schoolCollections.add(schoolCollection);
			}
			redisTemplate.opsForValue().set("schoolCollectionInfo_"+memerId, schoolCollections);
			
		}

		/**
	         * 从缓存中移除收藏
	    * @param memberId
	    * @param serviceBlock
	    * @param schoolId
	    */
		public void removeCollectionFromCache(String memberId, Integer serviceBlock, Integer schoolId) {
			
			//从缓存中获取学校收藏信息
			List<SchoolCollection>  schoolCollections = (List<SchoolCollection>)redisTemplate.opsForValue().get("schoolCollectionInfo_"+memberId);
			List<SchoolCollection> leftoverSchoolCollections = new ArrayList<SchoolCollection>();
			if (schoolCollections != null  && schoolCollections.size() > 0) {
				ListIterator<SchoolCollection> listIterator = schoolCollections.listIterator();
				while(listIterator.hasNext()) {
					SchoolCollection oneCollection = listIterator.next();
					if (!(oneCollection.getServiceBlock().compareTo(serviceBlock) == 0 && oneCollection.getSchoolId().compareTo(schoolId) == 0)) {
						leftoverSchoolCollections.add(oneCollection);
					} 
				}  
				redisTemplate.opsForValue().set("schoolCollectionInfo_"+memberId, leftoverSchoolCollections);
			}
			
		}

		private List<SchoolCollection> listIteratorToArrayList(ListIterator<SchoolCollection> listIterator){
			List<SchoolCollection> ret = new ArrayList<SchoolCollection>();
			if (listIterator != null) {
				while(listIterator.hasNext()) {
					SchoolCollection oneCollection = listIterator.next();
					ret.add(oneCollection);
				}
			}
			return ret;
		}
		/**
	        * 从缓存中获取某个会员的收藏的学校.
	     * @param memberId
	     * @return
	     */
		public List<SchoolCollection> querySchoolCollectionBy(String memberId) {
			return (List<SchoolCollection>)redisTemplate.opsForValue().get("schoolCollectionInfo_"+memberId);
		}

		public String getTownNameByTown(Integer town) {
			
			if (cacheTown != null && cacheTown.size() >0) {
				for (Town oneTown : cacheTown) {
					if (oneTown.getTownId().compareTo(town) == 0) {
						return oneTown.getTownName();
					}
				}
			}
			return "";
		}

		/**
	     * 根据学段和学籍代码从缓存中获取choiceId(学校优选中的主键)
	     * @param serviceBlock  学段 10002 小学 10003 初中
	     * @param schoolCode  学籍代码
	     * @return
	     */
		public SchoolChoice getChoiceIdByServiceBlockAndSchoolCode(String serviceBlock, String schoolCode) {
		
			if (StringUtils.isNotEmpty(serviceBlock) &&  StringUtils.isNotEmpty(schoolCode)) {
				//根据学段和学籍代码获得对应的统计代码
				String statisticSchoolCode = cacheSChoolMapInfo.get(serviceBlock+schoolCode);
				//根据学段和统计代码获得对应的choiceId
				if (StringUtils.isNotEmpty(statisticSchoolCode)) {
					return cacheChoiceIdInfo.get(serviceBlock+statisticSchoolCode);
				}else {
					log.warn("根据学段和学籍代码未获得对应的统计代码，学段："+serviceBlock+" || 学籍代码："+schoolCode);
				}
			}
			return null;
		}

		/**
	     * 根据学校代码和年度从缓存中查询小学出口数据.
	     * @param schoolCode  小学代码.
	     * @param year  年度.
	     * @return
	     */
		public List<PrimSchoolExport> getPrimSchoolExportBy(String schoolCode, Integer year) {
			List<PrimSchoolExport> ret = new ArrayList<PrimSchoolExport>();
			if (cachePrimSchoolExports != null && cachePrimSchoolExports.size() > 0) {
				for (PrimSchoolExport onePrimSchoolExport : cachePrimSchoolExports) {
					//学校代码
					String primSchoolCode = onePrimSchoolExport.getPrimSchoolCode();
					//年度
					Integer primSChoolExportYear = onePrimSchoolExport.getYear();
					
					if (StringUtils.isNotEmpty(primSchoolCode) &&  primSChoolExportYear != null) {
						if (primSchoolCode.equals(schoolCode) && primSChoolExportYear.compareTo(year) == 0) {
							ret.add(onePrimSchoolExport);
						}
					}
				}
			}
			return ret;
		}

		 /**
	     * 根据学校代码和年度从缓存中查询中学出口数据.
	     * @param schoolCode  中学代码.
	     * @param year  年度.
	     * @return
	     */
		public List<MiddleSchoolExport> getMiddleSchoolExportBy(String schoolCode, Integer year) {
			
			List<MiddleSchoolExport> ret = new ArrayList<MiddleSchoolExport>();
			if (cacheMiddleSchoolExports != null && cacheMiddleSchoolExports.size() > 0) {
				for (MiddleSchoolExport oneMiddleSchoolExport : cacheMiddleSchoolExports) {
					//学校代码
					String middleSchoolCode = oneMiddleSchoolExport.getMiddleSchoolCode();
					//年度
					Integer middleSChoolExportYear = oneMiddleSchoolExport.getYear();
					
					if (StringUtils.isNotEmpty(middleSchoolCode) &&  middleSChoolExportYear != null) {
						if (middleSchoolCode.equals(schoolCode) && middleSChoolExportYear.compareTo(year) == 0) {
							ret.add(oneMiddleSchoolExport);
						}
					}
				}
			}
			return ret;
		}

		/**
	     * 从缓存中获取最新年份的入学日程.
	     * @param serviceBlock 学段  10002  小学  10003 初中
	     * @param town  区
	     * @return
	     */
		public EnrollmentSchedule getEnrollmentScheduleBy(Integer serviceBlock, Integer town, Date date) {
			
			List<EnrollmentSchedule> tempList = new ArrayList<EnrollmentSchedule>();
			if (cacheEnrollmentSchedules != null && cacheEnrollmentSchedules.size() > 0) {
				for (EnrollmentSchedule oneSchedule : cacheEnrollmentSchedules) {
					
					if (oneSchedule.getServiceBlock().compareTo(serviceBlock) == 0
							&& oneSchedule.getTown().compareTo(town) == 0
							&& CommonUtil.DateToDateString(oneSchedule.getScheduleDate()).equals(CommonUtil.DateToDateString(date))) {
						tempList.add(oneSchedule);
					}
				}
			}
			if (tempList == null || tempList.size() == 0) {
				return null;
			}else {
				//按年倒序排列
				Collections.sort(tempList, new EnrollmentScheduleComparator());
				return tempList.get(0);
			}
		}

		
		public EnrollmentSchedule getEnrollmentScheduleBy(Integer serviceBlock, Integer town, Date date, 
				Integer admissionMode) {
			
			List<EnrollmentSchedule> tempList = new ArrayList<EnrollmentSchedule>();
			if (cacheEnrollmentSchedules != null && cacheEnrollmentSchedules.size() > 0) {
				for (EnrollmentSchedule oneSchedule : cacheEnrollmentSchedules) {
					
					if (oneSchedule.getServiceBlock().compareTo(serviceBlock) == 0
							&& oneSchedule.getTown().compareTo(town) == 0
							&& CommonUtil.DateToDateString(oneSchedule.getScheduleDate()).
							equals(CommonUtil.DateToDateString(date))) {
						
						// 如果有入学方式根据入学方式查询
						if(admissionMode != null) {
							if(oneSchedule.getAdmissionMode().compareTo(admissionMode) == 0) {
								tempList.add(oneSchedule);
							}
						}else {
							tempList.add(oneSchedule);
						}
					}
				}
			}
			if (tempList == null || tempList.size() == 0) {
				return null;
			}else {
				//按年倒序排列
				Collections.sort(tempList, new EnrollmentScheduleComparator());
				return tempList.get(0);
			}
		}

		
		public List<EnrollmentSchedule> getEnrollmentScheduleBy(Integer serviceBlock, Integer town, Integer admissionMode) {
			List<EnrollmentSchedule> tempList = new ArrayList<EnrollmentSchedule>();
			if (cacheEnrollmentSchedules != null && cacheEnrollmentSchedules.size() > 0) {
				for (EnrollmentSchedule oneSchedule : cacheEnrollmentSchedules) {
					if (oneSchedule.getServiceBlock().compareTo(serviceBlock) == 0
							&& oneSchedule.getTown().compareTo(town) == 0) {				
						// 如果有入学方式根据入学方式查询
						if(admissionMode != null) {
							if(oneSchedule.getAdmissionMode().compareTo(admissionMode) == 0) {
								tempList.add(oneSchedule);
							}
						}else {
							tempList.add(oneSchedule);
						}
					}
				}
			}
			return tempList;
		}
		
		
		/**
	     * 从缓存中获取最新年份的入学日程.
	     * @param serviceBlock 学段  10002  小学  10003 初中
	     * @param town  区
	     * @return
	     */
		public List<EnrollmentSchedule> getEnrollmentScheduleBy(Integer serviceBlock, Integer town) {
			
			List<EnrollmentSchedule> tempList = new ArrayList<EnrollmentSchedule>();
			if (cacheEnrollmentSchedules != null && cacheEnrollmentSchedules.size() > 0) {
				//按年倒序排列
				Collections.sort(cacheEnrollmentSchedules, new EnrollmentScheduleComparator());
				//最新的年
				Integer lastestYear =  cacheEnrollmentSchedules.get(0).getYearYear();
				for (EnrollmentSchedule oneSchedule : cacheEnrollmentSchedules) {
					if (oneSchedule.getServiceBlock().compareTo(serviceBlock) == 0
							&& oneSchedule.getTown().compareTo(town) == 0
							&& oneSchedule.getYearYear().compareTo(lastestYear) == 0) {
						tempList.add(oneSchedule);
					}
				}
			}
			
			return tempList;
		}
		
	
		public List<EnrollmentScheduleList> getEnrollmentScheduleListBy(Integer serviceBlock, Integer town,
				Integer admissionMode) {
			
			List<EnrollmentScheduleList> tempList = new ArrayList<EnrollmentScheduleList>();
			if (cacheEnrollmentScheduleLists != null && cacheEnrollmentScheduleLists.size() > 0) {
				//按年倒序排列
				Collections.sort(cacheEnrollmentScheduleLists, new EnrollmentScheduleListComparator());
				//最新的年
				Integer lastestYear =  cacheEnrollmentScheduleLists.get(0).getYearYear();
				for (EnrollmentScheduleList oneSchedule : cacheEnrollmentScheduleLists) {
					if (oneSchedule.getServiceBlock().compareTo(serviceBlock) == 0
							&& oneSchedule.getTown().compareTo(town) == 0
							&& oneSchedule.getYearYear().compareTo(lastestYear) == 0 
							&& oneSchedule.getAdmissionMode().compareTo(admissionMode) == 0) {
						tempList.add(oneSchedule);
					}
				}
			}
			
			return tempList;
		}

		/**
	     * 从缓存中获取最新一年的排名数据
	     * @return
	     */
		public List<Ranking> getRankingSchoolOflatestYear(Integer town) {
			List<Ranking> ret = new ArrayList<Ranking>();
			if (cacheRankings != null && cacheRankings.size() > 0) {
				//当前年是哪一年
				Integer yearYear = cacheRankings.get(0).getYearYear();
				for (Ranking oneRanking : cacheRankings ) {
					if (oneRanking.getYearYear().compareTo(yearYear) == 0 &&
							oneRanking.getTown().compareTo(town) == 0) {
						ret.add(oneRanking);
					}
				}
			}
			return ret;
		}

		/**
	     * 从缓存中获取最新一年的某个片区排名数据(片区)
	     * @return
	     */
		public List<RankingArea> getRankingAreaSchoolOflatestYear(String areaName) {
			
			List<RankingArea> ret = new ArrayList<RankingArea>();
			if (cacheRankingAreas != null && cacheRankingAreas.size() > 0) {
				//当前年是哪一年
				Integer yearYear = cacheRankingAreas.get(0).getYearYear();
				for (RankingArea oneRanking : cacheRankingAreas ) {
					if (oneRanking.getYearYear().compareTo(yearYear) == 0 && oneRanking.getAreaName().equals(areaName)) {
						ret.add(oneRanking);
					}
				}
			}
			return ret;
		}

		/**
	     * 从缓存中获取某个学校所属片区
	     * @param serviceBlockId 学段  10002  小学  10003 初中
	     * @param schoolCode 学校代码
	     * @return
	     */
		public SchoolArea getSchoolAreaBy(Integer serviceBlockId, String schoolCode) {
			
			if (cacheSchoolAreas != null  &&  cacheSchoolAreas.size() > 0) {
				for (SchoolArea oneSchoolArea : cacheSchoolAreas) {
					if (oneSchoolArea.getServiceBlock().compareTo(serviceBlockId) == 0 &&
							oneSchoolArea.getSchoolCode().equals(schoolCode)) {
						return oneSchoolArea;
					}
				}
			}
			return null;
		}

		/**
	     * 根据学段和区查询学籍
	     * @param serviceBlock
	     * @param town
	     * @return
	     */
		public List<AdmissionConditonFilterDto> queryStudentStatus(Integer serviceBlock, Integer town) {
			
			List<AdmissionConditonFilterDto> ret = new ArrayList<AdmissionConditonFilterDto>();
			if (cacheAdmissionCon != null  && cacheAdmissionCon.size() > 0) {
				Map<Integer, String> tempMap = new HashMap<Integer, String>();
				for (AdmissionCondition oneConditon :  cacheAdmissionCon) {
					//学籍
					Integer studentStatus = oneConditon.getStudentStatus();
					
					if (!tempMap.containsKey(studentStatus)) {
						SysDict sd = this.getDictByDictId(studentStatus);
						AdmissionConditonFilterDto oneConFilter = new AdmissionConditonFilterDto();
						oneConFilter.setDictId(studentStatus);
						oneConFilter.setDictName(sd.getDictName());
						oneConFilter.setDictType(sd.getDictType());
						oneConFilter.setDictTypeName(sd.getTypeName());
						
						ret.add(oneConFilter);
					}
				}
			}
			
			return ret;
		}

		/**
	     * 根据入学阶段、区、户籍、居住查询入学条件 
	     * @param serviceBlock
	     * @param town
	     * @param householdRegistration  户籍
	     * @param residence   居住 
	     * @return
	     */
		public AdmissionCondition queryAdmissionConditionBy(Integer serviceBlock, Integer town,
				Integer householdRegistration, Integer residence) {
			
			if (cacheAdmissionCon != null  && cacheAdmissionCon.size() > 0) {
				for (AdmissionCondition oneConditon :  cacheAdmissionCon) {
					if (oneConditon.getServiceBlock().compareTo(serviceBlock) == 0 && 
							oneConditon.getTown().compareTo(town) == 0 && 
							oneConditon.getHouseholdRegistration().compareTo(householdRegistration) == 0 &&
							oneConditon.getResidence().compareTo(residence) == 0) {
						return oneConditon;
					}
				}
			}
			
			return null;
		}

		/**
	     * 根据入学阶段、区、户籍、居住查询务工条件
	     * @param serviceBlock
	     * @param town
	     * @param householdRegistration
	     * @param residence
	     * @return
	     */
		public List<ConditionLabor> queryLaborBy(Integer serviceBlock, Integer town, Integer householdRegistration,
				Integer residence) {
			List<ConditionLabor> ret = new ArrayList<ConditionLabor>();
			if (cacheLabors != null && cacheLabors.size() > 0) {
				Map<Integer, String> tempMap = new HashMap<Integer, String>();
				for (ConditionLabor  oneLabor : cacheLabors) {
					if (oneLabor.getServiceBlock().compareTo(serviceBlock) == 0 && 
							oneLabor.getTown().compareTo(town) == 0 && 
							oneLabor.getHouseholdRegistration().compareTo(householdRegistration) == 0 &&
							oneLabor.getResidence().compareTo(residence) == 0) {
						
							if (!tempMap.containsKey(oneLabor.getLabor())) {
								ret.add(oneLabor);
							}
					}
				}
			}
			return ret;
		}

		
		public List<ConditionResidencePermit> queryConditionResidenPermitBy(Integer serviceBlock, Integer town,
				Integer householdRegistration, Integer residence, Integer labor, Integer residenPermit) {
			List<ConditionResidencePermit> ret = new ArrayList<ConditionResidencePermit>();
			if (cacheResPermits != null && cacheResPermits.size() > 0) {
				for (ConditionResidencePermit onePermit :  cacheResPermits) {
					if (onePermit.getServiceBlock().compareTo(serviceBlock) == 0 && 
							onePermit.getTown().compareTo(town) == 0 && 
							onePermit.getHouseholdRegistration().compareTo(householdRegistration) == 0 &&
							onePermit.getResidence().compareTo(residence) == 0 && 
							onePermit.getLabor().compareTo(labor) == 0 && 
							onePermit.getResidencePermit().compareTo(residenPermit) == 0) {
						ret.add(onePermit);
					}
				}
			}
			return ret;
		}

		
		public List<ConditionLabor> queryLaborBy(Integer serviceBlock, Integer town, Integer householdRegistration,
				Integer residence, Integer labor) {
			
			List<ConditionLabor> ret = new ArrayList<ConditionLabor>();
			if (cacheLabors != null && cacheLabors.size() > 0) {
				for (ConditionLabor oneLabor :  cacheLabors) {
					if (oneLabor.getServiceBlock().compareTo(serviceBlock) == 0 && 
							oneLabor.getTown().compareTo(town) == 0 && 
							oneLabor.getHouseholdRegistration().compareTo(householdRegistration) == 0 &&
							oneLabor.getResidence().compareTo(residence) == 0 && 
							oneLabor.getLabor().compareTo(labor) == 0) {
						ret.add(oneLabor);
					}
				}
			}
			return ret;
		}

		/**
	     * 将模块详细内容新增到缓存
	     * @param tplMsgContent
	     */
		public void writeTemplateMsgToCache(String tplMsgContent) {
			cacheTemplateMsgContents.put(tplMsgContent, "");
		}

		/**
	     * 检查模板消息是否
	     * @param tplMsgContent
	     * @return
	     */
		public boolean checkTemplateMsgIfSended(String tplMsgContent) {
			return cacheTemplateMsgContents.containsKey(tplMsgContent);
		}
		
		
		
		
		
		
		

}
