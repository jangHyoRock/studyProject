package dhi.common.util;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

/*
 * Services used when the server needs multilingual messages.
 */
@Service
public class MessageService {
	
	@Autowired
	private MessageSource messageSource;
	 
	public String getMessage(String code) {
		Locale locale = LocaleContextHolder.getLocale();
		return this.messageSource.getMessage(code,null,locale);
	}
		 
	public String getMessage(String code, Object[] var2) {
		Locale locale = LocaleContextHolder.getLocale();
		return this.messageSource.getMessage(code,var2,locale);
	}
}