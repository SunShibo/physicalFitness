package com.ichzh.physicalFitness.util;

import org.hibernate.cfg.ImprovedNamingStrategy;
  
@SuppressWarnings("deprecation")
public class ParseTableName extends ImprovedNamingStrategy {  
/** 
 *  
 */  
private static final long serialVersionUID = 3088474161734101900L;  
  
	@Override
	public String tableName(String tableName){
		return tableName.toUpperCase();
	}
} 
