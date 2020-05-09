package com.ichzh.physicalFitness.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class YamlResourceLoadUtil {

	public static List<Map<String, String>> loadYamlResource(String yamlFileName) throws IOException{
		List<Map<String, String>> ret = new ArrayList<>();
		Resource[] resources = new PathMatchingResourcePatternResolver().getResources(yamlFileName);
		if(resources.length>0){
		    YamlMapFactoryBean yaml=new YamlMapFactoryBean();
		    yaml.setResources(resources);
		    yaml.getObject().forEach((k,v)->{
		    	 Map<String, String> map=new HashMap<>();
		    	if(v instanceof Map){
		    		map.putAll((Map<String, String>)v);
		    	}else{
			    	if(v!=null)
			    		map.put(k, v.toString());	
		    	}
		    	ret.add(map);
		    });
		}
		return ret;
	}
}
