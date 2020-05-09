package com.ichzh.physicalFitness.util;

import java.math.BigDecimal;

import lombok.extern.slf4j.Slf4j;

/**
 * 学校热力计算工具
 *  热力共分五个级别
 * @author yjf
 *
 */
@Slf4j
public class HeatingPowerLevelUtil {

	public static int getHeatingValueLevel(Float heatingValue) {
		int ret = 5;
		try
		{
			if (heatingValue.floatValue() >= 0.1 && heatingValue.floatValue() <= 0.29) {
				ret = 5;
			}else if(heatingValue.floatValue() >= 0.3 && heatingValue.floatValue() <= 0.49) {
				ret = 4;
			}else if(heatingValue.floatValue() >= 0.5 && heatingValue.floatValue() <= 0.69) {
				ret = 3;
			}else if(heatingValue.floatValue() >= 0.7 && heatingValue.floatValue() <= 0.89) {
				ret = 2;
			}else if(heatingValue.floatValue() >= 0.9 && heatingValue.floatValue() <= 1) {
				ret = 1;
			}
		}catch(Exception ex) {
			log.warn(ex.getMessage(), ex);
		}
		return ret;
	}
	/**
	 * 热力分析数据——占比对应的等级  1  小  2  中  3 大
	 * @param heatingValue
	 * @return
	 */
	public static int getHeatingValueLevel2(BigDecimal radio) {
		int ret = 1;
		try
		{
			if (radio.floatValue() <= 33) {
				ret = 1;
			}else if (radio.floatValue() > 33 && radio.floatValue() <= 66) {
				ret = 2;
			}else if (radio.floatValue() > 66) {
				ret = 3;
			}
		}catch(Exception ex) {
			log.warn(ex.getMessage(), ex);
		}
		return ret;
	}
}
