package dhi.optimizer.common;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

/**
 * Services used when the server needs multiLanguage messages. <br>
 * : 다국어 서비스.
 */
@Service
public class MessageService {
	
	@Autowired
	private MessageSource messageSource;
	 
	/**
	 * 다국어 메세지 가져오기.
	 * @param code 문구코드.
	 * @return 문자열.
	 */
	public String getMessage(String code) {
		Locale locale = LocaleContextHolder.getLocale();
		return this.messageSource.getMessage(code,null,locale);
	}

	/**
	 * 다국어 메세지 가져오기.
	 * @param code code 문구코드.
	 * @param var2 args an array of arguments that will be filled in for params within.
	 * @return 문자열.
	 */
	public String getMessage(String code, Object[] var2) {
		Locale locale = LocaleContextHolder.getLocale();
		return this.messageSource.getMessage(code,var2,locale);
	}
}