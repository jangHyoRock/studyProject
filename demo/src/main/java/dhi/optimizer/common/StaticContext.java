package dhi.optimizer.common;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Ability to import classes managed by application bean beans. <br>
 * : Bean에 의해 관리되는 Class를 가져온다.
 */
@Component
public class StaticContext implements ApplicationContextAware {
 private static StaticContext staticContext;
	private ApplicationContext applicationContext;

	@PostConstruct
	public void registerInstance() {
		staticContext = this;
	}

	public static <T> T getBean(Class<T> clazz) {
		return staticContext.applicationContext.getBean(clazz);
	}

	public static Object getBean(String beanName) {
		return staticContext.applicationContext.getBean(beanName);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}