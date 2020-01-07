package com.sgy.util.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * 获取springmvc自动注解的相关bean对象
 * @author chang
 * @createDate Jul 26, 2013
 * @description
 */
public class SpringUtil implements BeanFactoryAware {

	private static BeanFactory beanFactory;

	//容器在实例化实现了BeanFactoryAware接口的Bean时，会自动将容器本身注入该Bean  
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	/**
	 * 获取BeanFactory
	 * @return
	 */
	public static BeanFactory getBeanfactory() {
		return beanFactory;
	}
	
	/**
	 * 根据beanname获取bean对象
	 * @return
	 */
	public static Object getSpringBean(String beanName) {
		return beanFactory.getBean(beanName);
	}
	
	/**
	 * 判断是否有bean
	 * @param name
	 * @return
	 */
	public static boolean containsBean(String name) {
		return beanFactory.containsBean(name);
	}

}
