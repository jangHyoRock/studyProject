sap.ui.define([], function() {
	"use strict";
	return {
		/**
		 * 오늘 날짜(시분초일월년)를 HH24MISSDDMMYYYY 포맷의 문자열로 반환한다.
		 * @return <String> HH24MISSDDMMYYYY
		 */
		getDate: function(){
			var today = new Date();
		    var year = today.getFullYear();
		    var month = (today.getMonth() + 1);
		    var day = today.getDate();
		    var hour = today.getHours();
		    var min = today.getMinutes();
		    var second = today.getSeconds();
		    var millisecond = today.getMilliseconds();

		    if (parseInt(month) < 10)
		        month = "0" + month;
		    if (parseInt(day) < 10)
		        day = "0" + day;
		    if (parseInt(hour) < 10)
		        hour = "0" + hour;
		    if (parseInt(min) < 10)
		        min = "0" + min;
		    if (parseInt(second) < 10)
		        second = "0" + second;
		    if (parseInt(millisecond) < 10) {
		        millisecond = "00" + millisecond;
		    } else {
		        if (parseInt(millisecond) < 100)
		            millisecond = "0" + millisecond;
		    }

		    return String(hour) + String(min) + String(second)+ String(day) + String(month) + String(year);
		},
		/**
		 * 오늘날짜를 가져온다.
		 * @param _sepa 구분자
		 * @return <String> ddMMyyyy
		 */
		getToday: function(_sepa){

			var today = new Date();
		    var year = today.getFullYear();
		    var month = (today.getMonth() + 1);
		    var day = today.getDate();

		    if (parseInt(month) < 10)
		        month = "0" + month;
		    if (parseInt(day) < 10)
		        day = "0" + day;

		    var todayStr = '';
		    if(_sepa != null){
		    	todayStr = String(day) + _sepa + String(month) + _sepa + String(year);
		    }else{
		    	todayStr = String(day) + String(month) + String(year);
		    }
		    return todayStr;
		},
		/**
		 * 날짜에서 년/월/일을 자유롭게 더하고 뺀 결과를 문자열로 반환한다.
		 *
		 * @param _d 기준날짜
		 * @param year 가감할년수
		 * @param month 가감할월수
		 * @param day 가감할일수
		 * @return YYYYMMDD
		 */
		calcDay : function(_d, year, month, day) {
			var pattern = /[^(0-9)]/gi;
			if(pattern.test(_d)){
				_d.replace(pattern,"");
			}
		    var sz_ymd;
		    if (year == "")
		        year = 0;
		    if (month == "")
		        month = 0;
		    if (day == "")
		        day = 0;
		    var y,m,d;
		    d= _d.substr(0,2);
		    m= _d.substr(2,2);
		    y= _d.substr(4,4);
		    var date = new Date(y,m-1,d);
		    date.setFullYear(date.getFullYear() + year);// y년을 더함
		    date.setMonth(date.getMonth() + month);// m월을 더함
		    date.setDate(date.getDate() + day);// d일을 더함

		    sz_ymd = "" + date.getFullYear();

		    if (date.getMonth() < 9) {
		        sz_ymd += "0" + (date.getMonth() + 1);
		    } else {
		        sz_ymd += (date.getMonth() + 1);
		    }
		    if (date.getDate() < 10) {
		        sz_ymd += "0" + date.getDate();
		    } else {
		        sz_ymd += "" + date.getDate();
		    }
		    return sz_ymd;
		},
		/**
		 * 일월년 포맷의 두 날짜를 년월일 포맷으로 변경 후 비교한다.
		 *
		 * @param <String> fromdate 시작날짜(일월년 포맷용)
		 * @param <String> todate 종료날짜(일월년 포맷용)
		 * @return 종료날짜보다 시작날짜가 클 경우 false
		 */
		compareDates : function(fromdate, todate) {
			if(fromdate && todate){
				var regExp = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/gi;
				var startdate = fromdate.replace(regExp, "");
				var enddate = todate.replace(regExp, "");
				var d1 = startdate.substr(4,4) + startdate.substr(2,2) + startdate.substr(0,2);
				var d2 = enddate.substr(4,4) + enddate.substr(2,2) + enddate.substr(0,2);
				if(d1>d2){
					return false;
				}
				return true;
			}else{
				return false;
			}
		},
		/**
		 * 두 날짜 사이의 차일을 리턴한다
		 *
		 * @param <String> fromdate 시작날짜
		 * @param <String> todate 종료날짜
		 * @return 종료날짜에서 시작날짜의 차일
		 */
		minusDates : function(fromdate, todate) {

		    var tmpFromDate = new Date(parseInt(Number(fromdate.substring(0, 4))), parseInt(Number(fromdate.substring(4, 6))) - 1, parseInt(Number(fromdate.substring(6))));
		    var tmpNextDate = new Date(parseInt(Number(todate.substring(0, 4))), parseInt(Number(todate.substring(4, 6))) - 1, parseInt(Number(todate.substring(6))));
		    var days = (tmpNextDate - tmpFromDate) / (3600 * 24 * 1000);

		    return days;
		},
		/**
		 * shortMonths 날짜 변환을 한다.
		 * @param _sepa 구분자
		 * @return <String> sm.dd.yyyy
		 */
		getShortMonthDate: function(_date) {
			var monthNames = ["JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"];
			var dateArr = _date.split('.');
			var month = monthNames[parseInt(dateArr[1]) - 1];

		    return month + '. ' + dateArr[0] + '. ' + dateArr[2];
		}
	};
});